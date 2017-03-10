package org.pipservices.runtime.commands;

import java.util.ArrayList;
import java.util.List;

import org.pipservices.runtime.AbstractComponent;
import org.pipservices.runtime.IComponent;
import org.pipservices.runtime.portability.DynamicMap;
import org.pipservices.runtime.errors.UnknownError;

/**
 * Events to receive notifications on command execution results and failures.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public class Event implements IEvent {
    private IComponent _component;
    private String _name;
    private List<IEventListener> _listeners = new ArrayList<IEventListener>();

    /**
     * Creates and initializes instance of the event.
     * @param component component references
     * @param name name of the event
     */
    public Event(IComponent component, String name) {
        if (name == null)
            throw new NullPointerException("Event name is not set");

        _component = component;
        _name = name;
    }

    /**
     * Gets the event name
     */
    public String getName() {
    	return _name;    	
    }

    /**
     * Gets listeners that receive notifications for that event
     */
    public List<IEventListener> getListeners() {
    	return _listeners;
    }

    /**
     * Adds listener to receive notifications
     * @param listener a listener reference to be added
     */
    public void addListener(IEventListener listener) {
        _listeners.add(listener);
    }

    /**
     * Remove listener for event notifications
     * @param listener a listener to be removed
     */
    public void removeListener(IEventListener listener) {
        _listeners.remove(listener);
    }

    /**
     * Notifies all listeners about the event.
     * @param correlationId a unique correlation/transaction id
     * @param value an event value
     */
    public void notify(String correlationId, DynamicMap value) {
        for (IEventListener listener : _listeners) {
            try {
                listener.onEvent(this, correlationId, value);
            } catch (Exception ex) {
                // Wrap the error
            	UnknownError error = (UnknownError) new UnknownError(                		
                    _component,
                    "EventFailed",
                    "Rasing event " + _name + " failed: " + ex
                )
    			.withDetails(_name)
                .withCorrelationId(correlationId)
                .wrap(ex);

                // Output the error
                if (_component instanceof AbstractComponent) {
                	AbstractComponent component = (AbstractComponent)_component;
                    component.error(correlationId, error);
                }
            }
        }
    }
}
