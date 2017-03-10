package org.pipservices.runtime.clients;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

public class DummyDirectClient extends DirectClient implements IDummyClient {
	/**
	 * Unique descriptor for the DummyDirectClient component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Clients, "pip-services-dummies", "direct", "1.0"
	);

	private IBusinessLogic _controller;
	
	public DummyDirectClient() {
		super(Descriptor);
	}

	@Override
	public void link(DynamicMap context, ComponentSet components) throws MicroserviceError {
		super.link(context, components);
		
		_controller = (IBusinessLogic)components.getOneRequired(
			new ComponentDescriptor(Category.BusinessLogic, "pip-services-dummies", "*", "*")
		);
	}
	
	@SuppressWarnings("unchecked")
	@Override
    public DataPage<Dummy> getDummies(String correlationId, FilterParams filter, PagingParams paging) throws MicroserviceError {
		return (DataPage<Dummy>)_controller.execute(
			"get_dummies",
			correlationId,
			DynamicMap.fromTuples(
				"filter", filter,
				"paging", paging
			)
		);
    }
    
	@Override
    public Dummy getDummyById(String correlationId, String dummyId) throws MicroserviceError {
		return (Dummy)_controller.execute(
			"get_dummy_by_id",
			correlationId,
			DynamicMap.fromTuples(
				"dummy_id", dummyId
			)
		);
    }
    
	@Override
    public Dummy createDummy(String correlationId, Dummy dummy) throws MicroserviceError {
		return (Dummy)_controller.execute(
			"create_dummy",
			correlationId,
			DynamicMap.fromTuples(
				"dummy", dummy
			)
		);
	}
	
	@Override
    public Dummy updateDummy(String correlationId, String dummyId, Object dummy) throws MicroserviceError {
		return (Dummy)_controller.execute(
			"update_dummy",
			correlationId,
			DynamicMap.fromTuples(
				"dummy_id", dummyId,
				"dummy", dummy
			)
		);
    }

	@Override
    public void deleteDummy(String correlationId, String dummyId) throws MicroserviceError {
		_controller.execute(
			"delete_dummy",
			correlationId,
			DynamicMap.fromTuples(
				"dummy_id", dummyId
			)
		);
   }
	
}
