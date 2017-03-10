package org.pipservices.runtime.logic;

import java.util.ArrayList;
import java.util.List;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.persistence.*;
import org.pipservices.runtime.portability.DynamicMap;

public class DummyController extends AbstractController implements IDummyBusinessLogic {	
	/**
	 * Unique descriptor for the DummyController component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Controllers, "pip-services-dummies", "*", "*"
	);

	private IDummyPersistence _db;
    private List<IDummyBusinessLogicListener> _listeners = new ArrayList<IDummyBusinessLogicListener>();

	public DummyController() throws MicroserviceError {
        super(Descriptor);
    }

	@Override
    public void link(DynamicMap context, ComponentSet components) throws MicroserviceError {
        // Locate reference to dummy persistence component
        _db = (IDummyPersistence)components.getOneRequired(
        	new ComponentDescriptor(Category.Persistence, "pip-services-dummies", "*", "*")
    	);

        super.link(context, components);
        
        // Add commands
        DummyCommandSet commands = new DummyCommandSet(this);
        addCommandSet(commands);
	}

    public void addListener(IDummyBusinessLogicListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(IDummyBusinessLogicListener listener) {
        _listeners.remove(listener);
    }
	
    public DataPage<Dummy> getDummies(String correlationId, FilterParams filter, PagingParams paging) 
		throws MicroserviceError {
    	ITiming timing = instrument(correlationId, "dummy.get_dummies");
    	try {
    		return _db.getDummies(correlationId, filter, paging);
    	} finally {
    		timing.endTiming();
    	}
    }
    
    public Dummy getDummyById(String correlationId, String dummyId) throws MicroserviceError {
    	ITiming timing = instrument(correlationId, "dummy.get_dummy_by_id");
    	try {
    		return _db.getDummyById(correlationId, dummyId);
    	} finally {
    		timing.endTiming();
    	}
    }
    
    public Dummy createDummy(String correlationId, Dummy dummy) throws MicroserviceError {
    	ITiming timing = instrument(correlationId, "dummy.create_dummy");
    	try {
    		Dummy result = _db.createDummy(correlationId, dummy);

            for (IDummyBusinessLogicListener listener : _listeners) {
                listener.onDummyCreated(correlationId, result.getId(), result);
            }
            
            return result;
    	} catch (Exception ex) {
            for (IDummyBusinessLogicListener listener : _listeners) {
                listener.onDummyCreateFailed(correlationId, dummy, ex);
            }

            throw ex;
    	} finally {
    		timing.endTiming();
    	}
    }
    
    public Dummy updateDummy(String correlationId, String dummyId, Object dummy) throws MicroserviceError {
    	ITiming timing = instrument(correlationId, "dummy.update_dummy");
    	try {
    		Dummy result = _db.updateDummy(correlationId, dummyId, dummy);
    		
            for (IDummyBusinessLogicListener listener : _listeners) {
                listener.onDummyUpdated(correlationId, dummyId, result);
            }
            
            return result;
    	} catch (Exception ex) {
            for (IDummyBusinessLogicListener listener : _listeners) {
                listener.onDummyUpdateFailed(correlationId, dummyId, dummy, ex);
            }
            
            throw ex;
    	} finally {
    		timing.endTiming();
    	}
    }
    
    public void deleteDummy(String correlationId, String dummyId) throws MicroserviceError {
    	ITiming timing = instrument(correlationId, "dummy.delete_dummy");
    	try {
    		_db.deleteDummy(correlationId, dummyId);

            for (IDummyBusinessLogicListener listener : _listeners) {
                listener.onDummyDeleted(correlationId, dummyId, null);
            }
    	} catch (Exception ex) {
            for (IDummyBusinessLogicListener listener : _listeners) {
                listener.onDummyDeleteFailed(correlationId, dummyId, ex);
            }
            
            throw ex;
    	} finally {
    		timing.endTiming();
    	}
    }

}
