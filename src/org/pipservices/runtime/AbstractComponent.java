package org.pipservices.runtime;

import java.time.*;
import java.util.*;

import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.DynamicMap;

/**
 * Abstract implementation for all microservice components.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public abstract class AbstractComponent implements IComponent {
	protected ComponentDescriptor _descriptor;
	protected int _state;
	protected ComponentConfig _config;
	protected IDiscovery _discovery;
	protected List<ILogger> _loggers = new ArrayList<ILogger>();
	protected ICounters _counters;
	
	/**
	 * Creates and initializes the component instance
	 * @param descriptor the unique descriptor that is used to identify and locate the component.
	 */
	protected AbstractComponent(ComponentDescriptor descriptor) {		
		if (descriptor == null)
			throw new NullPointerException("Descriptor is not set");
		
		_descriptor = descriptor;
		_state = State.Initial;
	}
	
	/**
	 * Gets the unique component descriptor that can identify
	 * and locate the component inside the microservice.
	 * @return the unique component descriptor.
	 */
	public ComponentDescriptor getDescriptor() { 
		return _descriptor; 
	}
		
	/* Life cycle management */
	
	/**
	 * Gets the current state of the component.
	 * @return the current component state.
	 */
	public int getState() { 
		return _state; 
	}
	
	/**
	 * Checks if specified state matches to the current one.
	 * @param state the expected state
	 * @throws MicroserviceError when current and expected states don't match
	 */
	protected void checkCurrentState(int state) throws MicroserviceError {
		if (_state != state)
			throw new StateError(this, "InvalidState", "Component is in wrong state")
				.withDetails(_state, state);
	}

	/**
	 * Checks if transition to the specified state is allowed from the current one.
	 * @param newState the new state to make transition
	 * @throws MicroserviceError when transition is not allowed.
	 */
	protected void checkNewStateAllowed(int newState) throws MicroserviceError {
		if (newState == State.Configured && _state != State.Initial)
			throw new StateError(this, "InvalidState", "Component cannot be configured")
				.withDetails(_state, State.Configured);

		if (newState == State.Linked && _state != State.Configured)
			throw new StateError(this, "InvalidState", "Component cannot be linked")
				.withDetails(_state, State.Linked);

		if (newState == State.Opened && _state != State.Linked && _state != State.Closed)
			throw new StateError(this, "InvalidState", "Component cannot be opened")
				.withDetails(_state, State.Opened);

		if (newState == State.Closed && _state != State.Opened)
			throw new StateError(this,"InvalidState", "Component cannot be closed")
				.withDetails(_state, State.Closed);
	}
	
	/**
	 * Sets component configuration parameters and switches from component
	 * to 'Configured' state. The configuration is only allowed once
	 * right after creation. Attempts to perform reconfiguration will 
	 * cause an exception.
	 * @param config the component configuration parameters.
	 * @throws MicroserviceError when component is in illegal state 
	 * or configuration validation fails. 
	 */
	@Override
	public void configure(ComponentConfig config) throws MicroserviceError {
		checkNewStateAllowed(State.Configured);
		_config = config;
		_state = State.Configured; 
	}
	
	/**
	 * Sets references to other microservice components to enable their 
	 * collaboration. It is recommended to locate necessary components
	 * and cache their references to void performance hit during
	 * normal operations. 
	 * Linking can only be performed once after configuration 
	 * and will cause an exception when it is called second time 
	 * or out of order. 
	 * @param context application context
	 * @param components references to microservice components.
	 * @throws MicroserviceError when requires components are not found.
	 */
	@Override
	public void link(DynamicMap context, ComponentSet components) throws MicroserviceError {
		checkNewStateAllowed(State.Linked);
		
		// Get reference to discovery component
		_discovery = (IDiscovery)components.getOneOptional(
			new ComponentDescriptor(Category.Discovery, null, null, null)
		);
		
		// Get reference to loggers
		List<IComponent> loggers = components.getAllOptional(
			new ComponentDescriptor(Category.Logs, null, null, null)	
		);
		_loggers.clear();
		for (IComponent logger : loggers) {
			_loggers.add((ILogger)logger);
		}
		
		// Get reference to counters component
		_counters = (ICounters)components.getOneOptional(
			new ComponentDescriptor(Category.Counters, null, null, null)
		);

		_state = State.Linked;
	}
	
	/**
	 * Opens the component, performs initialization, opens connections
	 * to external services and makes the component ready for operations.
	 * Opening can be done multiple times: right after linking 
	 * or reopening after closure.  
	 * @throws MicroserviceError when initialization or connections fail.
	 */
	@Override
	public void open() throws MicroserviceError {
		checkNewStateAllowed(State.Opened);
		_state = State.Opened;
		trace(null, "Component " + _descriptor.toString() + " opened");
	}
	
	/**
	 * Closes the component and all open connections, performs deinitialization
	 * steps. Closure can only be done from opened state. Attempts to close
	 * already closed component or in wrong order will cause exception.
	 * @throws MicroserviceError with closure fails.
	 */
	@Override
	public void close() throws MicroserviceError {
		checkNewStateAllowed(State.Closed);
		_state = State.Closed;
		trace(null, "Component " + _descriptor.toString() + " closed");
	}
		
	/* Logging */

	/**
	 * Writes message to log
	 * @param level a message logging level
	 * @param correlationId a unique id to identify distributed transaction
	 * @param message a message objects
	 */
	protected void writeLog(int level, String correlationId, Object[] message) {
		if (_loggers == null || _loggers.size() == 0) 
			return;

		String component = this._descriptor.toString();
    	for (ILogger logger : _loggers) {
            logger.log(level, component, correlationId, message);
        }
	}

	/**
	 * Logs fatal error that causes microservice to shutdown
	 * @param correlationId a unique id to identify distributed transaction
	 * @param message a list with message values
	 */
	public void fatal(String correlationId, Object... message) {
		writeLog(LogLevel.Fatal, correlationId, message);
	}

    /**
     * Logs recoverable error
	 * @param correlationId a unique id to identify distributed transaction
     * @param message a list with message values
     */
	public void error(String correlationId, Object... message) {
		writeLog(LogLevel.Error, correlationId, message);
	}

    /**
     * Logs warning messages
	 * @param correlationId a unique id to identify distributed transaction
     * @param message a list with message values
     */
	public void warn(String correlationId, Object... message) {
		writeLog(LogLevel.Warn, correlationId, message);
	}

    /**
     * Logs important information message
	 * @param correlationId a unique id to identify distributed transaction
     * @param message a list with message values
     */
	public void info(String correlationId, Object... message) {
		writeLog(LogLevel.Info, correlationId, message);
	}

    /**
     * Logs high-level debugging messages
	 * @param correlationId a unique id to identify distributed transaction
     * @param message a list with message values
     */
	public void debug(String correlationId, Object... message) {
		writeLog(LogLevel.Debug, correlationId, message);
	}

    /**
     * Logs fine-granular debugging message
	 * @param correlationId a unique id to identify distributed transaction
     * @param message a list with message values
     */
	public void trace(String correlationId, Object... message) {
		writeLog(LogLevel.Trace, correlationId, message);
	}
	
	/* Performance counters */
	
	/**
	 * Starts measurement of execution time interval.
	 * The method returns ITiming object that provides endTiming()
	 * method that shall be called when execution is completed
	 * to calculate elapsed time and update the counter.
	 * @param name the name of interval counter.
	 * @return callback interface with endTiming() method 
	 * that shall be called at the end of execution.
	 */
	public ITiming beginTiming(String name) {
		if (_counters != null)
			return _counters.beginTiming(name);
		else
			return new ITiming() { 
				public void endTiming() {} 
			};
	}
	
	/**
	 * Calculates rolling statistics: minimum, maximum, average
	 * and updates Statistics counter.
	 * This counter can be used to measure various non-functional
	 * characteristics, such as amount stored or transmitted data,
	 * customer feedback, etc. 
	 * @param name the name of statistics counter.
	 * @param value the value to add to statistics calculations.
	 */
	public void stats(String name, float value) {
		if (_counters != null) _counters.stats(name, value);
	}
	
	/**
	 * Records the last reported value. 
	 * This counter can be used to store performance values reported
	 * by clients or current numeric characteristics such as number
	 * of values stored in cache.
	 * @param name the name of last value counter
	 * @param value the value to be stored as the last one
	 */
	public void last(String name, float value) {
		if (_counters != null) _counters.last(name, value);
	}
	
	/**
	 * Records the current time.
	 * This counter can be used to track timing of key
	 * business transactions.
	 * @param name the name of timing counter
	 */
	public void timestampNow(String name) {
		timestamp(name, ZonedDateTime.now(ZoneId.of("Z")));
	}
	
	/**
	 * Records specified time.
	 * This counter can be used to tack timing of key
	 * business transactions as reported by clients.
	 * @param name the name of timing counter.
	 * @param value the reported timing to be recorded.
	 */
	public void timestamp(String name, ZonedDateTime value) {
		if (_counters != null) _counters.timestamp(name, value);
	}
	
	/**
	 * Increments counter by value of 1.
	 * This counter is often used to calculate
	 * number of client calls or performed transactions.
	 * @param name the name of counter counter.
	 */
	public void incrementOne(String name) {
		increment(name, 1);
	}
	
	/**
	 * Increments counter by specified value.
	 * This counter can be used to track various
	 * numeric characteristics
	 * @param name the name of the increment value.
	 * @param value number to increase the counter.
	 */
	public void increment(String name, int value) {
		if (_counters != null) _counters.increment(name, value);
	}

 	/**** Utility Methods ******/

	/**
	 * Generates a string representation for this component
	 * @return a component descriptor in string format
	 */
	public String toString() {
		return this._descriptor.toString();
	}    
}
