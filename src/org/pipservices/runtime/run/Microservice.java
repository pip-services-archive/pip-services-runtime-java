package org.pipservices.runtime.run;

import org.pipservices.runtime.*;
import org.pipservices.runtime.build.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.logs.*;
import org.pipservices.runtime.portability.DynamicMap;

public class Microservice {
	private String _name;
    private IComponentFactory _factory;
    private MicroserviceConfig _config;
    private ComponentSet _components;
    private DynamicMap _context = new DynamicMap();
    
    public Microservice(String name, IComponentFactory factory) {
        _factory = factory;
    	_name = name;
    }

	public Microservice(IComponentFactory factory, MicroserviceConfig config) {
        _factory = factory;
        _config = config;
    }

	public String getName() {
		return _name;
	}
	
	public MicroserviceConfig getConfig() {
		return _config;
	}
	
    public void setConfig(MicroserviceConfig config) {
        _config = config;
    }

    public void loadConfig(String path) throws MicroserviceError{
    	_config = ConfigReader.read(path);
    }    
    
    public ComponentSet getComponents() {
    	return _components;
    }
    
    public DynamicMap getContext() {
    	return _context;
    }
    
    public void fatal(Object... message) {
    	LogWriter.fatal(
			_components.getAllByCategory(Category.Logs), 
			LogFormatter.format(LogLevel.Fatal, message)
		);
    }

    public void error(Object... message) {
    	LogWriter.error(
			_components.getAllByCategory(Category.Logs), 
			LogFormatter.format(LogLevel.Error, message)
		);
    }

    public void info(Object... message) {
    	LogWriter.info(
			_components.getAllByCategory(Category.Logs), 
			LogFormatter.format(LogLevel.Info, message)
		);
    }

    public void trace(Object... message) {
    	LogWriter.trace(
			_components.getAllByCategory(Category.Logs), 
			LogFormatter.format(LogLevel.Trace, message)
		);
    }

    private void build() throws MicroserviceError {
        _components = Builder.build(_factory, _config);
    }

    private void link() throws MicroserviceError {
        trace("Initializing " + _name + " microservice");
        LifeCycleManager.link(_context, _components);
    }

    private void open() throws MicroserviceError {
        trace("Opening " + _name + " microservice");
        LifeCycleManager.open(_components);
        info("Microservice " + _name + " started");
    }

    public void start() throws MicroserviceError {
        build();
        link();
        open();
    }

    public void startWithConfig(MicroserviceConfig config) throws MicroserviceError {
    	setConfig(config);
    	start();
    }

    public void startWithConfigFile(String configPath) throws MicroserviceError {
    	loadConfig(configPath);
    	start();
    }
    
    public void stop() throws MicroserviceError {
        trace("Closing " + _name + " microservice");
        LifeCycleManager.forceClose(_components);
        info("Microservice " + _name + " closed");
    }
}
