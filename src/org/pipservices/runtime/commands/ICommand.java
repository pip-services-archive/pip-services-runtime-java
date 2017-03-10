package org.pipservices.runtime.commands;

import java.util.*;

import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Interface for commands that execute functional operations.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public interface ICommand {
	/**
	 * Gets the command name.
	 * @return the command name
	 */
	String getName();
	
	/**
	 * Executes the command given specific arguments as an input.
	 * @param correlationId a unique correlation/transaction id
	 * @param args command arguments
	 * @return execution result.
	 * @throws MicroserviceError when execution fails for whatever reason.
	 */
	Object execute(String correlationId, DynamicMap args) throws MicroserviceError;
	
	/**
	 * Performs validation of the command arguments.
	 * @param args command arguments
	 * @return a list of errors or empty list if validation was successful.
	 */
	List<MicroserviceError> validate(DynamicMap args);
}
