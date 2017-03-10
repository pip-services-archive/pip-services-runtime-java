package org.pipservices.runtime.counters;

import java.time.*;
import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

public abstract class AbstractCounters extends AbstractComponent implements ICounters {
	private final static DynamicMap DefaultConfig = DynamicMap.fromTuples(
		"options.timeout", 60000
	); 
	
    private Map<String, Counter> _cache = new HashMap<String, Counter>();
    private boolean _updated = false;
    private Timer _interval = new Timer();

	/**
	 * Creates and initializes instance of the microservice performance counter
	 * @param descriptor the unique descriptor that is used to identify and locate the component.
	 */
    protected AbstractCounters(ComponentDescriptor descriptor) {
        super(descriptor);
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
        super.configure(config.withDefaults(DefaultConfig));
	}
    
	/**
	 * Opens the component, performs initialization, opens connections
	 * to external services and makes the component ready for operations.
	 * Opening can be done multiple times: right after linking 
	 * or reopening after closure.  
	 * @throws MicroserviceError when initialization or connections fail.
	 */
	@Override
    public void open() throws MicroserviceError {
		checkNewStateAllowed(State.Opened);		

		// Define dump timeout 
        int timeout = Math.max(1000, _config.getOptions().getInteger("timeout"));

    	// Stop previously set timer
    	_interval.purge();
    	
    	// Set a new timer
    	TimerTask task = new TimerTask() {
    		@Override
    		public void run() {
    			try {
    				dump();
    			} catch (Exception ex) {
    				error(null, "Failed to dump counters", ex);
    			}
    		}
    	};
    	_interval.scheduleAtFixedRate(task, timeout, timeout);        	

        super.open();
    }

	/**
	 * Closes the component and all open connections, performs deinitialization
	 * steps. Closure can only be done from opened state. Attempts to close
	 * already closed component or in wrong order will cause exception.
	 * @throws MicroserviceError with closure fails.
	 */
	@Override
    public void close() throws MicroserviceError {
		checkNewStateAllowed(State.Closed);		

		// Stop previously set timer
		_interval.purge();

        // Save and clear counters if any
        if (_updated) {
            List<Counter> counters = this.getAll();
            save(counters);
            resetAll();
        }

        super.close();
    }
	
    protected abstract void save(List<Counter> counters) throws MicroserviceError;

    public void reset(String name) {
        _cache.remove(name);
    }

    public void resetAll() {
        _cache.clear();
        _updated = false;
    }

    public void dump() throws MicroserviceError {
        if (_updated) {
            List<Counter> counters = this.getAll();
            save(counters);
        }
    }

    public List<Counter> getAll() {
        return new ArrayList<Counter>(_cache.values());
    }

    public Counter get(String name, int type) {
        if (name == null || name.length() == 0)
            throw new NullPointerException("Counter name was not set");

        Counter counter = _cache.get(name);

        if (counter == null || counter.getType() != type) {
            counter = new Counter(name, type);
            _cache.put(name, counter);
        }

        return counter;
    }

    private void calculateStats(Counter counter, float value) {
        if (counter == null)
        	throw new NullPointerException("Missing counter");

        counter.setLast(value);
        counter.setCount(counter.getCount() != null ? counter.getCount() + 1 : 1);
        counter.setMax(counter.getMax() != null ? Math.max(counter.getMax(), value) : value);
        counter.setMin(counter.getMin() != null ? Math.min(counter.getMin(), value) : value);
        counter.setAvg(counter.getAvg() != null && counter.getCount() > 1
            ? ((counter.getAvg() * (counter.getCount() - 1)) + value) / counter.getCount() : value);

        _updated = true;
    }

    void setTiming(String name, float elapsed) {
    	Counter counter = get(name, CounterType.Interval);
    	calculateStats(counter, elapsed);
    }
    
	/**
	 * Starts measurement of execution time interval.
	 * The method returns ITiming object that provides endTiming()
	 * method that shall be called when execution is completed
	 * to calculate elapsed time and update the counter.
	 * @param name the name of interval counter.
	 * @return callback interface with endTiming() method 
	 * that shall be called at the end of execution.
	 */
    @Override
    public ITiming beginTiming(String name) {
        return new Timing(this, name);
    }

	/**
	 * Calculates rolling statistics: minimum, maximum, average
	 * and updates Statistics counter.
	 * This counter can be used to measure various non-functional
	 * characteristics, such as amount stored or transmitted data,
	 * customer feedback, etc. 
	 * @param name the name of statistics counter.
	 * @param value the value to add to statistics calculations.
	 */
    @Override
    public void stats(String name, float value) {
        Counter counter = get(name, CounterType.Statistics);
        calculateStats(counter, value);
    }

	/**
	 * Records the last reported value. 
	 * This counter can be used to store performance values reported
	 * by clients or current numeric characteristics such as number
	 * of values stored in cache.
	 * @param name the name of last value counter
	 * @param value the value to be stored as the last one
	 */
    @Override
    public void last(String name, float value) {
        Counter counter = get(name, CounterType.LastValue);
        counter.setLast(value);
        _updated = true;
    }

	/**
	 * Records specified time.
	 * This counter can be used to tack timing of key
	 * business transactions as reported by clients.
	 * @param name the name of timing counter.
	 * @param value the reported timing to be recorded.
	 */
    @Override
    public void timestamp(String name, ZonedDateTime value) {
        Counter counter = get(name, CounterType.Timestamp);
        counter.setTime(value != null ? value : ZonedDateTime.now(ZoneId.of("Z")));
        _updated = true;
    }

	/**
	 * Increments counter by specified value.
	 * This counter can be used to track various
	 * numeric characteristics
	 * @param name the name of the increment value.
	 * @param value number to increase the counter.
	 */
    @Override
    public void increment(String name, int value) {
        Counter counter = get(name, CounterType.Increment);
        counter.setCount(counter.getCount() != null
            ? counter.getCount() + value : value);
        _updated = true;
    }
}
