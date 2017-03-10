package org.pipservices.runtime;

import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;

/**
 * Interface for microservice component that is responsible for
 * reading bootstrap microservice configuration from a configuration repository.
 * 
 * It is still not clear if that logic shall be in component or
 * separate BootstrapConfig classes.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public interface IBootConfig extends IComponent {
	/**
	 * Reads microservice configuration from the source
	 * @return a microservice configuration
	 * @throws MicroserviceError when reading fails for any reason
	 */
	MicroserviceConfig readConfig() throws MicroserviceError;
}
