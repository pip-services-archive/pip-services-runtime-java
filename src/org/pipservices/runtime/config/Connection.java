package org.pipservices.runtime.config;

import org.pipservices.runtime.portability.*;

/**
 * Database connection configuration as set in the component config.
 * It usually contains a complete uri or separate host, port, user, password, etc.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-19
 */
public class Connection {
	private DynamicMap _content;

	/**
	 * Creates an empty instance of connection.
	 */
	public Connection() {
		_content = new DynamicMap();
	}
	
	/**
	 * Create an instance of database connection with free-form configuration map.
	 * @param content a map with the connection configuration parameters. 
	 */
	public Connection(DynamicMap content) {
		if (content == null)
			throw new NullPointerException("Content is not set");
		
		_content = content;
	}

	/**
	 * Gets connection as free-form configuration set.
	 * @return a free-form map with connection configuration.
	 */
	public DynamicMap getRawContent() {
		return _content;
	}
	
	/**
	 * Gets the connection type
	 * @return the connection type
	 */
	public String getType() {
		return _content.getNullableString("type");
	}

	/**
	 * Gets the connection host name or ip address.
	 * @return a string representing service host 
	 */
	public String getHost() {
		return _content.getNullableString("host");
	}

	/**
	 * Gets the connection port number
	 * @return integer representing the service port.
	 */
	public int getPort() {
		return _content.getInteger("port");
	}

	/**
	 * Gets the database name
	 * @return the database name
	 */
	public String getDatabase() {
		return _content.getNullableString("database");
	}

	/**
	 * Gets the connection user name.
	 * @return the user name 
	 */
	public String getUsername() {
		return _content.getNullableString("username");
	}

	/**
	 * Gets the connection user password.
	 * @return the user password 
	 */
	public String getPassword() {
		return this._content.getNullableString("password");
	}

	/**
	 * Gets the endpoint uri constracted from type, host and port
	 * @return uri as <type>://<host | ip>:<port>
	 */
	public String getUri() {
		return getType() + "://" + getHost() + ":" + getPort();
	}
}
