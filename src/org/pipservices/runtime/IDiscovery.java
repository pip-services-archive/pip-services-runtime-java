package org.pipservices.runtime;

import java.util.*;

import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;

/**
 * Service discovery component used to register addresses of the microservice
 * service endpoints or to resolve addresses of external services called by clients.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-09-16
 */
public interface IDiscovery extends IComponent {
	/**
	 * Register in discovery service endpoint where API service binds to.
	 * The endpoint shall contain discovery name to locate the registration.
	 * If it's not defined, the registration doesn't do anything.
	 * @param endpoint the endpoint to be registered.
	 * @throws MicroserviceError when registration fails for whatever reasons
	 */
	void register(Endpoint endpoint) throws MicroserviceError;
	
	/**
	 * Resolves one endpoint from the list of service endpoints
	 * to be called by a client.
	 * @param endpoints a list of endpoints to be resolved from. 
	 * The list must contain at least one endpoint with discovery name. 
	 * @return a resolved endpoint.
	 * @throws MicroserviceError when resolution failed for whatever reasons.
	 */
	Endpoint resolve(List<Endpoint> endpoints) throws MicroserviceError;

	/**
	 * Resolves a list of endpoints from to be called by a client.
	 * @param endpoints a list of endpoints to be resolved from. 
	 * The list must contain at least one endpoint with discovery name. 
	 * @return a list with resolved endpoints.
	 * @throws MicroserviceError when resolution failed for whatever reasons.
	 */
	List<Endpoint> resolveAll(List<Endpoint> endpoints) throws MicroserviceError;
}
