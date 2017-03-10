package org.pipservices.runtime.config;

import org.pipservices.runtime.portability.*;

/**
 * Service address as set in component configuration or
 * retrieved by discovery service. It contains service protocol,
 * host, port number, timeouts and additional configuration parameters.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class Endpoint {
	private DynamicMap _content;

	/**
	 * Creates an empty instance of endpoint.
	 */
	public Endpoint() {
		_content = new DynamicMap();
	}
	
	/**
	 * Create an instance of service address with free-form configuration map.
	 * @param content a map with the address configuration parameters. 
	 */
	public Endpoint(DynamicMap content) {
		if (content == null)
			throw new NullPointerException("Content is not set");
		
		_content = content;
	}

	/**
	 * Gets endpoint as free-form configuration set.
	 * @return a free-form map with address configuration.
	 */
	public DynamicMap getRawContent() {
		return _content;
	}
	
	/**
	 * Checks if discovery registration or resolution shall be performed.
	 * The discovery is requested when 'discover' parameter contains 
	 * a non-empty string that represents the discovery name.
	 * @return <b>true</b> if the address shall be handled by discovery 
	 * and <b>false</b> when all address parameters are defined statically.
	 */
	public boolean useDiscovery() {
		return _content.has("discover") || _content.has("discovery");
	}
	
	/**
	 * Gets a name under which the address shall be registered or resolved
	 * by discovery service. 
	 * @return a name to register or resolve the address
	 */
	public String getDiscoveryName() {
		String discover = _content.getNullableString("discover");
		discover = discover != null ? discover : _content.getNullableString("discovery");
		return discover;
	}
	
	/**
	 * Gets the endpoint protocol
	 * @return the endpoint protocol
	 */
	public String getProtocol() {
		return _content.getNullableString("protocol");
	}

	/**
	 * Gets the service host name or ip address.
	 * @return a string representing service host 
	 */
	public String getHost() {
		String host = _content.getNullableString("host");
		host = host != null ? host : _content.getNullableString("ip");
		return host;
	}

	/**
	 * Gets the service port number
	 * @return integer representing the service port.
	 */
	public int getPort() {
		return _content.getInteger("port");
	}

	/**
	 * Gets the service user name.
	 * @return the user name 
	 */
	public String getUsername() {
		return _content.getNullableString("username");
	}

	/**
	 * Gets the service user password.
	 * @return the user password 
	 */
	public String getPassword() {
		return _content.getNullableString("password");
	}

	/**
	 * Gets the endpoint uri constructed from protocol, host and port
	 * @return uri as <protocol>://<host | ip>:<port>
	 */
	public String getUri() {
		return getProtocol() + "://" + getHost() + ":" + getPort();
	}
}
