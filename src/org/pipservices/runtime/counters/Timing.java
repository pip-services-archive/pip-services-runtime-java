package org.pipservices.runtime.counters;

import org.pipservices.runtime.*;

/**
 * Implementation of ITiming interface that
 * provides callback to end measuring execution
 * time interface and update interval counter.
 * 
 * @author Sergey Seroukhov
 * @version 1.1
 * @since 2016-06-09
 */
public class Timing implements ITiming {
	private long _start;
	private AbstractCounters _counters;
	private String _name;
	
	/**
	 * Creates instance of timing object that doesn't record anything
	 */
	public Timing() {}
	
	/**
	 * Creates instance of timing object that calculates elapsed time
	 * and stores it to specified performance counters component under specified name.
	 * @param counters a performance counters component to store calculated value.
	 * @param name a name of the counter to record elapsed time interval.
	 */
	public Timing(AbstractCounters counters, String name) {
		_counters = counters;
		_name = name;
		_start = System.currentTimeMillis();
	}
	
	/**
	 * Completes measuring time interval and updates counter.
	 */
	public void endTiming() {
		if (_counters != null) {
			float elapsed = System.currentTimeMillis() - _start;
			_counters.setTiming(_name, elapsed);
		}
	}
}
