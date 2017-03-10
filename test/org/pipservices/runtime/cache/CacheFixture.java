package org.pipservices.runtime.cache;

import static org.junit.Assert.*;

import org.pipservices.runtime.*;

public class CacheFixture {
    private ICache _cache;

    public CacheFixture(ICache cache) {
        _cache = cache;
    }

    public void testBasicOperations() {
    	// Set the first value
    	Object value = _cache.store("test", 123);
    	assertEquals(123, value);
    	
    	value = _cache.retrieve("test");
    	assertEquals(123, value);

    	// Set null value
    	value = _cache.store("test", null);
    	assertNull(value);

		value = _cache.retrieve("test");
		assertNull(value);
		
		// Set the second value
    	value = _cache.store("test", "ABC");
    	assertEquals("ABC", value);
    	
    	value = _cache.retrieve("test");
    	assertEquals("ABC", value);

    	// Unset value
    	_cache.remove("test");

		value = _cache.retrieve("test");
		assertNull(value);
    }

    public void testReadAfterTimeout(int timeout) {
    	// Set value
    	Object value = _cache.store("test", 123);
    	assertEquals(123, value);
    	
    	// Read the value
    	value = _cache.retrieve("test");
    	assertEquals(123, value);
    	
    	// Wait
    	try {
    		Thread.sleep(timeout);
    	} catch (Exception ex) {
    		// Ignore..
    	}
    	
    	// Read the value again
    	value = _cache.retrieve("test");
    	assertNull(value);
    }
    
}