package org.pipservices.runtime.config;

import static org.junit.Assert.*;

import org.junit.*;

public class ComponentDescriptorTest {
	@Test
	public void testMatch() {
		ComponentDescriptor descriptor = new ComponentDescriptor(Category.Controllers, "pip-services-dummies", "default", "1.0");

		// Check match by individual fields
		assertTrue(descriptor.match(new ComponentDescriptor(Category.Controllers, null, null, null)));
		assertTrue(descriptor.match(new ComponentDescriptor(Category.Controllers, "pip-services-dummies", null, null)));
		assertTrue(descriptor.match(new ComponentDescriptor(Category.Controllers, null, "default", null)));
		assertTrue(descriptor.match(new ComponentDescriptor(Category.Controllers, null, null, "1.0")));

		// Check match by individual "*" fields
		assertTrue(descriptor.match(new ComponentDescriptor(Category.Controllers, "*", "*", "*")));
		assertTrue(descriptor.match(new ComponentDescriptor(Category.Controllers, "pip-services-dummies", "*", "*")));
		assertTrue(descriptor.match(new ComponentDescriptor(Category.Controllers, "*", "default", "*")));
		assertTrue(descriptor.match(new ComponentDescriptor(Category.Controllers, "*", "*", "1.0")));

		// Check match by all values
		assertTrue(descriptor.match(new ComponentDescriptor(Category.Controllers, "pip-services-dummies", "default", null)));
		assertTrue(descriptor.match(new ComponentDescriptor(Category.Controllers, null, "default", "1.0")));
		assertTrue(descriptor.match(new ComponentDescriptor(Category.Controllers, "pip-services-dummies", "default", "1.0")));

		// Check match by special BusinessLogic category
		assertTrue(descriptor.match(new ComponentDescriptor(Category.BusinessLogic, null, null, null)));
		
		// Check mismatch by individual fields
		assertFalse(descriptor.match(new ComponentDescriptor(Category.Cache, null, null, null)));
		assertFalse(descriptor.match(new ComponentDescriptor(Category.Controllers, "pip-services-runtime", null, null)));
		assertFalse(descriptor.match(new ComponentDescriptor(Category.Controllers, null, "special", null)));
		assertFalse(descriptor.match(new ComponentDescriptor(Category.Controllers, null, null, "2.0")));
	}

	@Test
	public void testToString() {
		ComponentDescriptor descriptor1 = new ComponentDescriptor(Category.Controllers, "pip-services-dummies", "default", "1.0");		
		assertEquals("controllers/pip-services-dummies/default/1.0", descriptor1.toString());

		ComponentDescriptor descriptor2 = new ComponentDescriptor(Category.Controllers, null, null, null);		
		assertEquals("controllers/*/*/*", descriptor2.toString());
	}
}
