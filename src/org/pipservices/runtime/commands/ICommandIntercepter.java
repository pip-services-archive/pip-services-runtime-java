package org.pipservices.runtime.commands;

import java.util.*;

import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Interface for stackable command intercepters
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public interface ICommandIntercepter {
	/**
	 * Gets the command name. Intercepter can modify the name if needed
	 * @param command the intercepted command
	 * @return the command name
	 */
	String getName(ICommand command);
	
	/**
	 * Executes the command given specific arguments as an input.
	 * @param command the intercepted command
	 * @param correlationId a unique correlation/transaction id
	 * @param args map with command arguments
	 * @return execution result.
	 * @throws MicroserviceError when execution fails for whatever reason.
	 */
	Object execute(ICommand command, String correlationId, DynamicMap args) throws MicroserviceError;
	
	/**
	 * Performs validation of the command arguments.
	 * @param command the intercepted command
	 * @param args map with command arguments
	 * @return a list of errors or empty list if validation was successful.
	 */
	List<MicroserviceError> validate(ICommand command, DynamicMap args);
}
