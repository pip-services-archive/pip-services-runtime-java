package org.pipservices.runtime.logic;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;

/**
 * Abstract implementation of business logic decorators.
 * Decorators are typically used to alter standard behavior
 * of microservice business logic by injecting custom logic
 * before or after execution.
 * 
 * @author Sergey Seroukhov
 * @version 1.1
 * @since 2016-065-09
 */
public abstract class AbstractDecorator extends AbstractBusinessLogic implements IDecorator {
	/**
	 * Creates instance of abstract business logic decorator
	 * @param descriptor the unique descriptor that is used to identify and locate the component.
	 */
	protected AbstractDecorator(ComponentDescriptor descriptor) {
		super(descriptor);
	}
}
