package org.pipservices.runtime.services;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.logic.*;
import org.pipservices.runtime.portability.DynamicMap;

@Path("/dummies")
public class DummyRestService extends RestService {
	/**
	 * Unique descriptor for the DummyRestService component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Services, "pip-services-dummies", "rest", "1.0"
	);

	private IDummyBusinessLogic _logic;
	
	public DummyRestService() {
		super(Descriptor);
	}

	@Override
	public void link(DynamicMap context, ComponentSet components) throws MicroserviceError {
		_logic = (IDummyBusinessLogic)components.getOnePrior(
			this, new ComponentDescriptor(Category.BusinessLogic, "pip-services-dummies", "*", "*")
		);

		super.link(context, components);		
	}
	
	@GET
//  @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public DataPage<Dummy> getDummies(
    	@QueryParam("correlation_id") String correlationId,
		@QueryParam("key") String key, @QueryParam("skip") String skip,
		@QueryParam("take") String take, @QueryParam("total") String total
	) throws MicroserviceError {    	
    	FilterParams filter = FilterParams.fromTuples(
			"key", key
		);    	
    	PagingParams paging = new PagingParams(skip, take, total);
    	
        return _logic.getDummies(correlationId, filter, paging);
    }

	@GET
	@Path("{dummyId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDummyById(
    	@QueryParam("correlation_id") String correlationId,
		@PathParam("dummyId") String dummyId
	) throws MicroserviceError{		        
		Dummy result = _logic.getDummyById(correlationId, dummyId);
		
        // This is a hack. When result is null client just hangs
        // Todo: find a fix for this issue later
        if (result != null)
        	return Response.ok(result).build();
        else return Response.ok("null").build();
        // All those do not work
        //else return Response.noContent().build();
        //else return Response.ok("").build();
    }

	@POST
//	@Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Dummy createDummy(
    	@QueryParam("correlation_id") String correlationId,
		Dummy dummy
	) throws MicroserviceError {
        return _logic.createDummy(correlationId, dummy);
    }

	@PUT
	@Path("{dummyId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Dummy updateDummy(
    	@QueryParam("correlation_id") String correlationId,
		@PathParam("dummyId") String dummyId, 
		Dummy dummy
	) throws MicroserviceError {
        return _logic.updateDummy(correlationId, dummyId, dummy);
    }

	@DELETE
	@Path("{dummyId}")
    public void deleteDummy(
    	@QueryParam("correlation_id") String correlationId,
		@PathParam("dummyId") String dummyId
	) throws MicroserviceError {
        _logic.deleteDummy(correlationId, dummyId);
    }
}
