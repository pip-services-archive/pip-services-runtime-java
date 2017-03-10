package org.pipservices.runtime.commands;

import java.util.List;

import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Interceptor wrapper to turn it into stackable command
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public class InterceptedCommand implements ICommand {
	private ICommandIntercepter _intercepter;
	private ICommand _next;
	
	/**
	 * Creates instance of intercepted command by chaining
	 * intercepter with the next intercepter in the chain 
	 * or command at the end of the chain.
	 * @param intercepter the intercepter reference.
	 * @param next the next intercepter or command in the chain.
	 */
	public InterceptedCommand(ICommandIntercepter intercepter, ICommand next) {
		_intercepter = intercepter;
		_next = next;
	}
	
	/**
	 * Gets the command name.
	 * @return the command name
	 */
	public String getName() {
		return _intercepter.getName(_next);
	}
	
	/**
	 * Executes the command given specific arguments as an input.
	 * @param correlationId a unique correlation/transaction id
	 * @param args command arguments
	 * @return execution result.
	 * @throws MicroserviceError when execution fails for whatever reason.
	 */
	public Object execute(String correlationId, DynamicMap args) throws MicroserviceError {
		return _intercepter.execute(_next, correlationId, args);
	}
	
	/**
	 * Performs validation of the command arguments.
	 * @param args command arguments
	 * @return a list of errors or empty list if validation was successful.
	 */
	public List<MicroserviceError> validate(DynamicMap args) {
		return _intercepter.validate(_next, args);
	}
}
