package org.pipservices.runtime.build;

import org.pipservices.runtime.config.*;

/**
 * Holds registration of specific component in component factory.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
class FactoryRegistration {
	private ComponentDescriptor _descriptor;
	private Class<?> _classFactory;
	
	/**
	 * Creates and fills registration instance.
	 * @param descriptor a component descriptor to locate the registration
	 * @param classFactory a component class factory to instantiate a component
	 */
	public FactoryRegistration(ComponentDescriptor descriptor, Class<?> classFactory) {
		_descriptor = descriptor;
		_classFactory = classFactory;
	}

	/**
	 * Gets a component descriptor for matching
	 * @return a component descriptor
	 */
	public ComponentDescriptor getDescriptor() {
		return _descriptor;
	}
	
	/**
	 * Get a component class factory to create a component instance
	 * @return a component class
	 */
	public Class<?> getClassFactory() {
		return _classFactory;
	}
}
