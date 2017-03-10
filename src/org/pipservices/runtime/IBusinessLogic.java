package org.pipservices.runtime;

import java.util.*;

import org.pipservices.runtime.commands.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Interface for components that implement microservice
 * business logic: controllers or decorators.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public interface IBusinessLogic extends IComponent {
	/**
	 * Get all supported commands
	 * @return a list with all commands supported by component. 
	 */
	List<ICommand> getCommands();
	
	/**
	 * Find a specific command by its name.
	 * @param command the command name.
	 * @return a found command or <b>null</b> if nothing was found
	 */
	ICommand findCommand(String command);
	
	/**
	 * Execute command by its name with specified arguments.
	 * @param command the command name.
	 * @param correlationId a unique correlation/transaction id
	 * @param args a list of command arguments.
	 * @return the execution result.
	 * @throws MicroserviceError when execution fails for any reason.
	 */
	Object execute(String command, String correlationId, DynamicMap args) throws MicroserviceError;
	
	/**
	 * Validates command arguments.
	 * @param command the command name.
	 * @param args a list of command arguments.
	 * @return a list of validation errors or empty list when arguments are valid.
	 */
	List<MicroserviceError> validate(String command, DynamicMap args);
}
