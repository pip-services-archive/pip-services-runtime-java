package org.pipservices.runtime.logs;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

public abstract class AbstractLogger extends AbstractComponent implements ILogger {
	private final static DynamicMap DefaultConfig = DynamicMap.fromTuples(
		"options.level", LogLevel.Info
	); 

	protected int _level = LogLevel.Info;
	
	protected AbstractLogger(ComponentDescriptor descriptor) {
		super(descriptor);
	}

	/**
	 * Sets component configuration parameters and switches from component
	 * to 'Configured' state. The configuration is only allowed once
	 * right after creation. Attempts to perform reconfiguration will 
	 * cause an exception.
	 * @param config the component configuration parameters.
	 * @throws MicroserviceError when component is in illegal state 
	 * or configuration validation fails. 
	 */
	@Override
    public void configure(ComponentConfig config) throws MicroserviceError {
		checkNewStateAllowed(State.Configured);
        super.configure(config.withDefaults(DefaultConfig));
        _level = parseLevel(_config.getOptions().get("level"));
    }

	/**
	 * Parses log level from configuration file
	 * @param level log level value
	 * @return parsed log level
	 */	
    protected int parseLevel(Object level) {
        if (level == null) return LogLevel.Info;

        level = level.toString().toUpperCase();
        if ("0".equals(level) || "NOTHING".equals(level) || "NONE".equals(level))
            return LogLevel.None;
        else if ("1".equals(level) || "FATAL".equals(level))
            return LogLevel.Fatal;
        else if ("2".equals(level) || "ERROR".equals(level))
            return LogLevel.Error;
        else if ("3".equals(level) || "WARN".equals(level) || "WARNING".equals(level))
            return LogLevel.Warn;
        else if ("4".equals(level) || "INFO".equals(level))
            return LogLevel.Info;
        else if ("5".equals(level) || "DEBUG".equals(level))
            return LogLevel.Debug;
        else if ("6".equals(level) || "TRACE".equals(level))
            return LogLevel.Trace;
        else
            return LogLevel.Info;
	}

	/**
	 * Get the current level of details
	 * @see LogLevel
	 * @return returns the current log level
	 */
	public int getLevel() { return _level; }

    /**
     * Writes a message to the log
     * @param level a log level - Fatal, Error, Warn, Info, Debug or Trace
     * @param component a component name
     * @param correlationId a correlationId
     * @param message a message objects
     */
    public abstract void log(int level, String component, String correlationId, Object[] message);
}
