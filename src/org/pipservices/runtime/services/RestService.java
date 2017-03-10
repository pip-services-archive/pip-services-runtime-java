package org.pipservices.runtime.services;

import java.net.*;

import javax.ws.rs.core.*;

import com.sun.jersey.api.container.httpserver.*;
import com.sun.jersey.api.core.*;
import com.sun.jersey.api.json.*;
import com.sun.net.httpserver.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Interoperable REST service that exposes a specific version of 
 * a microservice API via HTTP/HTTPS endpoint to consumers.  
 * 
 * This implementation uses Jersey library to define a RESTful API.
 * Authors of specific REST services must define get/post/update/delete
 * methods in descendant classes.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-06
 */
public abstract class RestService extends AbstractService {
	/**
	 * Default configuration for the REST service
	 */
	private final static DynamicMap DefaultConfig = DynamicMap.fromTuples(
		"endpoint.protocol", "http",
		"endpoint.host", "0.0.0.0",
		//"endpoint.port", 3000,
		"endpoint.requestMaxSize", 1024 * 1024,
		"endpoint.connectTimeout", 60000,
		"endpoint.debug", true
	); 
		
	@SuppressWarnings("restriction")
	private HttpServer _server;

	/**
	 * Creates instance of abstract REST service.
	 * @param descriptor the unique descriptor that is used to identify and locate the component.
	 */
	protected RestService(ComponentDescriptor descriptor) {
		super(descriptor);
	}
	
	/**
	 * Sets component configuration parameters and switches from component
	 * to 'Configured' state. The configuration is only allowed once
	 * right after creation. Attempts to perform reconfiguration will 
	 * cause an exception.
	 * @param config the component configuration parameters.
	 * @throws MicroserviceError when component is in illegal state 
	 * or configuration validation fails. 
	 */
	@Override
	public void configure(ComponentConfig config) throws MicroserviceError {
        super.configure(config.withDefaults(DefaultConfig));
	}
	
	/**
	 * Opens the component, performs initialization, opens connections
	 * to external services and makes the component ready for operations.
	 * Opening can be done multiple times: right after linking 
	 * or reopening after closure.  
	 * @throws MicroserviceError when initialization or connections fail.
	 */
	@SuppressWarnings("restriction")
	@Override
    public void open() throws MicroserviceError {
		checkNewStateAllowed(State.Opened);

		Endpoint endpoint = resolveEndpoint();
        String protocol = endpoint.getProtocol();
        String host = endpoint.getHost();
        int port = endpoint.getPort();
        URI uri = UriBuilder.fromUri(protocol + "://" + host).port(port).path("/").build();

        try {
        	ResourceConfig resourceConfig = new DefaultResourceConfig();
        	resourceConfig.getSingletons().add(this);
        	resourceConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
        	
        	_server = HttpServerFactory.create(uri, resourceConfig);
            _server.start();

            super.open();
        } catch (Exception ex) {
            _server = null;            
            throw new ConnectionError(this, "ConnectFailed", "Opening REST service failed")
            	.withCause(ex);
        }
    }

	/**
	 * Closes the component and all open connections, performs deinitialization
	 * steps. Closure can only be done from opened state. Attempts to close
	 * already closed component or in wrong order will cause exception.
	 * @throws MicroserviceError with closure fails.
	 */
	@SuppressWarnings("restriction")
	@Override
    public void close() throws MicroserviceError {
		checkNewStateAllowed(State.Closed);
		
        if (_server != null) {
            // Eat exceptions
            try {
                _server.stop(0);
            } catch (Exception ex) {
                warn(null, "Failed while closing REST service", ex);
            }

            _server = null;
        }

        super.close();
    }

	/**
	 * Gets configured endpoint, validates and resolves it when needed
	 * via service discovery (to be implemented).
	 * @return validated and resolved endpoint configuration
	 * @throws MicroserviceError when endpoint configuration is invalid 
	 * or discovery fails 
	 */
    private Endpoint resolveEndpoint() throws MicroserviceError {
    	// Todo: Complete implementation
    	Endpoint address = _config.getEndpoint();
    	validateEndpoint(address);
    	return address;
    }
    
    /**
     * Validates a given endpoint for correct configuration.
     * If validation doesn't pass it throws an exception.
     * @param endpoint an endpoint to be validated
     * @throws MicroserviceError when endpoint is invalid/
     */
    private void validateEndpoint(Endpoint endpoint) throws MicroserviceError {
        // Check for type
        String protocol = endpoint.getProtocol();
        if (!"http".equals(protocol))
            throw new ConfigError(this, "NotSupported", "Protocol type  is not supported by REST transport")
            	.withDetails(protocol);

        // Check for host
        if (endpoint.getHost() == null)
            throw new ConfigError(this, "NoHost", "No host is configured in REST transport");

        // Check for port
        if (endpoint.getPort() == 0)
            throw new ConfigError(this, "NoPort", "No port is configured in REST transport");
    }

}
