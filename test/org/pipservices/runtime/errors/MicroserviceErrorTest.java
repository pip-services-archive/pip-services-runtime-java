package org.pipservices.runtime.errors;

import static org.junit.Assert.*;

import org.junit.Test;

public class MicroserviceErrorTest {

	@Test
	public void test() {
		MicroserviceError error = new MicroserviceError(null, (String)null, null, "Test error")
			.forComponent("TestComponent").withCode("TestError");
		
		assertEquals("TestComponent", error.getComponent());
		assertEquals("TestError", error.getCode());
		assertEquals("Test error", error.getMessage());
		
		error = new MicroserviceError().forComponent("TestComponent");

		assertEquals("TestComponent", error.getComponent());
		assertEquals("Undefined", error.getCode());
		assertEquals("Unknown error", error.getMessage());
	}

}
