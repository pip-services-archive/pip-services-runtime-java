package org.pipservices.runtime;

/**
 * Transient cache which is used to bypass persistence 
 * to increase overall system performance. 
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public interface ICache extends IComponent {
	/**
	 * Retrieves a value from the cache by unique key.
	 * It is recommended to use either string GUIDs like '123456789abc'
	 * or unique natural keys prefixed with the functional group
	 * like 'pip-services-storage:block-123'. 
	 * @param key a unique key to locate value in the cache
	 * @return a cached value or <b>null</b> if value wasn't found or timeout expired.
	 */
	Object retrieve(String key);
	
	/**
	 * Stores value identified by unique key in the cache. 
	 * Stale timeout is configured in the component options. 
	 * @param key a unique key to locate value in the cache.
	 * @param value a value to store.
	 * @return a cached value stored in the cache.
	 */
	Object store(String key, Object value);
	
	/**
	 * Removes value stored in the cache.
	 * @param key a unique key to locate value in the cache.
	 */
	void remove(String key);
}
