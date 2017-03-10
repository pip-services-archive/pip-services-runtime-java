package org.pipservices.runtime.commands;

import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.errors.UnknownError;
import org.pipservices.runtime.portability.*;
import org.pipservices.runtime.validation.*;

/**
 * Represents a command that implements a command pattern
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-13
 */
public class Command implements ICommand {
	private IComponent _component;
	private String _name;
	private Schema _schema;
	private ICommandFunction _function;
	
	/**
	 * Creates a command instance
	 * @param component a component this command belongs to
	 * @param name the name of the command
	 * @param schema a validation schema for command arguments
	 * @param function an execution function to be wrapped into this command.
	 */
	public Command(IComponent component, String name, Schema schema, ICommandFunction function) {
		if (name == null)
			throw new NullPointerException("Command name is not set");
		if (function == null)
			throw new NullPointerException("Command function is not set");
		
		_component = component;
		_name = name;
		_schema = schema;
		_function = function;
	}

	/**
	 * Gets the command name.
	 * @return the command name
	 */
	@Override
	public String getName() {
		return _name;
	}

	/**
	 * Executes the command given specific arguments as an input.
	 * @param correlationId a unique correlation/transaction id
	 * @param args command arguments
	 * @return execution result.
	 * @throws MicroserviceError when execution fails for whatever reason.
	 */
	@Override
	public Object execute(String correlationId, DynamicMap args) throws MicroserviceError {
		// Validate arguments
		if (_schema != null) {
			List<MicroserviceError> errors = validate(args);
			// Throw the 1st error
			if (errors.size() > 0)
				throw errors.get(0);
		}
		
		// Call the function
		try {
			return _function.execute(correlationId, args);
		}
		// Intercept unhandled errors
		catch (Throwable ex) {
			throw new UnknownError(
				_component, 
				"CommandFailed", 
				"Execution " + _name + " failed: " + ex
			)
			.withDetails(_name)
			.withCorrelationId(correlationId)
			.wrap(ex);
		}
	}

	/**
	 * Performs validation of the command arguments.
	 * @param args command arguments
	 * @return a list of errors or empty list if validation was successful.
	 */
	@Override
	public List<MicroserviceError> validate(DynamicMap args) {
		// When schema is not defined, then skip validation
		if (_schema == null) 
			return new ArrayList<MicroserviceError>();
		
		// ToDo: Complete implementation
		return new ArrayList<MicroserviceError>();
	}
}
