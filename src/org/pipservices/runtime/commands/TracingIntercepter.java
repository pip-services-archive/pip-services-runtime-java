package org.pipservices.runtime.commands;

import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Intercepter that writes trace messages for every executed command.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public class TracingIntercepter implements ICommandIntercepter {
	private List<ILogger> _loggers;
	private String _verb;
	
    /**
     * Creates instance of tracing intercepter
     * @param loggers a logger component.
     * @param verb a verb for tracing message as '<verb> <command>, ...'
     */
	public TracingIntercepter(List<ILogger> loggers, String verb) {
		_loggers = loggers;
		_verb = verb != null ? verb : "Executing";
	}

	/**
	 * Gets the command name.
	 * @param command the intercepted command
	 * @return the command name
	 */
	public String getName(ICommand command) {
		return command.getName();
	}
	
	/**
	 * Executes the command given specific arguments as an input.
	 * @param command the intercepted command
	 * @param correlationId a unique correlation/transaction id
	 * @param args command arguments
	 * @return execution result.
	 * @throws MicroserviceError when execution fails for whatever reason.
	 */
	public Object execute(ICommand command, String correlationId, DynamicMap args) throws MicroserviceError {
		// Write trace message about the command execution
		if (_loggers != null && _loggers.size() > 0) {
			String name = command.getName();
			String message = _verb + " " + name + " command";
			if (correlationId != null)
				message += ", correlated to " + correlationId;

			for (ILogger logger : _loggers)
				logger.log(LogLevel.Trace, null, correlationId, new Object[] { message });
		}
		
		return command.execute(correlationId, args);
	}
	
	/**
	 * Performs validation of the command arguments.
	 * @param command the intercepted command
	 * @param args command arguments
	 * @return a list of errors or empty list if validation was successful.
	 */
	public List<MicroserviceError> validate(ICommand command, DynamicMap args) {
		return command.validate(args);
	}

}
