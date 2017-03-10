package org.pipservices.runtime.counters;

import java.time.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;

/**
 * Performance counters component that doesn't calculate or do anything.
 * NullCounter can be used to disable performance monitoring for performance reasons.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public class NullCounters extends AbstractComponent implements ICounters {
	/**
	 * Unique descriptor for the NullCounters component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Counters, "pip-services-runtime-counters", "null", "*"
	);

	/**
	 * Creates instance of null counter that doesn't do anything.
	 */
	public NullCounters() {
        super(Descriptor);
    }

	/**
	 * Suppresses time measurements and returns
	 * ITiming interface with endTiming() method
	 * that doesn't do another,
	 * @param name the name of interval counter.
	 * @return callback interface with empty endTiming() method.
	 */
	@Override
    public ITiming beginTiming(String name) {
        return new ITiming() {
        	public void endTiming() {}
        };
    }

	/**
	 * Suppresses calculation of statistics 
	 * @param name the name of statistics counter.
	 * @param value the value to add to statistics calculations.
	 */
	@Override
    public void stats(String name, float value) { }
	
	/**
	 * Suppresses recording of the last value.
	 * @param name the name of last value counter
	 * @param value the value to be stored as the last one
	 */
	@Override
    public void last(String name, float value) { }
    
	/**
	 * Suppresses recording of the specified time.
	 * TimestampNow shall also be suppressed since it
	 * redirects to this method.
	 * @param name the name of timing counter
     * @param value the specified time
	 */
	@Override
	public void timestamp(String name, ZonedDateTime value) { }

	/**
	 * Suppresses counter increment.
	 * @param name the name of the increment value.
	 * @param value number to increase the counter.
	 */
	@Override
	public void increment(String name, int value) { }
}
