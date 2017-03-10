package org.pipservices.runtime.persistence;

import static org.junit.Assert.*;

import org.pipservices.runtime.data.DataPage;
import org.pipservices.runtime.data.Dummy;
import org.pipservices.runtime.errors.*;

public class DummyPersistenceFixture {
    private final Dummy DUMMY1 = new Dummy(null, "Key 1", "Content 1");
    private final Dummy DUMMY2 = new Dummy(null, "Key 2", "Content 2");

    private IDummyPersistence _db;

    public DummyPersistenceFixture(IDummyPersistence db) {
        assertNotNull(db);
        _db = db;
    }

    public void testCrudOperations() throws MicroserviceError {
        // Create one dummy
        Dummy dummy1 = _db.createDummy(null, DUMMY1);

        assertNotNull(dummy1);
        assertNotNull(dummy1.getId());
        assertEquals(DUMMY1.getKey(), dummy1.getKey());
        assertEquals(DUMMY1.getContent(), dummy1.getContent());

        // Create another dummy
        Dummy dummy2 = _db.createDummy(null, DUMMY2);

        assertNotNull(dummy2);
        assertNotNull(dummy2.getId());
        assertEquals(DUMMY2.getKey(), dummy2.getKey());
        assertEquals(DUMMY2.getContent(), dummy2.getContent());

        // Get all dummies
        DataPage<Dummy> dummies = _db.getDummies(null, null, null);
        assertNotNull(dummies);
        assertEquals(2, dummies.getData().size());

        // Update the dummy
        Dummy dummy = _db.updateDummy(
    		null,
            dummy1.getId(), 
            "{ \"content\": \"Updated Content 1\" }"
        );

        assertNotNull(dummy);
        assertEquals(dummy1.getId(), dummy.getId());
        assertEquals(dummy1.getKey(), dummy.getKey());
        assertEquals("Updated Content 1", dummy.getContent());

        // Delete the dummy
        _db.deleteDummy(null, dummy1.getId());

        // Try to get deleted dummy
        dummy = _db.getDummyById(null, dummy1.getId());
        assertNull(dummy);
    }
}
