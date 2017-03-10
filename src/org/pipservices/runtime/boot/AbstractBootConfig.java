package org.pipservices.runtime.boot;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;

/**
 * Abstract implementation for all configuration reader components.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public abstract class AbstractBootConfig extends AbstractComponent implements IBootConfig {
	/**
	 * Creates instance of abstract configuration reader component.
	 * @param descriptor the unique descriptor that is used to identify and locate the component.
	 */
	protected AbstractBootConfig(ComponentDescriptor descriptor) {
		super(descriptor);
	}
	
	/**
	 * Reads microservice configuration from the source
	 * @return a microservice configuration
	 * @throws MicroserviceError when reading fails for any reason
	 */
	@Override
	public abstract MicroserviceConfig readConfig() throws MicroserviceError;
}
