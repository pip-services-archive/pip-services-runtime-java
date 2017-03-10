package org.pipservices.runtime.discovery;

import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;

/**
 * Abstract implementation for all discovery components.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public abstract class AbstractDiscovery extends AbstractComponent implements IDiscovery {
	/**
	 * Creates and initializes instance of discovery component
	 * @param descriptor the unique component descriptor
	 */
	protected AbstractDiscovery(ComponentDescriptor descriptor) {
		super(descriptor);
	}
	
	/**
	 * Register in discovery service endpoint where API service binds to.
	 * The endpoint shall contain discovery name to locate the registration.
	 * If it's not defined, the registration doesn't do anything.
	 * @param endpoint the endpoint to be registered.
	 * @throws MicroserviceError when registration fails for whatever reasons
	 */
	public abstract void register(Endpoint endpoint) throws MicroserviceError;
	
	/**
	 * Resolves one endpoint from the list of service endpoints
	 * to be called by a client.
	 * @param endpoints a list of endpoints to be resolved from. 
	 * The list must contain at least one endpoint with discovery name. 
	 * @return a resolved endpoint.
	 * @throws MicroserviceError when resolution failed for whatever reasons.
	 */
	public abstract Endpoint resolve(List<Endpoint> endpoints) throws MicroserviceError;

	/**
	 * Resolves a list of endpoints from to be called by a client.
	 * @param endpoints a list of endpoints to be resolved from. 
	 * The list must contain at least one endpoint with discovery name. 
	 * @return a list with resolved endpoints.
	 * @throws MicroserviceError when resolution failed for whatever reasons.
	 */
	public abstract List<Endpoint> resolveAll(List<Endpoint> endpoints) throws MicroserviceError;
}
