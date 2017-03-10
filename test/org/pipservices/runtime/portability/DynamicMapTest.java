package org.pipservices.runtime.portability;

import static org.junit.Assert.*;

import org.junit.*;
import org.pipservices.runtime.portability.DynamicMap;

public class DynamicMapTest {
	@Test
	public void testMerge() {
        DynamicMap result = DynamicMap.fromTuples(
    		"value1", 123, 
    		"value2", 234
		);
        DynamicMap defaults = DynamicMap.fromTuples(
    		"value2", 432, 
    		"value3", 345
		);
        result = result.merge(defaults, false);
        assertEquals(3, result.size());
        assertEquals(123, result.get("value1"));
        assertEquals(234, result.get("value2"));
        assertEquals(345, result.get("value3"));
	}

	@Test
    public void testMergeRecursive() {
        DynamicMap result = DynamicMap.fromValue(
    		"{ \"value1\": 123, \"value2\": { \"value21\": 111, \"value22\": 222 } }"
		);
        DynamicMap defaults = DynamicMap.fromValue(
    		"{ \"value2\": { \"value22\": 777, \"value23\": 333 }, \"value3\": 345 }"
		);
        result = result.merge(defaults, true);

        assertEquals(3, result.size());
        assertEquals(123, result.get("value1"));
        assertEquals(345, result.get("value3"));

        DynamicMap deepResult = result.getMap("value2");
        assertEquals(3, deepResult.size());
        assertEquals(111, deepResult.get("value21"));
        assertEquals(222, deepResult.get("value22"));
        assertEquals(333, deepResult.get("value23"));
    }

	@Test
    public void testMergeWithNulls() {
        DynamicMap result = DynamicMap.fromValue(
    		"{ \"value1\": 123, \"value2\": 234 }"
		);
        result = result.merge(null, true);

        assertEquals(2, result.size());
        assertEquals(123, result.get("value1"));
        assertEquals(234, result.get("value2"));
    }

	class TestClass {
		public TestClass(Object value1, Object value2) {
			this.value1 = value1;
			this.setValue2(value2);
		}
		
		public Object value1;
		private Object _value2;
		
		public Object getValue2() { return _value2; }
		public void setValue2(Object value) { _value2 = value; } 
	}
	
	@Test 
	public void testAssign() {
		TestClass value = new TestClass(null, null);
		DynamicMap newValues = DynamicMap.fromValue(
			"{ \"value1\": 123, \"value2\": \"ABC\", \"value3\": 456 }"
		);
		
		newValues.assignTo(value);
		assertNotNull(value.value1);
		assertEquals(123, value.value1);
		assertNotNull(value.getValue2());
		assertEquals("ABC", value.getValue2());
	}
	
	@Test
    public void testGet() {
        DynamicMap config = DynamicMap.fromValue(
    		"{ \"value1\": 123, \"value2\": { \"value21\": 111, \"value22\": 222 } }"
		);

        Object value = config.get("");
        assertNull(value);

        value = config.get("value1");
        assertNotNull(value);
        assertEquals(123, value);

        value = config.get("value2");
        assertNotNull(value);

        value = config.get("value3");
        assertNull(value);

        value = config.get("value2.value21");
        assertNotNull(value);
        assertEquals(111, value);

        value = config.get("value2.value31");
        assertNull(value);

        value = config.get("value2.value21.value211");
        assertNull(value);

        value = config.get("valueA.valueB.valueC");
        assertNull(value);
    }

    @Test
    public void testHas() {
        DynamicMap config = DynamicMap.fromValue(
    		"{ \"value1\": 123, \"value2\": { \"value21\": 111, \"value22\": 222 } }"
		);

        boolean has = config.has("");
        assertFalse(has);

        has = config.has("value1");
        assertTrue(has);

        has = config.has("value2");
        assertTrue(has);

        has = config.has("value3");
        assertFalse(has);

        has = config.has("value2.value21");
        assertTrue(has);

        has = config.has("value2.value31");
        assertFalse(has);

        has = config.has("value2.value21.value211");
        assertFalse(has);

        has = config.has("valueA.valueB.valueC");
        assertFalse(has);
    }

	@Test
    public void testSet() {
    	DynamicMap config = new DynamicMap();
    	
    	config.set(null, 123);
    	assertEquals(0, config.size());
    	
    	config.set("field1", 123);
    	assertEquals(1, config.size());
    	assertEquals(123, config.get("field1"));

    	config.set("field2", "ABC");
    	assertEquals(2, config.size());
    	assertEquals("ABC", config.get("field2"));

    	config.set("field2.field1", 123);
    	assertEquals("ABC", config.get("field2"));

    	config.set("field3.field31", 456);
    	assertEquals(3, config.size());
    	DynamicMap subConfig = config.getNullableMap("field3");
    	assertNotNull(subConfig);
    	assertEquals(456, subConfig.get("field31"));
    	
    	config.set("field3.field32", "XYZ");
    	assertEquals("XYZ", config.get("field3.field32"));
    }
}
