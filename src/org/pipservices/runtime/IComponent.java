package org.pipservices.runtime;

import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.DynamicMap;

/**
 * The most basic interface that identifies microservice component
 * and it's behavior. It exposes unique component descriptor for 
 * identification and allows to manage the component lifecycle to
 * transition between several states:
 * <ul>
 * <li> Create - creates a new component instance
 * <li> Configure - sets component configuration parameters
 * <li> Link - sets references to other microservice components
 * <li> Open - performs initialization, opens connections and makes the component ready
 * <li> Close - closes connections, deinitializes component. 
 * </ul>
 * 
 * @author Seroukhov Seroukhov
 * @version 1.1
 * @since 2016-05-01
 */
public interface IComponent {
	/**
	 * Gets the unique component descriptor that can identify
	 * and locate the component inside the microservice.
	 * @return the unique component descriptor.
	 */
	ComponentDescriptor getDescriptor();

	/**
	 * Gets the current state of the component.
	 * @return the current component state.
	 */
	int getState();	
	
	/**
	 * Sets component configuration parameters and switches from component
	 * to 'Configured' state. The configuration is only allowed once
	 * right after creation. Attempts to perform reconfiguration will 
	 * cause an exception.
	 * @param config the component configuration parameters.
	 * @throws MicroserviceError when component is in illegal state 
	 * or configuration validation fails. 
	 */
	void configure(ComponentConfig config) throws MicroserviceError;
	
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
	void link(DynamicMap context, ComponentSet components) throws MicroserviceError;
	
	/**
	 * Opens the component, performs initialization, opens connections
	 * to external services and makes the component ready for operations.
	 * Opening can be done multiple times: right after linking 
	 * or reopening after closure.  
	 * @throws MicroserviceError when initialization or connections fail.
	 */
	void open() throws MicroserviceError;
	
	/**
	 * Closes the component and all open connections, performs deinitialization
	 * steps. Closure can only be done from opened state. Attempts to close
	 * already closed component or in wrong order will cause exception.
	 * @throws MicroserviceError when closure fails.
	 */
	void close() throws MicroserviceError;
}
