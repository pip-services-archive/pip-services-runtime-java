package org.pipservices.runtime;

import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;

/**
 * Factory for microservice components. It registers component classes,
 * locates classes by descriptors and creates component instances.
 * It also supports inheritance from other factories.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public interface IComponentFactory {
	/**
	 * Extends this factory with base factories.
	 * @param baseFactories a list of base factories to extend registrations of this factory.
	 */
	void extend(IComponentFactory... baseFactories);

	/**
	 * Registers a component class accompanies by component descriptor.
	 * @param descriptor a component descriptor to locate the class.
	 * @param classFactory a component class used to create a component.
	 * @return a reference to this factory to support chaining registrations.
	 */
	IComponentFactory register(ComponentDescriptor descriptor, Class<?> classFactory);
			
	/**
	 * Lookups for component class by matching component descriptor.
	 * @param descriptor a component descriptor used to locate a class
	 * @return a located component class.
	 * @throws MicroserviceError when component class was not found.
	 */
	Class<?> find(ComponentDescriptor descriptor) throws MicroserviceError;
	
	/**
	 * Create a component instance using class located by component descriptor.
	 * @param descriptor a component descriptor to locate a component class.
	 * @return a created component instance.
	 * @throws MicroserviceError when class to construct component wasn't found, 
	 * when constructor failed or component doesn't implements required interfaces.
	 */
	IComponent create(ComponentDescriptor descriptor) throws MicroserviceError;
}
