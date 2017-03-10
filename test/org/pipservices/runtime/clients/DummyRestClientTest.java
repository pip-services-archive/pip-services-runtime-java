package org.pipservices.runtime.clients;

import org.junit.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.logic.*;
import org.pipservices.runtime.persistence.*;
import org.pipservices.runtime.portability.DynamicMap;
import org.pipservices.runtime.run.*;
import org.pipservices.runtime.services.*;

public class DummyRestClientTest {
	private static final ComponentConfig RestOptions = ComponentConfig.fromTuples(
        "type", "rest",
        "endpoint.protocol", "http",
        "endpoint.host", "localhost",
        "endpoint.port", 3000
	);

    private DummyMemoryPersistence db;
    private DummyController ctrl;
    private DummyRestService api;
    private DummyRestClient client;
    private ComponentSet components;
    private DummyClientFixture fixture;

    public DummyRestClientTest() throws MicroserviceError {
        db = new DummyMemoryPersistence();
        db.configure(new ComponentConfig());
        
        ctrl = new DummyController();
        ctrl.configure(new ComponentConfig());
        
        api = new DummyRestService();
        api.configure(RestOptions);
        
        client = new DummyRestClient();
        client.configure(RestOptions);

        components = ComponentSet.fromComponents(db, ctrl, client, api);
        
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
