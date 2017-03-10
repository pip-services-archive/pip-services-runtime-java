package org.pipservices.runtime.counters;

import java.util.*;

import org.pipservices.runtime.config.*;
import org.pipservices.runtime.portability.*;

/**
 * Performance counters component that periodically dumps counters
 * to log as message: 'Counter <name> {"type": <type>, "last": <last>, ...}
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public class LogCounters extends AbstractCounters {
	/**
	 * Unique descriptor for the LogCounters component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Counters, "pip-services-runtime-counters", "log", "*"
	);

	/**
	 * Creates instance of log counters component.
	 */
	public LogCounters() {
        super(Descriptor);
    }

	/**
	 * Formats counter string representation.
	 * @param counter a counter object to generate a string for.
	 * @return a formatted string representation of the counter.
	 */
    private String counterToString(Counter counter) {
        String result = "Counter " + counter.getName() + " { ";
        result += "\"type\": " + counter.getType();
        if (counter.getLast() != null)
            result += ", \"last\": " + Converter.toString(counter.getLast());
        if (counter.getCount() != null)
            result += ", \"count\": " + Converter.toString(counter.getCount());
        if (counter.getMin() != null)
            result += ", \"min\": " + Converter.toString(counter.getMin());
        if (counter.getMax() != null)
            result += ", \"max\": " + Converter.toString(counter.getMax());
        if (counter.getAvg() != null)
            result += ", \"avg\": " + Converter.toString(counter.getAvg());
        if (counter.getTime() != null)
            result += ", \"time\": " + Converter.toString(counter.getTime());
        result += " }";
        return result;
    }

    /**
     * Outputs a list of counter values to log.
     * @param counter a list of counters to be dump to log.
     */
    @Override
    protected void save(List<Counter> counters) {
        if (counters.size() == 0) return;

        Collections.sort(
    		counters,
    		(c1, c2) -> c1.getName().compareTo(c2.getName())
		);

        for (Counter counter : counters) {
            debug(null, counterToString(counter));
        }
    }
}
