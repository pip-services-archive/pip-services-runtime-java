package org.pipservices.runtime.data;

import static org.junit.Assert.*;

import org.junit.Test;
import org.pipservices.runtime.data.IdGenerator;

public class IdGeneratorTest {
	
	@Test
	public void testShortId() {
		String id1 = IdGenerator.getShort();
		assertNotNull(id1);
		assertTrue(id1.length() >= 9);
		
		String id2 = IdGenerator.getShort();
		assertNotNull(id2);
		assertTrue(id2.length() >= 9);
		assertNotEquals(id1, id2);
	}

	@Test
	public void testUiid() {
		String id1 = IdGenerator.uuid();
		assertNotNull(id1);
		assertTrue(id1.length() == 32);

		String id2 = IdGenerator.uuid();
		assertNotNull(id2);
		assertTrue(id2.length() == 32);
		assertNotEquals(id1, id2);
	}

}
