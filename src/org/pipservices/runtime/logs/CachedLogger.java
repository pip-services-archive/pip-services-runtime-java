package org.pipservices.runtime.logs;

import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

public abstract class CachedLogger extends AbstractLogger {
	private final static DynamicMap DefaultConfig = DynamicMap.fromTuples(
		"options.timeout", 1000
	); 

	private ArrayList<LogEntry> _cache = new ArrayList<LogEntry>();
    private Timer _interval = new Timer();

    protected CachedLogger(ComponentDescriptor descriptor) {
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
		checkNewStateAllowed(State.Configured);
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
    				periodicSave();
    			} catch (Exception ex) {
    				error(null, "Failed to write log", ex);
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
		
        periodicSave();
        
        super.close();
    }

    /**
     * Writes a message to the log
     * @param level a log level - Fatal, Error, Warn, Info, Debug or Trace
     * @param component a component name
     * @param correlationId a correlationId
     * @param message a message objects
     */
    @Override
    public void log(int level, String component, String correlationId, Object[] message) {
        if (_level < level) return;

		_cache.add(new LogEntry(level, component, correlationId, message));
    }

    private void periodicSave() throws MicroserviceError {
        if (_cache.size() == 0) return;

        List<LogEntry> entries = _cache;
        _cache = new ArrayList<LogEntry>();
        save(entries);
    }

    protected abstract void save(List<LogEntry> entries) throws MicroserviceError;

}