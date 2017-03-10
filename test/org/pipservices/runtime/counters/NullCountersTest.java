package org.pipservices.runtime.counters;

import org.junit.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.logs.*;
import org.pipservices.runtime.portability.DynamicMap;

public class NullCountersTest {
    private ICounters counters;

	@Before
	public void setUp() throws MicroserviceError {
        ConsoleLogger log = new ConsoleLogger();

        counters = new NullCounters();
        counters.configure(new ComponentConfig());
        counters.link(new DynamicMap(), ComponentSet.fromComponents(log));
        counters.open();
    }

	@After
	public void tearDown() throws MicroserviceError {
        counters.close();
    }

    @Test
    public void testSimpleCounters() throws MicroserviceError {
        counters.last("Test.LastValue", 123);
        counters.increment("Test.Increment", 3);
        counters.stats("Test.Statistics", 123);
    }

    @Test
    public void TestMeasureElapsedTime() throws MicroserviceError {
        ITiming timer = counters.beginTiming("Test.Elapsed");
        timer.endTiming();
    }
}
