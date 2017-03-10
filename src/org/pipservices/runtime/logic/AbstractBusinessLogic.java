package org.pipservices.runtime.logic;

import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.commands.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Abstract implementation for all microservice business logic components
 * that are able to perform business functions (commands).
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public class AbstractBusinessLogic extends AbstractComponent implements IBusinessLogic {
	private CommandSet _commands = new CommandSet();
	
	/**
	 * Creates instance of abstract functional component
	 * @param descriptor the unique descriptor that is used to identify and locate the component.
	 */
	protected AbstractBusinessLogic(ComponentDescriptor descriptor) {
		super(descriptor);
	}

	/**
	 * Get all supported commands
	 * @return a list with all commands supported by component. 
	 */
	public List<ICommand> getCommands() {
		return _commands.getCommands();
	}
	
	/**
	 * Find a specific command by its name.
	 * @param command the command name.
	 * @return an object with command name.
	 */
	public ICommand findCommand(String command) {
		return _commands.findCommand(command);
	}

	/**
	 * Adds a command to the command set.
	 * @param command a command instance to be added
	 */
	protected void addCommand(ICommand command) {
		_commands.addCommand(command);
	}

	/**
	 * Adds commands from another command set to this one.
	 * @param commands a command set that contains commands to be added
	 */
	protected void addCommandSet(CommandSet commands) {
		_commands.addCommandSet(commands);
	}
	
	/**
	 * Delegates all commands to another functional component.
	 * @param component a functional component to perform delegated commands.
	 */
	protected void delegateCommands(IBusinessLogic component) {
		_commands.addCommands(component.getCommands());
	}
	
	/**
	 * Adds intercepter to the command set.
	 * @param interceptor an intercepter instance to be added.
	 */
	protected void addIntercepter(ICommandIntercepter interceptor) {
		_commands.addIntercepter(interceptor);
	}
	
	/**
	 * Execute command by its name with specified arguments.
	 * @param command the command name.
	 * @param correlationId a unique correlation/transaction id
	 * @param args a list of command arguments.
	 * @return the execution result.
	 * @throws MicroserviceError when execution fails for any reason.
	 */
	public Object execute(
		String command, String correlationId, DynamicMap args
	) throws MicroserviceError {
		return _commands.execute(command, correlationId, args);
	}
	
	/**
	 * Validates command arguments.
	 * @param command the command name.
	 * @param args a list of command arguments.
	 * @return a list of validation errors or empty list when arguments are valid.
	 */
	public List<MicroserviceError> validate(String command, DynamicMap args) {
		return _commands.validate(command, args);
	}
}
