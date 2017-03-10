package org.pipservices.runtime.run;

import java.util.concurrent.*;

import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;

public class ProcessRunner {
    private Microservice _microservice;
    private Semaphore _exitEvent = new Semaphore(0);

    public ProcessRunner(Microservice microservice) {
        _microservice = microservice;
    }

    public void setConfig(MicroserviceConfig config) {
        _microservice.setConfig(config);
    }

    public void loadConfig(String configPath) throws MicroserviceError {
        _microservice.loadConfig(configPath);
    }
    
    public void loadConfigWithDefault(
		String[] args, String defaultConfigPath
	) throws MicroserviceError {
    	String configPath = args.length > 0 ? args[0] : defaultConfigPath;
    	_microservice.loadConfig(configPath);
    }
    
    private void captureErrors() {
        Thread.setDefaultUncaughtExceptionHandler(
    		new Thread.UncaughtExceptionHandler() {				
				@Override
				public void uncaughtException(Thread thread, Throwable ex) {
		            _microservice.fatal(ex);
		            _microservice.info("Process is terminated");
		
		            _exitEvent.release();
				}
			}
		);
    }

    private void captureExit() {
        _microservice.info("Press Control-C to stop the microservice...");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	_microservice.info("Goodbye!");
            	
            	_exitEvent.release();
            	
            	//Runtime.getRuntime().exit(1);
            }
         });
        
        // Wait and close
        try {
        	_exitEvent.acquire();
        } catch (InterruptedException ex) {
        	// Ignore...
        }        
    }

    public void run() throws MicroserviceError {
        captureErrors();
    	_microservice.start();
        captureExit();
        _microservice.stop();
    }

    public void runWithConfig(MicroserviceConfig config) throws MicroserviceError {
    	setConfig(config);
    	run();
    }

    public void runWithConfigFile(String configPath) throws MicroserviceError {
    	loadConfig(configPath);
    	run();
    }

    public void runWithDefaultConfigFile(
		String[] args, String defaultConfigPath
	) throws MicroserviceError {
    	loadConfigWithDefault(args, defaultConfigPath);
    	run();
    }
}
