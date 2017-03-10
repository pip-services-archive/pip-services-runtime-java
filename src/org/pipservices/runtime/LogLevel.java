package org.pipservices.runtime;

/**
 * Logging levels to determine details of logged messages
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public class LogLevel {
	/**
	 * Nothing to be logged
	 */
    public static final int None = 0;
    
    /**
     * Logs only fatal errors that cause microservice to fail
     */
    public static final int Fatal = 1;
    
    /**
     * Logs all errors - fatal or recoverable
     */
    public static final int Error = 2;
    
    /**
     * Logs errors and warnings
     */
    public static final int Warn = 3;
    
    /**
     * Logs errors and important information messages
     */
    public static final int Info = 4;
    
    /**
     * Logs everything up to high-level debugging information
     */
    public static final int Debug = 5;
    
    /**
     * Logs everything down to fine-granular debugging messages
     */
    public static final int Trace = 6;
}
