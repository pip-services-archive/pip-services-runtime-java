package org.pipservices.runtime.clients;

import javax.ws.rs.core.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;

import com.sun.jersey.api.client.*;

public class DummyRestClient extends RestClient implements IDummyClient {
	/**
	 * Unique descriptor for the DummyRestClient component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Clients, "pip-services-dummies", "rest", "1.0"
	);

	public DummyRestClient() {
		super(Descriptor, "dummies");
	}

	@Override
    public DataPage<Dummy> getDummies(String correlationId, FilterParams filter, PagingParams paging) throws MicroserviceError {
        checkCurrentState(State.Opened);

        ITiming timing = instrument(correlationId, "dummy.get_dummies");
        try {
        	MultivaluedMap<String, String> params = createQueryParams();
        	addCorrelationId(params, correlationId);
        	addFilterParams(params, filter);
        	addPagingParams(params, paging);
        	
	       return _resource
			.queryParams(params)
	       	.type(MediaType.APPLICATION_JSON)
		    .get(new GenericType<DataPage<Dummy>>() {});
        } finally {
        	timing.endTiming();
        }
    }
    
	@Override
    public Dummy getDummyById(String correlationId, String dummyId) throws MicroserviceError {
        checkCurrentState(State.Opened);

        ITiming timing = instrument(correlationId, "dummy.get_dummy_by_id");
        try {
        	MultivaluedMap<String, String> params = createQueryParams();
        	addCorrelationId(params, correlationId);

        	return _resource.path(dummyId)
    		   .queryParams(params)
         	   .type(MediaType.APPLICATION_JSON)
    		   .get(Dummy.class);
        } finally {
        	timing.endTiming();
        }
    }
    
	@Override
    public Dummy createDummy(String correlationId, Dummy dummy) throws MicroserviceError {
        checkCurrentState(State.Opened);

        ITiming timing = instrument(correlationId, "dummy.create_dummy");
        try {
        	MultivaluedMap<String, String> params = createQueryParams();
        	addCorrelationId(params, correlationId);

        	return _resource        	
     		   .queryParams(params)
        	   .type(MediaType.APPLICATION_JSON)
    		   .post(Dummy.class, dummy);
        } finally {
        	timing.endTiming();
        }
	}
	
	@Override
    public Dummy updateDummy(String correlationId, String dummyId, Object dummy) throws MicroserviceError {
        checkCurrentState(State.Opened);

        ITiming timing = instrument(correlationId, "dummy.update_dummy");
        try {
        	MultivaluedMap<String, String> params = createQueryParams();
        	addCorrelationId(params, correlationId);

        	return _resource.path(dummyId)
     		   .queryParams(params)
        	   .type(MediaType.APPLICATION_JSON)
    		   .put(Dummy.class, dummy);
        } finally {
        	timing.endTiming();
        }
    }

	@Override
    public void deleteDummy(String correlationId, String dummyId) throws MicroserviceError {
        checkCurrentState(State.Opened);

        ITiming timing = instrument(correlationId, "dummy.delete_dummy");
        try {
        	MultivaluedMap<String, String> params = createQueryParams();
        	addCorrelationId(params, correlationId);

        	_resource.path(dummyId)
	 		   .queryParams(params)
	    	   .type(MediaType.APPLICATION_JSON)
    		   .delete();
        } finally {
        	timing.endTiming();
        }
   }
	
}
