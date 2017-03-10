package org.pipservices.runtime.cache;

import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Local in-memory cache that can be used in non-scaled deployments or for testing.
 * 
 * Todo: Track access time for cached entries to optimize cache shrinking logic
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public class MemoryCache extends AbstractCache {
	/**
	 * Unique descriptor for the Memory Cache component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Cache, "pip-services-runtime-cache", "memory", "*"
	);

	/**
	 * Default configuration for memory cache component
	 */
	private final static DynamicMap DefaultConfig = DynamicMap.fromTuples(
		"options.timeout", 60000, // timeout in milliseconds
		"options.max_size", 1000 // maximum number of elements in cache
	); 

	private Map<String, CacheEntry> _cache = new HashMap<String, CacheEntry>();
	private int _count = 0;
	private long _timeout;
	private int _maxSize;
	
	/**
	 * Creates instance of local in-memory cache component
	 */
	public MemoryCache() {
		super(Descriptor);
	}
	
	/**
	 * Sets component configuration parameters and switches from component
	 * to 'Configured' state. The configuration is only allowed once
	 * right after creation. Attempts to perform reconfiguration will 
	 * cause an exception.
	 * @param config the component configuration parameters.
	 * @throws MicroserviceError when component is in illegal state 
	 * or configuration validation fails. 
	 */
	@Override
	public void configure(ComponentConfig config) throws MicroserviceError {
		checkNewStateAllowed(State.Configured);
		
		config = config.withDefaults(DefaultConfig);
		super.configure(config);

		_timeout = config.getOptions().getLong("timeout");
		_maxSize = config.getOptions().getInteger("max_size");
	}
	
	/**
	 * Cleans up cache from obsolete values and shrinks the cache
	 * to fit into allowed max size by dropping values that were not
	 * accessed for a long time
	 */
    private void cleanup() {
        CacheEntry oldest = null;
        long now = System.currentTimeMillis();
        _count = 0;
        
        // Cleanup obsolete entries and find the oldest
        for (Map.Entry<String, CacheEntry> e : _cache.entrySet()) {
        	String key = e.getKey();
            CacheEntry entry = e.getValue();
            // Remove obsolete entry
            if (_timeout > 0 && (now - entry.getCreated()) > _timeout) {
                _cache.remove(key);
            }
            // Count the remaining entry 
            else {
                _count++;
                if (oldest == null || oldest.getCreated() > entry.getCreated())
                    oldest = entry;
            }
        }
        
        // Remove the oldest if cache size exceeded maximum
        if (_count > _maxSize && oldest != null) {
        	_cache.remove(oldest.getKey());
            _count--;
        }
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
        // Get entry from the cache
        CacheEntry entry = _cache.get(key);
        
        // Cache has nothing
        if (entry == null) {
            return null;
        }
        
        // Remove entry if expiration set and entry is expired
        if (_timeout > 0 && (System.currentTimeMillis() - entry.getCreated()) > _timeout) {
            _cache.remove(key);
            _count--;
            return null;
        }
        
        // Update access timeout
        return entry.getValue();
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
        // Get the entry
        CacheEntry entry = _cache.get(key);

        // Shortcut to remove entry from the cache
        if (value == null) {
            if (entry != null) {
                _cache.remove(key);
                _count--;
            }
            return null;        
        }
        
        // Update the entry
        if (entry != null) {
            entry.setValue(value);
        }
        // Or create a new entry 
        else {
            entry = new CacheEntry(key, value);
            _cache.put(key, entry);
            _count++;
        }

        // Clean up the cache
        if (_maxSize > 0 && _count > _maxSize)
            cleanup();
        
        return value;        
    }
    
	/**
	 * Removes value stored in the cache.
	 * @param key a unique key to locate value in the cache.
	 */
    @Override
    public void remove(String key) {
        // Get the entry
        CacheEntry entry = _cache.get(key);

        // Remove entry from the cache
        if (entry != null) {
            _cache.remove(key);
            _count--;
        }
    }
	
}
