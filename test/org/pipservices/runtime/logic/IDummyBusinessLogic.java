package org.pipservices.runtime.logic;

import org.pipservices.runtime.*;
import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;

public interface IDummyBusinessLogic extends IBusinessLogic {
    void addListener(IDummyBusinessLogicListener listener);
    void removeListener(IDummyBusinessLogicListener listener);

    DataPage<Dummy> getDummies(String correlationId, FilterParams filter, PagingParams paging) throws MicroserviceError;
    Dummy getDummyById(String correlationId, String dummyId) throws MicroserviceError;
    Dummy createDummy(String correlationId, Dummy dummy) throws MicroserviceError;
    Dummy updateDummy(String correlationId, String dummyId, Object dummy) throws MicroserviceError;
    void deleteDummy(String correlationId, String dummyId) throws MicroserviceError;
}
