package org.pipservices.runtime.commands;

import org.pipservices.runtime.portability.DynamicMap;

/**
 * Listener for command events
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public interface IEventListener {
	/**
	 * Notifies that event occured.
	 * @param e event reference
	 * @param correlationId a unique correlation/transaction id
	 * @param value event arguments
	 */
    void onEvent(IEvent e, String correlationId, DynamicMap value);
}