package org.pipservices.runtime.cache;

/**
 * Holds cached value for in-memory cache.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class CacheEntry {
	private long _created = System.currentTimeMillis();
	private String _key;
	private Object _value;

	/**
	 * Creates instance of the cache entry.
	 * @param key the unique key used to identify and locate the value.
	 * @param value the cached value.
	 */
	public CacheEntry(String key, Object value) {
		_key = key;
		_value = value;
	}
	
	/**
	 * Gets the unique key to identify and locate the value.
	 * @return the value key.
	 */
	public String getKey() { 
		return _key; 
	}

	/**
	 * Gets the cached value.
	 * @return the currently cached value.
	 */
	public Object getValue() {
		return _value;
	}
	
	/**
	 * Changes the cached value and updates creation time.
	 * @param value the new cached value.
	 */
	public void setValue(Object value) {
		_value = value;
		_created = System.currentTimeMillis();
	}

	/**
	 * Gets time when the cached value was stored.
	 * @return the value creation time.
	 */
	public long getCreated() {
		return _created;
	}
}
