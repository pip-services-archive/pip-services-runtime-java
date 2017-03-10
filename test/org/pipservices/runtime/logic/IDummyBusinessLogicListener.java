package org.pipservices.runtime.logic;

import org.pipservices.runtime.data.Dummy;

public interface IDummyBusinessLogicListener {
    void onDummyCreated(String correlationId, String dummyId, Dummy dummy);
    void onDummyCreateFailed(String correlationId, Dummy dummy, Exception error);

    void onDummyUpdated(String correlationId, String dummyId, Dummy dummy);
    void onDummyUpdateFailed(String correlationId, String dummyId, Object dummy, Exception error);

    void onDummyDeleted(String correlationId, String dummyId, Dummy dummy);
    void onDummyDeleteFailed(String correlationId, String dummyId, Exception error);
}
