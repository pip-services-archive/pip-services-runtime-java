package org.pipservices.runtime.commands;

import java.util.*;

import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Handles command registration and execution.
 * Enables intercepters to control or modify command behavior 
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public class CommandSet {
	private List<ICommand> _commands = new ArrayList<ICommand>();
	private Map<String, ICommand> _commandsByName = new HashMap<String, ICommand>();
	private List<IEvent> _events = new ArrayList<IEvent>();
    private Map<String, IEvent> _eventsByName = new HashMap<String, IEvent>();
	private List<ICommandIntercepter> _intercepters = new ArrayList<ICommandIntercepter>();
	
	/**
	 * Create a command set instance.
	 */
	public CommandSet() {}
	
	/**
	 * Get all supported commands
	 * @return a list with all commands supported by the component. 
	 */
	public List<ICommand> getCommands() {
		return _commands;
	}
	
	/**
	 * Get all supported events;
	 * @return a list with all supported events by the component.
	 */
	public List<IEvent> getEvents() {
		return _events;
	}
	
	/**
	 * Find a specific command by its name.
	 * @param command the command name.
	 * @return an object with command name.
	 */
	public ICommand findCommand(String command) {
		return _commandsByName.get(command);
	}

	/**
	 * Find a specific event by its name.
	 * @param event the event name.
	 * @return an object with command name.
	 */
	public IEvent findEvent(String event) {
		return _eventsByName.get(event);
	}

	/**
	 * Builds execution chain including all intercepters
	 * and the specified command.
	 * @param command the command to build a chain.
	 */
	private void buildCommandChain(ICommand command) {
		ICommand next = command;
		for (int i = _intercepters.size() - 1; i >= 0; i--) {
			next = new InterceptedCommand(_intercepters.get(i), next);
		}
		_commandsByName.put(next.getName(), next);
	}

	/**
	 * Rebuilds execution chain for all registered commands.
	 * This method is typically called when intercepters are changed.
	 * Because of that it is more efficient to register intercepters
	 * before registering commands (typically it will be done in abstract classes).
	 * However, that performance penalty will be only once during creation time. 
	 */
	private void rebuildAllCommandChains() {
		_commandsByName.clear();
		for (ICommand command : _commands) {
			buildCommandChain(command);
		}
	}
	
	/**
	 * Adds a command to the command set.
	 * @param command a command instance to be added
	 */
	public void addCommand(ICommand command) {
		_commands.add(command);
		buildCommandChain(command);
	}

	/**
	 * Adds a list of commands to the command set
	 * @param commands a list of commands to be added
	 */
	public void addCommands(List<ICommand> commands) {
		for (ICommand command : commands) {
			addCommand(command);
		}
	}

	/**
	 * Adds an event to the command set.
	 * @param event an event instance to be added
	 */
    public void addEvent(IEvent event) {
        _events.add(event);
        _eventsByName.put(event.getName(), event);
    }

    /**
     * Adds a list of event to the command set
     * @param events a list of events to be added
     */
    public void addEvents(List<IEvent> events) {
        for (IEvent event : events) {
            addEvent(event);
        }
    }

    /**
	 * Adds commands from another command set to this one
	 * @param commands a commands set to add commands from
	 */
	public void addCommandSet(CommandSet commands) {
		for (ICommand command : commands.getCommands()) {
			addCommand(command);
		}
	}
	
	/**
	 * Adds intercepter to the command set.
	 * @param intercepter an intercepter instance to be added.
	 */
	public void addIntercepter(ICommandIntercepter intercepter) {
		_intercepters.add(intercepter);
		rebuildAllCommandChains();
	}
	
	/**
	 * Execute command by its name with specified arguments.
	 * @param command the command name.
	 * @param correlationId a unique correlation/transaction id
	 * @param args a list of command arguments.
	 * @return the execution result.
	 * @throws MicroserviceError when execution fails for any reason.
	 */
	public Object execute(String command, String correlationId, DynamicMap args) throws MicroserviceError {
		// Get command and throw error if it doesn't exist
		ICommand cref = findCommand(command);
		if (cref == null)
			throw new BadRequestError("NoCommand", "Requested command does not exist")
				.withDetails(command);

		// Generate correlationId if it doesn't exist
		// Use short ids for now
		if (correlationId == null)
			correlationId = IdGenerator.getShort();
		
		// Validate command arguments before execution and throw the 1st found error
		List<MicroserviceError> errors = cref.validate(args);
		if (errors.size() > 0)
			throw errors.get(0);
				
		// Execute the command.
		return cref.execute(correlationId, args);
	}
	
	/**
	 * Validates command arguments.
	 * @param command the command name.
	 * @param args a list of command arguments.
	 * @return a list of validation errors or empty list when arguments are valid.
	 */
	public List<MicroserviceError> validate(String command, DynamicMap args) {
		ICommand cref = findCommand(command);
		if (cref == null) {
			List<MicroserviceError> errors = new ArrayList<MicroserviceError>();
			errors.add(new BadRequestError("NoCommand", "Requested command does not exist")
				.withDetails(command)
			);
			return errors;
		}
		return cref.validate(args);
	}

	/**
	 * Adds listener to all events.
	 * @param listener a listener to be added
	 */
    public void addListener(IEventListener listener) {
        for (IEvent event : _events) {
            event.addListener(listener);
        }
    }

    /**
     * Remove listener to all events.
     * @param listener a listener to be removed
     */
    public void removeListener(IEventListener listener) {
        for (IEvent event : _events) {
            event.removeListener(listener);
        }
    }

    /**
     * Notifies all listeners about the event.
     * @param event an event name
     * @param correlationId a unique correlation/transaction id
     * @param value an event value
     */
    public void notify(String event, String correlationId, DynamicMap value) {
        IEvent e = findEvent(event);
        if (e != null) {
            e.notify(correlationId, value);
        }
    }
	
}
