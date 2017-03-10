package org.pipservices.runtime.persistence;

import org.pipservices.runtime.config.*;
import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;

public class DummyFilePersistence extends FilePersistence<Dummy> implements IDummyPersistence {
	/**
	 * Unique descriptor for the DummyFilePersistence component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Persistence, "pip-services-dummies", "file", "*"
	);

	public DummyFilePersistence() {
        super(Descriptor, Dummy.class);
    }

	protected DummyFilePersistence(ComponentDescriptor descriptor) {
		super(descriptor, Dummy.class);
	}

	@Override
    public DataPage<Dummy> getDummies(String correlationId, FilterParams filter, PagingParams paging) 
		throws MicroserviceError {
		
        filter = filter != null ? filter : new FilterParams();
        String key = filter.getNullableString("key");

        return getPage(
        	correlationId, 
    		(v) -> {
                if (key != null && v.getKey() != key)
                    return false;
                return true;
            },
            paging, null
        );
    }

	@Override
    public Dummy getDummyById(String correlationId, String dummyId) throws MicroserviceError {
        return getById(correlationId, dummyId);
    }

	@Override
    public Dummy createDummy(String correlationId, Dummy dummy) throws MicroserviceError {
        return create(correlationId, dummy);
    }

	@Override
    public Dummy updateDummy(String correlationId, String dummyId, Object dummy) throws MicroserviceError {
        return update(correlationId, dummyId, dummy);
    }
	
	@Override
    public void deleteDummy(String correlationId, String dummyId) throws MicroserviceError {
        delete(correlationId, dummyId);
    }

}
