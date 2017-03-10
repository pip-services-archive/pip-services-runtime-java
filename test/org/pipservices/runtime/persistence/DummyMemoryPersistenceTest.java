package org.pipservices.runtime.persistence;

import org.junit.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.DynamicMap;

public class DummyMemoryPersistenceTest {
    private static final ComponentConfig config = ComponentConfig.fromTuples(
    	"type", "memory"
	);

    private static DummyFilePersistence db;
    private static DummyPersistenceFixture fixture;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
        db = new DummyMemoryPersistence();
        fixture = new DummyPersistenceFixture(db);

        db.configure(config);
        db.link(new DynamicMap(), new ComponentSet());
        db.open();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
        db.close();
	}

	@Before
	public void setUp() throws Exception {
		db.clearTestData();
	}

	@Test
    public void testCrudOperations() throws MicroserviceError {
        fixture.testCrudOperations();
    }
	
	@Test
	public void testLoadData() throws MicroserviceError {
		db.load();
	}

}
