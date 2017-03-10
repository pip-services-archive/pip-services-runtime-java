package org.pipservices.runtime.persistence;

import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;

public class DummyMemoryPersistence extends DummyFilePersistence implements IDummyPersistence {
	/**
	 * Unique descriptor for the DummyMemoryPersistence component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Persistence, "pip-services-dummies", "memory", "*"
	);

	public DummyMemoryPersistence() throws MicroserviceError {
		super(Descriptor);
    }

    @Override
    public void configure(ComponentConfig config) throws MicroserviceError {
        super.configure(config.withDefaultTuples("options.path", ""));
    }

    @Override
    public void save() throws MicroserviceError {}
}
