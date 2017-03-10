package org.pipservices.runtime.logs;

import org.pipservices.runtime.config.*;

public class NullLogger extends AbstractLogger {
	/**
	 * Unique descriptor for the Null Logger component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Logs, "pip-services-runtime-log", "null", "*"
	);

	public NullLogger() {
		super(Descriptor);
	}
	
    /**
     * Writes a message to the log
     * @param level a log level - Fatal, Error, Warn, Info, Debug or Trace
     * @param component a component name
     * @param correlationId a correlationId
     * @param message a message objects
     */
	@Override
    public void log(int level, String component, String correlationId, Object[] message) {}
}
