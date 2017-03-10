package org.pipservices.runtime.portability;

import static org.junit.Assert.*;

import java.time.*;
import java.util.*;
import org.junit.*;
import org.pipservices.runtime.portability.Converter;
import org.pipservices.runtime.portability.DynamicMap;

public class ConverterTest {

	@Test
	public void testToString() {
		assertNull(Converter.toNullableString(null));
		assertEquals("xyz", Converter.toString("xyz"));
        assertEquals("123", Converter.toString(123));
        assertEquals("true", Converter.toString(true));
        //assertEquals("{ prop = xyz }", Converter.toString(new { prop = "xyz" }, "xyz"));

        assertEquals("xyz", Converter.toStringWithDefault(null, "xyz"));
	}

	@Test
    public void testToBoolean() {
        assertTrue(Converter.toBoolean(true));
        assertTrue(Converter.toBoolean(1));
        assertTrue(Converter.toBoolean("True"));
        assertTrue(Converter.toBoolean("yes"));
        assertTrue(Converter.toBoolean("1"));
        assertTrue(Converter.toBoolean("Y"));

        assertFalse(Converter.toBoolean(false));
        assertFalse(Converter.toBoolean(0));
        assertFalse(Converter.toBoolean("False"));
        assertFalse(Converter.toBoolean("no"));
        assertFalse(Converter.toBoolean("0"));
        assertFalse(Converter.toBoolean("N"));

        assertFalse(Converter.toBoolean(123));
        assertTrue(Converter.toBooleanWithDefault("XYZ", true));
    }
	
	@Test
	public void testToInteger() {
        assertEquals(123, Converter.toInteger(123));
        assertEquals(123, Converter.toInteger(123.456));
        assertEquals(123, Converter.toInteger("123"));
        assertEquals(123, Converter.toInteger("123.465"));

        assertEquals(123, Converter.toIntegerWithDefault(null, 123));
        assertEquals(0, Converter.toIntegerWithDefault(false, 123));
        assertEquals(123, Converter.toIntegerWithDefault("ABC", 123));
	}

	@Test
    public void testToFloat() {
        assertTrue(Math.abs(123 - Converter.toFloat(123)) < 0.001);
        assertTrue(Math.abs(123.456 - Converter.toFloat(123.456)) < 0.001);
        assertTrue(Math.abs(123.456 - Converter.toFloat("123.456")) < 0.001);

        assertTrue(Math.abs(123 - Converter.toFloatWithDefault(null, 123)) < 0.001);
        assertTrue(Math.abs(0 - Converter.toFloatWithDefault(false, 123)) < 0.001);
        assertTrue(Math.abs(123 - Converter.toFloatWithDefault("ABC", 123)) < 0.001);
    }
	
	@Test
    public void testToDate() {
        assertNull(Converter.toDate(null));

        ZonedDateTime date1 = ZonedDateTime.of(LocalDateTime.of(1975, 4, 8, 0, 0), ZoneId.systemDefault());
        assertEquals(date1, Converter.toDateWithDefault(null, date1));
        assertEquals(date1, Converter.toDate(new GregorianCalendar(1975, 3, 8)));
        
        ZonedDateTime date2 = ZonedDateTime.ofInstant(Instant.ofEpochMilli(123456), ZoneId.systemDefault());
        assertEquals(date2, Converter.toDate(123456));
        
        ZonedDateTime date3 = ZonedDateTime.of(LocalDateTime.of(1975, 4, 8, 0, 0), ZoneId.of("Z"));
        assertEquals(date3, Converter.toDate("1975-04-08T00:00:00Z"));
        assertEquals(date1, Converter.toDate("1975/04/08"));
        
        assertNull(Converter.toDate("XYZ"));
    }
	
	class TestClass {
		public TestClass(Object value1, Object value2) {
			this.value1 = value1;
			this.value2 = value2;
		}
		
		public Object value1;
		public Object value2;
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testObjectToMap() {
		// Handling nulls
		Object value = null;
		Map<String, Object> result = Converter.toNullableMap(value);
		assertNull(result);
		
		// Handling simple objects
		value = new TestClass(123, 234);
		result = Converter.toNullableMap(value);
		assertEquals(123, result.get("value1"));
		assertEquals(234, result.get("value2"));

        // Handling dictionaries
        value = new DynamicMap();
        result = Converter.toNullableMap(value);
        assertSame(value, result);

        // Non-recursive conversion
//        value = new TestClass(123, new TestClass(111, 222));
//        result = Converter.toMap(value, null, false);
//        assertNotNull(result);
//        assertEquals(123, result.get("value1"));
//        assertNotNull(result.get("value2"));
//        assertFalse(result.get("value2") instanceof Map<?,?>);
//        assertTrue(result.get("value2") instanceof TestClass);

        // Recursive conversion
        value = new TestClass(123, new TestClass(111, 222));
        result = Converter.toNullableMap(value);
        assertNotNull(result);
        assertEquals(123, result.get("value1"));
        assertNotNull(result.get("value2"));
        assertTrue(result.get("value2") instanceof Map<?,?>);

        // Handling arrays
        value = new TestClass(new Object[] { new TestClass(111, 222) }, null);
        result = Converter.toNullableMap(value);
        assertNotNull(result);
        assertTrue(result.get("value1") instanceof List<?>);
        List<Object> resultElements = ((List<Object>)result.get("value1"));
        Map<String, Object> resultElement0 = (Map<String, Object>)resultElements.get(0);
        assertNotNull(resultElement0);
        assertEquals(111, resultElement0.get("value1"));
        assertEquals(222, resultElement0.get("value2"));

        // Handling lists
        value = new TestClass(Arrays.asList(new Object[] { new TestClass(111, 222) }), null);
        result = Converter.toNullableMap(value);
        assertNotNull(result);
        assertTrue(result.get("value1") instanceof List<?>);
        resultElements = ((List<Object>)result.get("value1"));
        resultElement0 = (Map<String, Object>)resultElements.get(0);
        assertNotNull(resultElement0);
        assertEquals(111, resultElement0.get("value1"));
        assertEquals(222, resultElement0.get("value2"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testJsonToMap() {
		// Handling simple objects
		String value = "{ \"value1\":123, \"value2\":234 }";
		Map<String, Object> result = Converter.toNullableMap(value);
		assertEquals(123, result.get("value1"));
		assertEquals(234, result.get("value2"));

        // Recursive conversion
        value = "{ \"value1\":123, \"value2\": { \"value1\": 111, \"value2\": 222 } }";
        result = Converter.toNullableMap(value);
        assertNotNull(result);
        assertEquals(123, result.get("value1"));
        assertNotNull(result.get("value2"));
        assertTrue(result.get("value2") instanceof Map<?,?>);

        // Handling arrays
        value = "{ \"value1\": [{ \"value1\": 111, \"value2\": 222 }] }";
        result = Converter.toNullableMap(value);
        assertNotNull(result);
        assertTrue(result.get("value1") instanceof List<?>);
        List<Object> resultElements = ((List<Object>)result.get("value1"));
        Map<String, Object> resultElement0 = (Map<String, Object>)resultElements.get(0);
        assertNotNull(resultElement0);
        assertEquals(111, resultElement0.get("value1"));
        assertEquals(222, resultElement0.get("value2"));
	}

}
