package org.pipservices.runtime.commands;

import java.util.List;

import org.pipservices.runtime.portability.DynamicMap;

/**
 * Interface for command events.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public interface IEvent {
	/**
	 * Gets the event name
	 */
    String getName();

    /**
     * Get listeners that receive notifications for that event
     */
    List<IEventListener> getListeners();

    /**
     * Adds listener to receive notifications
     * @param listener a listener reference to be added
     */
    void addListener(IEventListener listener);

    /**
     * Removed listener for event notifications.
     * @param listener a listener reference to be removed
     */
    void removeListener(IEventListener listener);

    /**
     * Notifies all listeners about the event.
     * @param correlationId a unique correlation/transaction id
     * @param args event value
     */
    void notify(String correlationId, DynamicMap args);
}