package org.pipservices.runtime.logic;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.DynamicMap;

/**
 * Abstract implementation for business logic controller.
 * 
 * @author Sergey Seroukhov
 * @version 1.1
 * @since 2016-06-09
 */
public abstract class AbstractController extends AbstractBusinessLogic implements IController {	
	/**
	 * Creates instance of abstract controller
	 * @param descriptor the unique descriptor that is used to identify and locate the component.
	 */
	protected AbstractController(ComponentDescriptor descriptor) {
		super(descriptor);
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
		super.link(context, components);
		
		// Commented until we decide to use command pattern as everywhere
		// Until now the main method is to implement specific methods with instrumentation
		//addIntercepter(new TracingIntercepter(_loggers, "Executing"));
		//addIntercepter(new TimingIntercepter(_counters, "exec_time"));
	}

	/**
	 * Does instrumentation of performed business method by counting elapsed time.
	 * @param correlationId the unique id to identify distributed transaction
	 * @param name the name of called business method
	 * @return ITiming instance to be called at the end of execution of the method.
	 */
	protected ITiming instrument(String correlationId, String name) {
		trace(null, "Executing " + name + " method");
		return beginTiming(name + ".exec_time");
	}

}
