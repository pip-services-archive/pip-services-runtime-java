package org.pipservices.runtime.services;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;

/**
 * Abstract implementation for all API service components
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public abstract class AbstractService extends AbstractComponent implements IService {
	/**
	 * Creates and initializes instance of the APIs service
	 * @param descriptor the unique descriptor that is used to identify and locate the component.
	 */
	protected AbstractService(ComponentDescriptor descriptor) {
		super(descriptor);
	}
}
