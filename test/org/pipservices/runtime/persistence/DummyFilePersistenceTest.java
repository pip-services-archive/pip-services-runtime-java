package org.pipservices.runtime.persistence;

import java.util.*;
import org.junit.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.DynamicMap;

public class DummyFilePersistenceTest {
    private static final ComponentConfig config = ComponentConfig.fromTuples(
    	"type", "file",
    	"options.path", "data/dummies.json",
    	"options.data", new ArrayList<Dummy>()
	);

    private static DummyFilePersistence db;
    private static DummyPersistenceFixture fixture;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
        db = new DummyFilePersistence();
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
