package org.pipservices.runtime.config;

import java.util.*;

import org.pipservices.runtime.portability.*;

/**
 * Stores configuration for microservice component 
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class ComponentConfig {
	private String _category;
	private DynamicMap _content;
	
	/**
	 * Creates an empty component configuration
	 * This constructor is used in testing
	 */
	public ComponentConfig() {
		_category = Category.Undefined;
		_content = new DynamicMap();
	}

	/**
	 * Creates instance of component configuration with values
	 * retrieved from microservice configuration section.
	 * @param category a component category
	 * @param content configuration parameters
	 */
	public ComponentConfig(String category, DynamicMap content) {
		if (category == null)
			throw new NullPointerException("Category is not set");
		if (content == null)
			throw new NullPointerException("Content is empty");
		
		_category = category;
		_content = content;
	}
	
	/**
	 * Gets the raw content of the configuration as dynamic map
	 * @return dynamic map with all component configuration parameters.
	 */
	public DynamicMap getRawContent() {
		return _content;
	}

	/**
	 * Sets default values to the configuration
	 * @param defaultContent default configuration
	 * @return a reference to this configuration for chaining or passing through.
	 */
	public ComponentConfig withDefaults(DynamicMap defaultContent) {
		_content = _content.merge(defaultContent, true);
		return this;
	}	

	/**
	 * Sets default values to the configuration
	 * @param defaultsTuples default configuration represented by <key> + <value> tuples
	 * @return a reference to this configuration for chaining or passing through.
	 */
	public ComponentConfig withDefaultTuples(Object... defaultTuples) {
		DynamicMap defaultContent = new DynamicMap();
		defaultContent.setTuplesArray(defaultTuples);
		return this.withDefaults(defaultContent);
	}	

	/**
	 * Gets component descriptor. It is read from 'descriptor' object if it exists.
	 * If not, then the descriptor is created based on fields of the root object.
	 * @return the component descriptor
	 */
	public ComponentDescriptor getDescriptor() {
		DynamicMap values = _content.getMap("descriptor");
		
		return new ComponentDescriptor(
			_category,
			values.getNullableString("group"),
			values.getNullableString("type"),
			values.getNullableString("version")
		);
	}
	
	/**
	 * Gets connection parameters from 'connection' object 
	 * or with parameters from the root object.
	 * This method is usually used by persistence components
	 * to get connections to databases.
	 * @return database connection parameters or <b>null</b> if connection is not set
	 */
	public Connection getConnection() {
		DynamicMap values = _content.getNullableMap("connection");
		return values != null ? new Connection(values) : null;
	}

	/**
	 * Gets a list of database connections from 'connections' or 'connection' objects
	 * This method is usually used by persistence components that may connect to one of few database servers.
	 * @return a list with database connections
	 */
	public List<Connection> getConnections() {
		// Get configuration parameters for connections
		List<Object> values = _content.getNullableArray("connections");
		values = values != null ? values : _content.getNullableArray("connection");
		
		// Convert configuration parameters to connections
		List<Connection> connections = new ArrayList<Connection>();
		
		// Convert list of values
		if (values != null) {
			for (Object value : values) {
				connections.add(new Connection(DynamicMap.fromValue(value)));
			}
		}

		// Return the result
		return connections;
	}

	/**
	 * Gets a service endpoint from 'endpoint' object 
	 * or with parameters from the root object.
	 * This method is usually used by services that need to bind to a single endpoint.
	 * @return a service endpoint or <b>null</b> if endpoint is not set
	 */
	public Endpoint getEndpoint() {
		DynamicMap values = _content.getNullableMap("endpoint");
		return values != null ? new Endpoint(values) : null;
	}

	/**
	 * Gets a list of service endpoint from 'endpoints' or 'endpoint' objects
	 * or with parameters from the root object.
	 * This method is usually used by clients that may connect to one of few services.
	 * @return a list with service endpoints
	 */
	public List<Endpoint> getEndpoints() {
		// Get configuration parameters for endpoints
		List<Object> values = _content.getNullableArray("endpoints");
		values = values != null ? values : _content.getNullableArray("endpoint");
		
		// Convert configuration parameters to endpoints
		List<Endpoint> endpoints = new ArrayList<Endpoint>();
		// Convert list of values
		if (values != null) {
			for (Object value : values) {
				endpoints.add(new Endpoint(DynamicMap.fromValue(value)));
			}
		}

		// Return the result
		return endpoints;
	}
	
	/**
	 * Gets component free-form configuration options.
	 * The options are read from 'options', 'settings' or 'params' objects.
	 * If they are not found, then options are created using fields from the root object.
	 * @return a map with free-form component options or <b>null</b> when options are not set. 
	 */
	public DynamicMap getOptions() {
		return _content.getNullableMap("options");		
	}

	/**
	 * Creates component configuration using free-form objects.
	 * This method of configuration is usually used during testing.
	 * The configuration is created with 'Undefined' category
	 * since it's not used to create a component.
	 * @param value a free-form object
	 * @return constructed component configuration
	 */
	public static ComponentConfig fromValue(Object value) {
        DynamicMap content = DynamicMap.fromValue(value);
        return new ComponentConfig(Category.Undefined, content);
	}

	/**
	 * Creates component configuration using hardcoded parameters.
	 * This method of configuration is usually used during testing.
	 * The configuration is created with 'Undefined' category
	 * since it's not used to create a component.
	 * @param tuples configuration parameters as <key> + <value> tuples
	 * @return constructed component configuration
	 */
	public static ComponentConfig fromTuples(Object... tuples) {
        DynamicMap content = DynamicMap.fromTuples(tuples);
        return new ComponentConfig(Category.Undefined, content);
	}
}
