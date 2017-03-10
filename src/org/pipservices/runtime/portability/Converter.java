package org.pipservices.runtime.portability;

import java.time.*;
import java.time.format.*;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

public class Converter {
    private static final DateTimeFormatter simpleDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static final DateTimeFormatter simpleDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	private static ObjectMapper mapper = new ObjectMapper();
	private static TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};

	public static String toNullableString(Object value) {
    	// Shortcuts
        if (value == null) return null;
        if (value instanceof String) return (String)value;

        // Legacy and new dates
        if (value instanceof Date)
        	value = ZonedDateTime.ofInstant(((Date)value).toInstant(), ZoneId.systemDefault());
        if (value instanceof Calendar) { 
        	value = ZonedDateTime.ofInstant(
    			((Calendar)value).toInstant(), ((Calendar)value).getTimeZone().toZoneId());
        }
        if (value instanceof Instant)
        	value = ZonedDateTime.ofInstant((Instant)value, ZoneId.systemDefault());
        if (value instanceof LocalDateTime)
        	value = ZonedDateTime.of((LocalDateTime)value, ZoneId.systemDefault());
        if (value instanceof LocalDate)
        	value = ZonedDateTime.of((LocalDate)value, LocalTime.of(0,0), ZoneId.systemDefault());
        if (value instanceof ZonedDateTime)
        	return ((ZonedDateTime)value).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    	// Everything else
        return value.toString();
	}

	public static String toString(Object value) {
		return toStringWithDefault(value, "");
	}
	
    public static String toStringWithDefault(Object value, String defaultValue) {
    	String result = toNullableString(value);
    	return result != null ? result : defaultValue;
    }

    public static Boolean toNullableBoolean(Object value) {
    	// Shortcuts
        if (value == null) return null;
        if (value instanceof Boolean) return (boolean)value;

    	// All true values
        String strValue = value.toString().toLowerCase();
        if (strValue.equals("1") || strValue.equals("true") 
    		|| strValue.equals("t") || strValue.equals("yes") 
    		|| strValue.equals("y"))
            return true;

    	// All false values
        if (strValue.equals("0") || strValue.equals("false") 
    		|| strValue.equals("f") || strValue.equals("no") 
    		|| strValue.equals("n"))
            return false;

    	// Everything else
        return null;
    }
    
    public static boolean toBoolean(Object value) {
    	return toBooleanWithDefault(value, false);
    }
    
    public static boolean toBooleanWithDefault(Object value, boolean defaultValue) {
    	Boolean result = toNullableBoolean(value);
    	return result != null ? (boolean)result : defaultValue;
    }
    
    public static Long toNullableLong(Object value) {
    	// Shortcuts
    	if (value == null) return null;
    	
    	// All date and times (this is incomplete, consider removing)
    	if (value instanceof Date) return ((Date)value).getTime();
    	if (value instanceof Calendar) return ((Calendar)value).getTimeInMillis();
    	
    	// Booleans
    	if (value instanceof Boolean) return (boolean)value ? 1L : 0L;
    	
    	// All numeric types
    	if (value instanceof Integer) return (long)((int)value);
    	if (value instanceof Short) return (long)((short)value);
    	if (value instanceof Long) return (long)value;
    	if (value instanceof Float) return (long) Math.round((float)value); 
    	if (value instanceof Double) return (long) Math.round((double)value);

    	// Strings
    	if (value instanceof String)
    		try {
    			return (long) Math.round(Double.parseDouble((String)value));
    		} catch (NumberFormatException ex) {
    			return null;
    		}
    	
    	// Everything else
    	return null;
    }

    public static long toLong(Object value) {
    	return toLongWithDefault(value, 0);
    }
    
    public static long toLongWithDefault(Object value, long defaultValue) {
    	Long result = toNullableLong(value);
    	return result != null ? (long)result : defaultValue;
    }

    public static Integer toNullableInteger(Object value) {
    	Long result = toNullableLong(value);
    	return result != null ? (int)((long)result) : null;
    }

    public static int toInteger(Object value) {
    	return toIntegerWithDefault(value, 0);
    }
    
    public static int toIntegerWithDefault(Object value, int defaultValue) {
    	Integer result = toNullableInteger(value);
    	return result != null ? (int)result : defaultValue;
    }

    public static Float toNullableFloat(Object value) {
    	// Shortcuts
    	if (value == null) return null;

    	// All date and times (this is incomplete, consider removing)
    	if (value instanceof Date) return (float)((Date)value).getTime();
    	if (value instanceof Calendar) return (float)((Calendar)value).getTimeInMillis();

    	// Boolean types
    	if (value instanceof Boolean) return (boolean)value ? 1f : 0f;

    	// All numeric types
    	if (value instanceof Integer) return (float)((int)value);
    	if (value instanceof Short) return (float)((short)value);
    	if (value instanceof Long) return (float)((long)value);
    	if (value instanceof Float) return (float)value; 
    	if (value instanceof Double) return (float)((double)value);

    	// Strings
    	if (value instanceof String)
    		try {
    			return (float) Double.parseDouble((String)value);
    		} catch (NumberFormatException ex) {
    			return null;
    		}
    	
    	// Everything else
    	return null;
    }

    public static float toFloat(Object value) {
    	return toFloatWithDefault(value, 0);
    }
    
    public static float toFloatWithDefault(Object value, float defaultValue) {
    	Float result = toNullableFloat(value);
    	return result != null ? (float)result : defaultValue;
    }
    
    private static ZonedDateTime millisToDateTime(long millis) {
    	Instant instant = Instant.ofEpochMilli(millis);
    	return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    
    public static ZonedDateTime toNullableDate(Object value) {
    	// Shortcuts
    	if (value == null) return null;
    	if (value instanceof ZonedDateTime) return (ZonedDateTime)value;

    	// Legacy dates
    	if (value instanceof Calendar) {
    		Calendar calendar = (Calendar)value;
    		return ZonedDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
    	}
    	if (value instanceof Date) 
    		return ZonedDateTime.ofInstant(((Date) value).toInstant(), ZoneId.systemDefault());
    	
    	// New dates
    	if (value instanceof LocalDate)
    		return ZonedDateTime.of((LocalDate)value, LocalTime.of(0, 0), ZoneId.systemDefault());
    	if (value instanceof LocalDateTime)
    		return ZonedDateTime.of((LocalDateTime)value, ZoneId.systemDefault());
    	
    	// Number fields
    	if (value instanceof Integer) return millisToDateTime((int)value);
    	if (value instanceof Short) return millisToDateTime((short)value);
    	if (value instanceof Long) return millisToDateTime((long)value);
    	if (value instanceof Float) return millisToDateTime((long)((float)value)); 
    	if (value instanceof Double) return millisToDateTime((long)((double)value));
    	
    	// Strings
    	if (value instanceof String) {
    		// Parse ISO date and time with zone
    		try {
    			return ZonedDateTime.parse((String)value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    		} catch (DateTimeParseException ex) {
    			// Ignore...
    		}

    		// Parse local date and time
    		try {
    			return ZonedDateTime.of(
					LocalDateTime.parse((String)value, simpleDateTimeFormatter),
					ZoneId.systemDefault()
				);
    		} catch (DateTimeParseException ex) {
    			// Ignore...
    		}

    		// Parse local date
    		try {
    			return ZonedDateTime.of(
					LocalDate.parse((String)value, simpleDateFormatter),
					LocalTime.of(0, 0),
					ZoneId.systemDefault()
				);
    		} catch (DateTimeParseException ex) {
    			// Ignore...
    		}
    	}
    	
    	return null;
    }
    
    public static ZonedDateTime toDate(Object value) {
    	return toDateWithDefault(value, null);
    }
    
    public static ZonedDateTime toDateWithDefault(Object value, ZonedDateTime defaultValue) {
    	ZonedDateTime result = toNullableDate(value);
    	return result != null ? result : defaultValue;
    }
            
	@SuppressWarnings("unchecked")
	public static List<Object> toNullableArray(Object value) {
		// Return null when nothing found
		if (value == null) {
			return null;
		}
		// Convert list
		else if (value instanceof List<?>) {
			return (List<Object>)value;
		}
		// Convert single values
		else {
			List<Object> array = new ArrayList<Object>();
			array.add(value);
			return array;
		}
	}

	public static List<Object> toArray(Object value) {
		List<Object> result = Converter.toNullableArray(value);
		return result != null ? result : new ArrayList<Object>();
	}

	public static List<Object> toArrayWithDefault(Object value, List<Object> defaultValue) {
		List<Object> result = Converter.toNullableArray(value);
		return result != null ? result : defaultValue;
	}

	private static Object arrayToMap(Collection<Object> array) {
    	List<Object> result = new ArrayList<Object>(array.size());
    	for (Object item : array)
    		result.add(valueToMap(item));
    	return result;
    }
    
    private static DynamicMap mapToMap(Map<Object, Object> map) {
    	DynamicMap result = new DynamicMap();
    	for (Map.Entry<Object, Object> entry : map.entrySet()) {
    		result.put(toString(entry.getKey()) , valueToMap(entry.getValue()));
    	}
    	return result;
    }

    private static DynamicMap mapToMap2(Map<String, Object> map) {
    	DynamicMap result = new DynamicMap();
    	for (Map.Entry<String, Object> entry : map.entrySet()) {
    		result.put(entry.getKey(), valueToMap(entry.getValue()));
    	}
    	return result;
    }

	@SuppressWarnings("unchecked")
	private static Object valueToMap(Object value) {
    	if (value == null) return null;
    	if (value instanceof DynamicMap) return value;
    	
    	Class<?> valueClass = value.getClass();
    	if (valueClass.isPrimitive()) return value;
    	
    	if (value instanceof Map<?, ?>) 
    		return mapToMap((Map<Object, Object>)value);

    	if (valueClass.isArray()) { 
    		Object[] array = (Object[])value;
    		return arrayToMap(Arrays.asList(array));
    	}

    	if (value instanceof Collection<?>) {
    		return arrayToMap((Collection<Object>)value);
    	}
    	
    	try {
    		Map<String, Object> map = mapper.convertValue(value, typeRef);
    		return mapToMap2(map);
		} catch (Exception ex) {
			return value;
		}
    }

    public static DynamicMap toNullableMap(Object value) {
    	if (value == null) return null;

    	// Parse JSON
		if (value instanceof String) {
	    	try {
				Map<String, Object> map = mapper.readValue((String)value, typeRef);
				return mapToMap2(map);
			} catch (Exception ex) {
				return null;
			}
		}
		
		// Default parsing
		Object result = valueToMap(value);
		if (result instanceof DynamicMap)
			return (DynamicMap)result;
		return null;
    }

	public static DynamicMap toMap(Object value) {
    	DynamicMap result = toNullableMap(value);
    	return result != null ? result : new DynamicMap();
    }

	public static DynamicMap toMapWithDefault(Object value, DynamicMap defaultValue) {
    	DynamicMap result = toNullableMap(value);
    	return result != null ? result : defaultValue;
    }
}
