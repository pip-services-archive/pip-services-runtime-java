package org.pipservices.runtime.run;

import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.logs.*;

/**
 * Utility logger to write messages to configured logs 
 * or to console when no logs are found. 
 * This logger is used in the microservice build/run process 
 *  
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
class LogWriter {
    /**
     * Writes a message to the all logs
     * @param components a list of microservice components to choose logs from
     * @param level a log level - Fatal, Error, Warn, Info, Debug or Trace
     * @param component a component name
     * @param correlationId a correlation id
     * @param message message objects
     */
    public static void log(List<? extends IComponent> components, int level, 
		String component, String correlationId, Object[] message) {
    	
        boolean logged = false;

        // Output to all loggers
        if (components != null && components.size() > 0) {
            for (IComponent cref : components) {
                if (cref instanceof ILogger) {
                    ILogger logger = (ILogger)cref;
                    logger.log(level, component, correlationId, message);
                    logged = true;
                }
            }
        }

        // If nothing was logged then write to console
        if (logged == false) {
            String output = LogFormatter.format(level, message);
            if (correlationId != null)
                output += ", correlated to " + correlationId;

            if (level >= LogLevel.Fatal && level <= LogLevel.Warn)
                System.err.println(output);
            else System.out.println(output);
        }
    }
	
    public static void fatal(List<? extends IComponent> components, Object... message) {
        log(components, LogLevel.Fatal, null, null, message);
    }

    public static void error(List<? extends IComponent> components, Object... message) {
        log(components, LogLevel.Error, null, null, message);
    }

    public static void warn(List<? extends IComponent> components, Object... message) {
        log(components, LogLevel.Warn, null, null, message);
    }

    public static void info(List<? extends IComponent> components, Object... message) {
        log(components, LogLevel.Info, null, null, message);
    }

    public static void debug(List<? extends IComponent> components, Object... message) {
        log(components, LogLevel.Debug, null, null, message);
    }

    public static void trace(List<? extends IComponent> components, Object... message) {
        log(components, LogLevel.Trace, null, null, message);
    }
	
}
