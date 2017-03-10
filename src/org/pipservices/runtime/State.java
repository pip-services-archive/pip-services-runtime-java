package org.pipservices.runtime;

/**
 * State in lifecycle of components or the entire microservice
 *  
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class State {
	/**
	 * Undefined state
	 */
	public static final int Undefined = -1;
	
	/**
	 * Initial state right after creation
	 */
	public static final int Initial = 0;
	
	/**
	 * Configuration was successfully completed
	 */
	public static final int Configured = 1;
	
	/**
	 * Links between components were successfully set
	 */
	public static final int Linked = 2;
	
	/**
	 * Ready to perform operations
	 */
	public static final int Opened = 3;

	/**
	 * Ready to perform operations.
	 * This is a duplicate for Opened. 
	 */
	public static final int Ready = 3;

	/**
	 * Closed but can be reopened
	 */
	public static final int Closed = 4;
}
