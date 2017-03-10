package org.pipservices.runtime.build;

import java.lang.reflect.*;
import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Factory for microservice components. It registers component classes,
 * locates classes by descriptors and creates component instances.
 * It also supports inheritance from other factories.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class ComponentFactory implements IComponentFactory {
	private List<FactoryRegistration> _registrations = new ArrayList<FactoryRegistration>();
	private List<IComponentFactory> _baseFactories = new ArrayList<IComponentFactory>();
	
	/**
	 * Creates an instance of component factory.
	 */
	public ComponentFactory() {}
	
	/**
	 * Creates an instance of component factory and extends it with base factories.
	 * @param baseFactories base factories to extend registrations of this factory.
	 */
	public ComponentFactory(IComponentFactory... baseFactories) {
		this.extend(baseFactories);
	}
	
	/**
	 * Extends this factory with base factories.
	 * @param baseFactories a list of base factories to extend registrations of this factory.
	 */
	public void extend(IComponentFactory... baseFactories) {
		for (IComponentFactory baseFactory : baseFactories) {
			_baseFactories.add(baseFactory);
		}
	}
	
	/**
	 * Registers a component class accompanies by component descriptor.
	 * @param descriptor a component descriptor to locate the class.
	 * @param classFactory a component class used to create a component.
	 * @return a reference to this factory to support chaining registrations.
	 */
	public IComponentFactory register(ComponentDescriptor descriptor, Class<?> classFactory) {
		if (descriptor == null)
			throw new NullPointerException("Descriptor cannot be null");
		if (classFactory == null)
			throw new NullPointerException("Class factory cannot be null");
		
		_registrations.add(new FactoryRegistration(descriptor, classFactory));		
		return this;
	}

	/**
	 * Lookups for component class by matching component descriptor.
	 * @param descriptor a component descriptor used to locate a class
	 * @return a located component class or <b>null</b> if component wasn't found.
	 */
	public Class<?> find(ComponentDescriptor descriptor) throws MicroserviceError {
		// Try to find a match in local registrations
		for (FactoryRegistration registration : _registrations) {
			if (registration.getDescriptor().match(descriptor))
				return registration.getClassFactory();
		}
		
		for (IComponentFactory baseFactory : _baseFactories) {
			Class<?> classFactory = baseFactory.find(descriptor);
			if (classFactory != null)
				return classFactory;
		}
		
		return null;
	}
	
	/**
	 * Create a component instance using class located by component descriptor.
	 * @param descriptor a component descriptor to locate a component class.
	 * @return a created component instance.
	 * @throws MicroserviceError when class to construct component wasn't found, 
	 * when constructor failed or component doesn't implements required interfaces.
	 */
	public IComponent create(ComponentDescriptor descriptor) throws MicroserviceError {
		Object component;
		
        try {
            // Create a component
        	Class<?> classFactory = find(descriptor);
        	
        	if (classFactory == null) {
        		throw new ConfigError(
    				"FactoryNotFound", 
    				"Factory for component " + descriptor + " was not found"
    			).withDetails(descriptor);
        	}
        	
        	Constructor<?> constructor = classFactory.getConstructor();
        	component = constructor.newInstance();
        } catch (Exception ex) {
        	throw new BuildError(
    			"CreateFailed",
    			"Failed to create component " + descriptor + ": " + ex
			)
    		.withDetails(descriptor)
    		.wrap(ex);
        }
        
        if (!(component instanceof IComponent)) {
        	throw new BuildError(
    			"BadComponent", 
    			"Component " + descriptor + " does not implement IComponent interface"
			).withDetails(descriptor);
        }
        
        return (IComponent)component;
	}
	
	/**
	 * Dynamically creates an instance of configuration factory based
	 * on configuration parameters. 
	 * @param config a configuration parameters to locate the factory class.
	 * @return a created factory instance
	 * @throws MicroserviceError when creation wasn't successful.
	 */
	public static IComponentFactory createFactory(DynamicMap config) throws MicroserviceError {
		// The code left here for future references
//    	// Shortcut if class if specified as a class function
//        Object typeRef = config.get("class");
//        
//        if (typeRef instanceof Class<?>) {
//        	Class<?> type = (Class<?>)typeRef;
//        	return instantiateComponent(id, type, config);
//        }
//
//        // Get and check class/type name
//        String typeName = "" + typeRef;
//        if (typeName.isEmpty())
//            throw new ConfigError("NoCustomClass", id + " custom class is not configured");
//
//        // Get and check component module name
//        String moduleName = config.getNullableString("jar");
//        moduleName = moduleName != null ? moduleName : config.getNullableString("assembly");
//        moduleName = moduleName != null ? moduleName : config.getNullableString("module");
//        moduleName = moduleName != null ? moduleName : config.getNullableString("library");
//
//        try {
//	        // Load module
//	        if (moduleName != null && !moduleName.isEmpty()) {
//	        	URL moduleUrl = new File(moduleName).toURI().toURL();
//	        	URLClassLoader child = new URLClassLoader(
//        			new URL[] { moduleUrl }, this.getClass().getClassLoader());
//	            Class<?> type = Class.forName(typeName, true, child);	        	
//	            return instantiateComponent(id, type, config);
//	        } else {	        
//		        // Create a component
//		        Class<?> type = Class.forName(typeName);		
//		        return instantiateComponent(id, type, config);
//	        }
//        } catch (Exception ex) {
//	        // Check component class function
//            throw new ConfigError("WrongCustomType", id + " type is not correctly configured")
//            	.withDetails(typeName, moduleName).withCause(ex);	
//        }
        
        throw new UnsupportedError("NotImplemented", "Loading of custom factories it not supported yet");
	}
}
