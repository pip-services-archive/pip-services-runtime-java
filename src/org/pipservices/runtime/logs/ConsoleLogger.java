package org.pipservices.runtime.logs;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;

public class ConsoleLogger extends AbstractLogger {
	/**
	 * Unique descriptor for the ConsoleLogger component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Logs, "pip-services-runtime-log", "console", "*"
	);
	
    public ConsoleLogger() {
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
    public void log(int level, String component, String correlationId, Object[] message) {
        if (this.getLevel() < level) return;

        String output = LogFormatter.format(level, message);
        if (correlationId != null)
            output += ", correlated to " + correlationId; 

        if (level >= LogLevel.Fatal && level <= LogLevel.Warn)
        	System.err.println(output);
        else System.out.println(output);
    }                
}
