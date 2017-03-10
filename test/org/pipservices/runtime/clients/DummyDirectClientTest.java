package org.pipservices.runtime.clients;

import org.junit.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.logic.*;
import org.pipservices.runtime.persistence.*;
import org.pipservices.runtime.portability.DynamicMap;
import org.pipservices.runtime.run.*;

public class DummyDirectClientTest {
    private DummyMemoryPersistence db;
    private DummyController ctrl;
    private DummyDirectClient client;
    private ComponentSet components;
    private DummyClientFixture fixture;

    public DummyDirectClientTest() throws MicroserviceError {
        db = new DummyMemoryPersistence();
        db.configure(new ComponentConfig());
        
        ctrl = new DummyController();
        ctrl.configure(new ComponentConfig());
        
        client = new DummyDirectClient();
        client.configure(new ComponentConfig());

        components = ComponentSet.fromComponents(db, ctrl, client);
        
        fixture = new DummyClientFixture(client);
    }
    
	@Before
	public void setUp() throws Exception {
		LifeCycleManager.linkAndOpen(new DynamicMap(), components);
	}

	@After
	public void tearDown() throws Exception {
		LifeCycleManager.close(components);
	}

	@Test
	public void testCrudOperations() throws MicroserviceError {
		fixture.testCrudOperations();
	}

}
