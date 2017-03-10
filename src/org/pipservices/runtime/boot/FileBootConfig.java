package org.pipservices.runtime.boot;

import java.io.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Configuration reader that gets microservice configuration from
 * JSON file on local disk. 
 * 
 * This is the simplest possible configuration.
 * However, in large scale deployments it may be unpractical. 
 * The distrubuting configurations from a centralized configuration 
 * repository may be a better option. Check other types of readers
 * to support those scenarios.
 * 
 * @author Sergey Seroukhov
 * @version 1.1
 * @since 2016-06-19
 */
public class FileBootConfig extends AbstractBootConfig {
	/**
	 * Unique descriptor for the FileConfigReader component
	 */
	public final static ComponentDescriptor Descriptor = new ComponentDescriptor(
		Category.Boot, "pip-services-runtime-boot", "file", "*"
	);

	private String _path;

	/**
	 * Creates an instance of file configuration reader component.
	 */
	public FileBootConfig() {
        super(Descriptor);
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

		DynamicMap options = config.getOptions();		
        if (options == null || options.hasNot("path"))
            throw new ConfigError(this, "NoPath", "Missing config file path");
		
        super.configure(config);
        
        _path = options.getString("path");
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
    	
        if (!new File(_path).exists())
        	throw new FileError(
    			this, 
    			"FileNotFound", 
    			"Config file was not found at "  + _path
			).withDetails(_path);

        super.open();
    }

	/**
	 * Reads microservice configuration from the source
	 * @return a microservice configuration
	 * @throws MicroserviceError when reading fails for any reason
	 */
    @Override
    public MicroserviceConfig readConfig() throws MicroserviceError {
    	return ConfigReader.read(_path);
    }
}
