package org.pipservices.runtime.cache;

import org.junit.*;
import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;

public class MemoryCacheTest {
	private ICache cache;
	private CacheFixture fixture;
	
	@Before
	public void setUp() throws Exception {
		ComponentConfig config = ComponentConfig.fromTuples("options.timeout", 500);
		cache = new MemoryCache();
		cache.configure(config);
		fixture = new CacheFixture(cache);
	}
	
	@Test
	public void testBasicOperations() {
		fixture.testBasicOperations();
	}

	@Test
	public void testReadAfterTimeout() {
		fixture.testReadAfterTimeout(1000);
	}
}
