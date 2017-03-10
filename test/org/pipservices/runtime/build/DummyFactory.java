package org.pipservices.runtime.build;

import org.pipservices.runtime.clients.*;
import org.pipservices.runtime.logic.*;
import org.pipservices.runtime.persistence.*;
import org.pipservices.runtime.services.*;

public class DummyFactory extends ComponentFactory {
	public final static DummyFactory Instance = new DummyFactory();
	
	public DummyFactory() {
		super(DefaultFactory.Instance);
		
		register(DummyFilePersistence.Descriptor, DummyFilePersistence.class);
		register(DummyMemoryPersistence.Descriptor, DummyMemoryPersistence.class);
		register(DummyController.Descriptor, DummyController.class);
		register(DummyRestClient.Descriptor, DummyRestClient.class);
		register(DummyRestService.Descriptor, DummyRestService.class);
	}
	
}
