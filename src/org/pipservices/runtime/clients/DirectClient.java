package org.pipservices.runtime.clients;

import org.pipservices.runtime.config.*;

/**
 * Direct client implementation that allows to call another microservice from the same container.
 * 
 * It can be very useful for deployments of microservices as monolithic systems.
 * Although it may seem strange some situation may require deployment simplicity 
 * over scalability and other benefits of microservices. The good news, you have flexibility to 
 * adapt the end product without sacrificing the system architecture.  
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class DirectClient extends AbstractClient {
	/**
	 * Creates and initializes instance of the microservice client component.
	 * @param descriptor the unique descriptor that is used to identify and locate the component.
	 */
	protected DirectClient(ComponentDescriptor descriptor) {
		super(descriptor);
	}	
}
