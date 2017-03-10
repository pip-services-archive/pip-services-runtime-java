package org.pipservices.runtime.build;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;

public class DummyFactoryTest {
    private final MicroserviceConfig BuildConfig = MicroserviceConfig.fromTuples(
		"logs.type", "console",
		"counters.type", "log",
		"cache.type", "memory",
		"persistence.type", "file",
		"persistence.options.path", "dummies.json",
		"persistence.options.data", new ArrayList<Dummy>(),
		"controllers.type", "*",
		"services.type", "rest",
		"services.version", "1.0"
	);
    
	@Test
	public void testBuildDefaults() throws MicroserviceError {
        MicroserviceConfig config = new MicroserviceConfig();
        ComponentSet components = Builder.build(DummyFactory.Instance, config);
        assertEquals(3, components.getAllOrdered().size());
	}

    @Test
    public void testBuildWithConfig() throws MicroserviceError {
        ComponentSet components = Builder.build(DummyFactory.Instance, BuildConfig);
        assertEquals(6, components.getAllOrdered().size());
    }

    @Test
    public void testBuildWithFile() throws MicroserviceError {
    	MicroserviceConfig config = ConfigReader.read("test/org/pipservices/runtime/build/config.json");
        ComponentSet components = Builder.build(DummyFactory.Instance, config);
        assertEquals(7, components.getAllOrdered().size());
    }

}
