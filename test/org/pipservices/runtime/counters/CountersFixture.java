package org.pipservices.runtime.counters;

import static org.junit.Assert.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.errors.*;

public class CountersFixture {
    private AbstractCounters _counters;

    public CountersFixture(AbstractCounters counters) {
        _counters = counters;
    }

    public void testSimpleCounters() throws MicroserviceError {
        _counters.last("Test.LastValue", 123);
        _counters.last("Test.LastValue", 123456);

        Counter counter = _counters.get("Test.LastValue", CounterType.LastValue);
        assertNotNull(counter);
        assertTrue(Math.abs(counter.getLast() - 123456) < 0.001);

        _counters.incrementOne("Test.Increment");
        _counters.increment("Test.Increment", 3);

        counter = _counters.get("Test.Increment", CounterType.Increment);
        assertNotNull(counter);
        assertEquals((int)counter.getCount(), 4);

        _counters.timestampNow("Test.Timestamp");
        _counters.timestampNow("Test.Timestamp");

        counter = _counters.get("Test.Timestamp", CounterType.Timestamp);
        assertNotNull(counter);
        assertNotNull(counter.getTime());

        _counters.stats("Test.Statistics", 1);
        _counters.stats("Test.Statistics", 2);
        _counters.stats("Test.Statistics", 3);

        counter = _counters.get("Test.Statistics", CounterType.Statistics);
        assertNotNull(counter);
        assertTrue(Math.abs(counter.getAvg() - 2) < 0.001);

        _counters.dump();
    }

    public void testMeasureElapsedTime() throws MicroserviceError {
        ITiming timing = _counters.beginTiming("Test.Elapsed");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) { 
        	// Do nothing...
    	} finally {
            timing.endTiming();
        }

        Counter counter = _counters.get("Test.Elapsed", CounterType.Interval);
        assertNotNull(counter);
        assertTrue(counter.getLast() > 50);
        assertTrue(counter.getLast() < 5000);

        _counters.dump();
    }
}
