package org.pipservices.runtime.cache;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;

/**
 * Abstract implementation for transient cache.
 * It can be used to bypass persistence to increase overall system performance. 
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public abstract class AbstractCache extends AbstractComponent implements ICache {
	/**
	 * Constructs and initializes cache instance.
	 * @param descriptor the unique component descriptor to identify and locate the component
	 */
	protected AbstractCache(ComponentDescriptor descriptor) {
		super(descriptor);
	}
	
	/**
	 * Retrieves a value from the cache by unique key.
	 * It is recommended to use either string GUIDs like '123456789abc'
	 * or unique natural keys prefixed with the functional group
	 * like 'pip-services-storage:block-123'. 
	 * @param key a unique key to locate value in the cache
	 * @return a cached value or <b>null</b> if value wasn't found or timeout expired.
	 */
	public abstract Object retrieve(String key);

	/**
	 * Stores value identified by unique key in the cache. 
	 * Stale timeout is configured in the component options. 
	 * @param key a unique key to locate value in the cache.
	 * @param value a value to store.
	 * @return a cached value stored in the cache.
	 */
	public abstract Object store(String key, Object value);

	/**
	 * Removes value stored in the cache.
	 * @param key a unique key to locate value in the cache.
	 */
	public abstract void remove(String key);
	
}
