package org.pipservices.runtime.boot;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

public class FileBootConfigTest {
    private IBootConfig configReader;

    @Before
    public void setUp() throws MicroserviceError {
        configReader = new FileBootConfig();
		ComponentConfig config = ComponentConfig.fromTuples(
			"options.path", "test/org/pipservices/runtime/boot/options.yaml"
		);
		configReader.configure(config);
		configReader.link(new DynamicMap(), new ComponentSet());
        configReader.open();
    }

    @SuppressWarnings("unchecked")
	@Test
    public void TestRead() throws MicroserviceError {
    	MicroserviceConfig config = configReader.readConfig();
        DynamicMap options = config.getRawContent();

        assertNotNull(options);
        assertEquals(123, Converter.toInteger(options.get("test")));

        List<Object> array = (List<Object>)options.get("array");
        assertNotNull(array);
        assertEquals(111, Converter.toInteger(array.get(0)));
        assertEquals(222, Converter.toInteger(array.get(1)));
    }
}
