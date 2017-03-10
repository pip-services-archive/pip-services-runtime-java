package org.pipservices.runtime.config;

import static org.junit.Assert.*;

import org.junit.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

public class ConfigReaderTest {

	@Test
	public void testJson() throws MicroserviceError {
		MicroserviceConfig config = ConfigReader.read("./test/org/pipservices/runtime/config/options.json");
		DynamicMap content = config.getRawContent();
		
        assertNotNull(content);
        assertEquals(123, content.getInteger("test"));
	}

	@Test
	public void testYaml() throws MicroserviceError {
		MicroserviceConfig config = ConfigReader.read("./test/org/pipservices/runtime/config/options.yaml");
		DynamicMap content = config.getRawContent();
		
        assertNotNull(content);
        assertEquals(123, content.getInteger("test"));
	}

}
