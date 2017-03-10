package org.pipservices.runtime.run;

import org.junit.*;
import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.counters.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.logs.*;
import org.pipservices.runtime.portability.DynamicMap;

public class LifeCycleManagerTest {
    private ComponentSet components;
    private DynamicMap context;

    @Before
    public void setUp() throws MicroserviceError {
        ILogger log = new NullLogger();
        log.configure(new ComponentConfig());
        
        ICounters counters = new NullCounters();
        counters.configure(new ComponentConfig());

        components = ComponentSet.fromComponents(log, counters);
        
        context = new DynamicMap();
    }

    @Test
    public void testLink() throws MicroserviceError {
        LifeCycleManager.link(context, components);
    }

    @Test
    public void testInitAndOpen() throws MicroserviceError {
        LifeCycleManager.linkAndOpen(context, components);
    }

    @Test
    public void testOpen() throws MicroserviceError {
        LifeCycleManager.link(context, components);
        LifeCycleManager.open(components);
    }

    @Test
    public void testClose() throws MicroserviceError {
        LifeCycleManager.linkAndOpen(context, components);
        LifeCycleManager.close(components);
    }

    @Test
    public void testForceClose() throws MicroserviceError {
        LifeCycleManager.linkAndOpen(context, components);
        LifeCycleManager.forceClose(components);
    }
}
