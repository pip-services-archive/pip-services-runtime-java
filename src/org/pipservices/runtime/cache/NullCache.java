package org.pipservices.runtime.cache;

import org.pipservices.runtime.config.*;

/**
 * Null cache component that doesn't do caching at all.
 * It's mainly used in testing. However, it can be temporary
 * used to disable cache to troubleshoot problems or study
 * effect of caching on overall system performance. 
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public class NullCache extends AbstractCache {
	/**
	 * Unique descriptor for the Memory Cache component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Cache, "pip-services-runtime-cache", "null", "*"
	);

	/**
	 * Creates instance of null cache component.
	 */
	public NullCache() {
		super(Descriptor);
	}
	
	/**
	 * Retrieves a value from the cache by unique key.
	 * It is recommended to use either string GUIDs like '123456789abc'
	 * or unique natural keys prefixed with the functional group
	 * like 'pip-services-storage:block-123'. 
	 * @param key a unique key to locate value in the cache
	 * @return a cached value or <b>null</b> if value wasn't found or timeout expired.
	 */
	@Override 
	public Object retrieve(String key) {
		return null;
	}
	
	/**
	 * Stores value identified by unique key in the cache. 
	 * Stale timeout is configured in the component options. 
	 * @param key a unique key to locate value in the cache.
	 * @param value a value to store.
	 * @return a cached value stored in the cache.
	 */
	@Override
	public Object store(String key, Object value) {
		return value;
	}
	
	/**
	 * Removes value stored in the cache.
	 * @param key a unique key to locate value in the cache.
	 */
	@Override 
	public void remove(String key) { }
}
