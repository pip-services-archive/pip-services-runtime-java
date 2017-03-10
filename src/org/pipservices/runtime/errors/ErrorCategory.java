package org.pipservices.runtime.errors;

/**
 * Defines broad categories of microservice errors.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class ErrorCategory {
	/**
	 * Internal errors caused by programming or unexpected errors
	 */
	public final static String UnknownError = "UnknownError";
	
	/**
	 * Errors happened during microservice build process
	 * and caused by problems in component factories
	 */
	public final static String BuildError = "BuildError";
	
	/**
	 * Errors related to mistakes in microservice 
	 * user-defined configuration
	 */
	public final static String ConfigError = "ConfigError";
	
	/**
	 * Errors related to operations called in wrong component state.
	 * For instance, business calls when component is not ready
	 */
	public final static String StateError = "StateError";
	
	/**
	 * Errors happened during connection to remote services.
	 * They can be related to misconfiguration, network issues
	 * or remote service itself 
	 */
	public final static String ConnectionError = "ConnectionError";

    /**
     * Errors returned by remote services or network
     * during call attempts
     */
	public final static String CallError = "CallError";

	/**
	 * Errors in read/write file operations
	 */
	public final static String FileError = "FileError";

	/**
	 * Errors due to improper user requests, like
	 * missing or wrong parameters 
	 */
	public final static String BadRequest = "BadRequest";
	
	/**
	 * Access errors caused by missing user identity
	 * or security permissions
	 */
	public final static String Unauthorized = "Unauthorized";

    /**
     * Error caused by attempt to access missing object
     */
	public final static String NotFound = "NotFound";
	
	/**
	 * Errors raised by conflict in object versions
	 * posted by user and stored on server.
	 */
	public final static String Conflict = "Conflict";	
	
	/**
	 * Errors caused by calls to unsupported 
	 * or not yet implemented functionality
	 */
	public final static String Unsupported = "Unsupported";
}
