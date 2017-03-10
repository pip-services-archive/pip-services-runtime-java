package org.pipservices.runtime.portability;

import java.lang.reflect.*;
import java.time.*;
import java.util.*;

/**
 * Portable implementation of dynamic data object represented as hash-map of values.
 * It supports handling hierarchical data structures and arrays
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public class DynamicMap extends HashMap<String, Object> {
	private static final long serialVersionUID = -172045239718071099L;

	public DynamicMap() {}
	
	public DynamicMap(Map<String, Object> map) {
		this.putAll(map);
	}
		
	/********** Getters ***********/
	
	@SuppressWarnings("unchecked")
	public Object get(String path) {
		if (path == null || path.equals("")) 
			return null;
		
        String[] props = path.split("\\.");
        Object result = this;

        for (String prop : props) {
        	if (!(result instanceof Map<?,?>))
        		return null;
        	
			Map<String, Object> obj = (Map<String, Object>)result;

            if (!obj.containsKey(prop))
                return null;

            result = obj.get(prop);
        }

        return result;
	}

    public boolean has(String path) {
        return get(path) != null;
    }

    public boolean hasNot(String path) {
        return get(path) == null;
    }

	public DynamicMap getNullableMap(String path) {
        Object value = get(path);
        return Converter.toNullableMap(value);
    }

	public DynamicMap getMap(String path) {
        Object value = get(path);
        DynamicMap map = Converter.toNullableMap(value);
        return map != null ? map : new DynamicMap();
    }

	public DynamicMap getMapWithDefault(String path, DynamicMap defaultValue) {
        Object value = get(path);
        return Converter.toMapWithDefault(value, defaultValue);
    }

	@SuppressWarnings("unchecked")
	public List<Object> getNullableArray(String path) {
		Object value = this.get(path);
		
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

	public List<Object> getArray(String path) {
		List<Object> value = getNullableArray(path);
		return value != null ? value : new ArrayList<Object>();
	}

	public List<Object> getArrayWithDefault(String path, List<Object> defaultValue) {
		List<Object> value = getNullableArray(path);
		return value != null ? value : defaultValue;
	}

	public String getNullableString(String path) {
        Object value = get(path);
        return Converter.toNullableString(value);
    }

	public String getString(String path) {
    	return getStringWithDefault(path, "");
    }

	public String getStringWithDefault(String path, String defaultValue) {
        Object value = get(path);
        return Converter.toStringWithDefault(value, defaultValue);
    }

    public Boolean getNullableBoolean(String path) {
        Object value = get(path);
        return Converter.toNullableBoolean(value);
    }

    public boolean getBoolean(String path) {
    	return getBooleanWithDefault(path, false);
    }

	public boolean getBooleanWithDefault(String path, boolean defaultValue) {
        Object value = get(path);
        return Converter.toBooleanWithDefault(value, defaultValue);
    }

    public Integer getNullableInteger(String path) {
        Object value = get(path);
        return Converter.toNullableInteger(value);
    }

    public int getInteger(String path) {
        return getIntegerWithDefault(path, 0);
    }

    public int getIntegerWithDefault(String path, int defaultValue) {
        Object value = get(path);
        return Converter.toIntegerWithDefault(value, defaultValue);
    }

    public Long getNullableLong(String path) {
        Object value = get(path);
        return Converter.toNullableLong(value);
    }

    public long getLong(String path) {
        return getLongWithDefault(path, 0);
    }

    public long getLongWithDefault(String path, long defaultValue) {
        Object value = get(path);
        return Converter.toLongWithDefault(value, defaultValue);
    }

    public Float getNullableFloat(String path) {
        Object value = get(path);
        return Converter.toNullableFloat(value);
    }

    public float getFloat(String path) {
    	return getFloatWithDefault(path, 0);
    }
    	
	public float getFloatWithDefault(String path, float defaultValue) {
        Object value = get(path);
        return Converter.toFloatWithDefault(value, defaultValue);
    }

    public ZonedDateTime getNullableDate(String path) {
        Object value = get(path);
        return Converter.toNullableDate(value);
    }

    public ZonedDateTime getDate(String path) {
    	return getDateWithDefault(path, null);
    }
    	
	public ZonedDateTime getDateWithDefault(String path, ZonedDateTime defaultValue) {
        Object value = get(path);
        return Converter.toDateWithDefault(value, defaultValue);
    }
	
	/************ Setters ************/

	@SuppressWarnings("unchecked")
	public void set(String path, Object value) {
        if (path == null) return;

        String[] props = path.split("\\.");
        if (props.length == 0) return;
        
        Map<String, Object> container = this;

        for (int i = 0; i < props.length - 1; i++) {
        	String prop = props[i];
        	
        	Object obj = container.get(prop);
        	if (obj == null) {
        		Map<String, Object> temp = new HashMap<String, Object>();
        		container.put(prop, temp);
        		container = temp;
        	} else {
	        	if (!(obj instanceof Map<?,?>))
	        		return;
	        	
				container = (Map<String, Object>)obj;
        	}
        }
        
        container.put(props[props.length - 1], value);
    }

    public void setTuplesArray(Object[] values) {
    	for (int i = 0; i < values.length; i += 2) {
    		if (i + 1 >= values.length) break;
    		
    		String path = Converter.toString(values[i]);
    		Object value = values[i + 1];
    		
    		set(path, value);
    	}
    }

    public void setTuples(Object... values) {
    	setTuplesArray(values);
    }

    public void remove(String path) {
    	// Todo: implement hierarchical delete
    	super.remove(path);
    }
    
    public void removeAll(String... paths) {
    	for (String path : paths) {
    		super.remove(path);
    	}
    }
    
    /********** Merging ************/

	@SuppressWarnings("unchecked")
	public static DynamicMap merge(DynamicMap dest, Map<String, Object> source, boolean deep) {
    	if (dest == null) dest = new DynamicMap();
        if (source == null) return dest;

        for(Map.Entry<String, Object> entry : source.entrySet()) {
            if (dest.containsKey(entry.getKey())) {
                Object configValue = dest.get(entry.getKey());
                Object defaultValue = entry.getValue();

                if (deep && configValue instanceof Map<?,?>
                    && defaultValue instanceof Map<?,?>)
                {                	
                    dest.put(entry.getKey(), merge(
                        new DynamicMap((Map<String, Object>)configValue),
                        (Map<String, Object>)defaultValue,
                        deep
                    ));
                }
            } else {
                dest.put(entry.getKey(), entry.getValue());
            }
        }

        return dest;
    }

 	public DynamicMap merge(Map<String, Object> source, boolean deep) {
 		DynamicMap dest = new DynamicMap(this);
        return DynamicMap.merge(dest, source, deep);
    }
    
 	public DynamicMap mergeDeep(Map<String, Object> source) {    	
        return merge(source, true);
    }

 	/********** Other Utilities **********/
 	
    public void assignTo(Object value) {
        if (value == null || size() == 0) return;

        Class<?> valueClass = value.getClass();
        
        for (Map.Entry<String, Object> entry : entrySet()) {
        	String prop = entry.getKey();
        	
        	try {        		
        		// Set a class field
	            Field field = valueClass.getField(prop);
	            if (field != null) { 
            		field.set(value, entry.getValue());
            		continue;
	            }
        	} catch (Exception ex) {
        		// Ignore...??
        	}
        	
        	try {
            	// Set a property	            	
	            String setter = "set" + prop.substring(0, 1).toUpperCase() + prop.substring(1, prop.length());
	            for (Method method : valueClass.getMethods()) {
	            	if (method.getName().equals(setter)) { 
	            		method.invoke(value, entry.getValue());
	            		continue;
	            	}
	            }
        	} catch (Exception ex) {
        		// Ignore...??
        	}
        }
    }

    public DynamicMap pick(String... paths) {
        DynamicMap result = new DynamicMap();
        for (String path : paths) {
            if (containsKey(path))
                result.put(path, get(path));
        }
        return result;
    }

    public DynamicMap omit(String... paths) {
        DynamicMap result = new DynamicMap(this);        
        for (String path : paths) {
            result.remove(path);
        }        
        return result;
    }

    /********* Class constructors ********/

    /**
     * Creates a dynamic map from free-form object
     * by converting it into the map.
     * @param value a free-form object
     * @return a constructed dynamic map
     */
    public static DynamicMap fromValue(Object value) {
		return Converter.toMap(value);
    }

    /**
     * Creates a dynamic map from list of
     * <path> + <value> tuples
     * @param tuples tuples that contain property path
     * following with property value
     * @return a constructed dynamic map
     */
    public static DynamicMap fromTuples(Object... tuples) {
        DynamicMap result = new DynamicMap();
        result.setTuplesArray(tuples);
        return result;
    }
    
}
