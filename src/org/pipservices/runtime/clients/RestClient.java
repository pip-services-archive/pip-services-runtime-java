package org.pipservices.runtime.clients;

import java.util.*;

import javax.ws.rs.core.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.*;
import com.sun.jersey.api.json.*;
import com.sun.jersey.core.util.*;

public class RestClient extends AbstractClient {
    private final static DynamicMap DefaultConfig = DynamicMap.fromTuples(
        "endpoint.protocol", "http"
        //"endpoint.host", "localhost",
        //"endpoint.port", 3000,
    );

    private String _path;
	protected Client _client;
	protected WebResource _resource;
	
	protected RestClient(ComponentDescriptor descriptor, String path) {
		super(descriptor);			
		_path = path;
	}
	
	/**
	 * Sets component configuration parameters and switches component
	 * to 'Configured' state. The configuration is only allowed once
	 * right after creation. Attempts to perform reconfiguration will 
	 * cause an exception.
	 * @param config the component configuration parameters.
	 * @throws MicroserviceError when component is in illegal state 
	 * or configuration validation fails. 
	 */
	public void configure(ComponentConfig config) throws MicroserviceError {
        super.configure(config.withDefaults(RestClient.DefaultConfig));
	}

	/**
	 * Opens the component, performs initialization, opens connections
	 * to external services and makes the component ready for operations.
	 * Opening can be done multiple times: right after linking 
	 * or reopening after closure.  
	 * @throws MicroserviceError when initialization or connections fail.
	 */
	@Override
    public void open() throws MicroserviceError {
		checkNewStateAllowed(State.Opened);
				
		Endpoint endpoint = resolveEndpoint();
    	String protocol = endpoint.getProtocol();
        String host = endpoint.getHost();
        int port = endpoint.getPort();
        String url = protocol + "://" + host + ":" + port + "/" + _path;

        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        _client = Client.create(clientConfig);
        
        _resource = _client.resource(url);
        //_resource.accept("application/json");
        //_resource.type(MediaType.APPLICATION_JSON);
        
        super.open();
    }

	/**
	 * Closes the component and all open connections, performs deinitialization
	 * steps. Closure can only be done from opened state. Attempts to close
	 * already closed component or in wrong order will cause exception.
	 * @throws MicroserviceError with closure fails.
	 */
    @Override
    public void close() throws MicroserviceError {
		checkNewStateAllowed(State.Closed);

		// Close client
        _client = null;
        _resource = null;

        super.close();
    }
    
    private Endpoint resolveEndpoint() throws MicroserviceError {
    	List<Endpoint> endpoints = _config.getEndpoints();
    	if (endpoints.size() == 0)
    		throw new ConfigError(this, "NoEndpoint", "Service endpoint is not configured in the client");
    	
    	// Todo: Complete implementation
    	Endpoint endpoint = endpoints.get(0);
    	validateEndpoint(endpoint);
    	return endpoint;
    }
    
    private void validateEndpoint(Endpoint endpoint) throws MicroserviceError {
        // Check for type
        String protocol = endpoint.getProtocol();
        if (!"http".equals(protocol))
            throw new ConfigError(this, "SupportedProtocol", "Protocol type  is not supported by REST transport")
            	.withDetails(protocol);

        // Check for host
        if (endpoint.getHost() == null)
            throw new ConfigError(this, "NoHost", "No host is configured in REST transport");

        // Check for port
        if (endpoint.getPort() == 0)
            throw new ConfigError(this, "NoPort", "No port is configured in REST transport");
    }
    
    protected MultivaluedMap<String, String> createQueryParams() {
    	MultivaluedMap<String, String> params = new MultivaluedMapImpl();
    	return params;
    }
        
    protected void addCorrelationId(MultivaluedMap<String, String> params, String correlationId) {
    	if (correlationId == null) return;
    	
    	params.add("correlation_id", correlationId);
    }

	protected void addFilterParams(MultivaluedMap<String, String> params, FilterParams filter) {
    	if (filter == null) return;
    		
    	for (Map.Entry<String, Object> entry : filter.entrySet()) {
    		String value = Converter.toString(entry.getValue());
    		if (value != null)
    			params.add(entry.getKey(), value);
    	}
    }

    protected void addPagingParams(MultivaluedMap<String, String> params, PagingParams paging) {
    	if (paging == null) return;
    	
    	if (paging.getSkip() != null)
    		params.add("skip", Converter.toString(paging.getSkip()));
    	if (paging.getTake() != null)
    		params.add("take", Converter.toString(paging.getTake()));
		params.add("total", Converter.toString(paging.isTotal()));
    }

}
