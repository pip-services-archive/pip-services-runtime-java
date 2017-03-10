package org.pipservices.runtime;

import java.time.*;

/**
 * Interface for performance counters. These components
 * are used to measure non-functional characteristics
 * of microservice components: number of calls,
 * execution time, timing of key events, etc.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public interface ICounters extends IComponent {
	/**
	 * Starts measurement of execution time interval.
	 * The method returns ITiming object that provides endTiming()
	 * method that shall be called when execution is completed
	 * to calculate elapsed time and update the counter.
	 * @param name the name of interval counter.
	 * @return callback interface with endTiming() method 
	 * that shall be called at the end of execution.
	 */
	ITiming beginTiming(String name);
	
	/**
	 * Calculates rolling statistics: minimum, maximum, average
	 * and updates Statistics counter.
	 * This counter can be used to measure various non-functional
	 * characteristics, such as amount stored or transmitted data,
	 * customer feedback, etc. 
	 * @param name the name of statistics counter.
	 * @param value the value to add to statistics calculations.
	 */
	void stats(String name, float value);
	
	/**
	 * Records the last reported value. 
	 * This counter can be used to store performance values reported
	 * by clients or current numeric characteristics such as number
	 * of values stored in cache.
	 * @param name the name of last value counter
	 * @param value the value to be stored as the last one
	 */
	void last(String name, float value);
	
	/**
	 * Records the current time.
	 * This counter can be used to track timing of key
	 * business transactions.
	 * @param name the name of timing counter
	 */
	void timestampNow(String name);
	
	/**
	 * Records specified time.
	 * This counter can be used to tack timing of key
	 * business transactions as reported by clients.
	 * @param name the name of timing counter.
	 * @param value the reported timing to be recorded.
	 */
	void timestamp(String name, ZonedDateTime value);
	
	/**
	 * Increments counter by value of 1.
	 * This counter is often used to calculate
	 * number of client calls or performed transactions.
	 * @param name the name of counter counter.
	 */
	void incrementOne(String name);
	
	/**
	 * Increments counter by specified value.
	 * This counter can be used to track various
	 * numeric characteristics
	 * @param name the name of the increment value.
	 * @param value number to increase the counter.
	 */
	void increment(String name, int value);
}
