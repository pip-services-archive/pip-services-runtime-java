package org.pipservices.runtime.counters;

import org.junit.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.logs.*;
import org.pipservices.runtime.portability.DynamicMap;

public class LogCountersTest {
    private LogCounters counters;
    private CountersFixture fixture;
    
	@Before
	public void setUp() throws MicroserviceError {
        ConsoleLogger log = new ConsoleLogger();

        counters = new LogCounters();
        counters.configure(new ComponentConfig());
        counters.link(new DynamicMap(), ComponentSet.fromComponents(log));
        counters.open();
        
        fixture = new CountersFixture(counters);
    }

	@After
	public void tearDown() throws MicroserviceError {
        counters.close();
    }

    @Test
    public void testSimpleCounters() throws MicroserviceError {
        fixture.testSimpleCounters();
    }

    @Test
    public void TestMeasureElapsedTime() throws MicroserviceError {
        fixture.testMeasureElapsedTime();
    }
}
