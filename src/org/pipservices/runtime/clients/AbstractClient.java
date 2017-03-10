package org.pipservices.runtime.clients;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;

/**
 * Abstract implementation for all microservice client components.
 * 
 * @author Sergey Seroukhov
 * @version 1.1
 * @since 2016-06-09
 */
public abstract class AbstractClient extends AbstractComponent implements IClient {
	/**
	 * Creates and initializes instance of the microservice client component.
	 * @param descriptor the unique descriptor that is used to identify and locate the component.
	 */
	protected AbstractClient(ComponentDescriptor descriptor) {
		super(descriptor);
	}

	/**
	 * Does instrumentation of performed business method by counting elapsed time.
	 * @param correlationId the unique id to identify distributed transaction
	 * @param name the name of called business method
	 * @return ITiming instance to be called at the end of execution of the method.
	 */
	protected ITiming instrument(String correlationId, String name) {
		trace(null, "Calling " + name + " method");
		return beginTiming(name + ".call_time");
	}
}
