package org.pipservices.runtime.commands;

import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Intercepter that times execution elapsed time.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public class TimingIntercepter implements ICommandIntercepter {
	private ICounters _counters;
	private String _suffix;
	
	/**
	 * Creates instance of timing intercepter.
	 * @param counters a reference to performance counters
	 * @param suffix a suffix to create a counter name as <command>.<suffix>
	 */
	public TimingIntercepter(ICounters counters, String suffix) {
		_counters = counters;
		_suffix = suffix != null ? suffix : "exec_time";
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
		// Starting measuring elapsed time
		ITiming timing = null;
		if (_counters != null) {
			String name = command.getName() + "." + _suffix;
			timing = _counters.beginTiming(name);
		}
		
		try {
			return command.execute(correlationId, args);
		} finally {
			// Complete measuring elapsed time
			if (timing != null) timing.endTiming();
		}
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
