package org.pipservices.runtime.persistence;

import org.pipservices.runtime.config.*;
import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;

public abstract class MemoryPersistence<T extends IIdentifiable> extends FilePersistence<T> {
	
    protected MemoryPersistence(ComponentDescriptor descriptor, Class<?> itemType) {
        super(descriptor, itemType);      
    }

	/**
	 * Sets component configuration parameters and switches from component
	 * to 'Configured' state. The configuration is only allowed once
	 * right after creation. Attempts to perform reconfiguration will 
	 * cause an exception.
	 * @param config the component configuration parameters.
	 * @throws MicroserviceError when component is in illegal state 
	 * or configuration validation fails. 
	 */
	@Override
    public void configure(ComponentConfig config) throws MicroserviceError {
        super.configure(config.withDefaultTuples("options.path", ""));
    }

    @Override
    public void save() throws MicroserviceError {}
}
