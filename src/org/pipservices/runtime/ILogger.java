package org.pipservices.runtime;

/**
 * Logger that logs messages from other microservice components.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public interface ILogger extends IComponent {
	/**
	 * Get the current level of details
	 * @see LogLevel
	 * @return returns the current log level
	 */
	int getLevel();

    /**
     * Writes a message to the log
     * @param level a log level - Fatal, Error, Warn, Info, Debug or Trace
     * @param component a component name
     * @param correlationId a correlationId
     * @param message a message objects
     */
    void log(int level, String component, String correlationId, Object[] message);
}
