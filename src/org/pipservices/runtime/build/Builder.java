package org.pipservices.runtime.build;

import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;

/**
 * Builds microservice components using configuration as a build recipe.
 * 
 * @author Sergey Seroukhov
 * @version 2.0
 * @since 2016-09-10
 */
public class Builder {
	/**
	 * Builds default components for specified configuration section.
	 * @param factory a component factory that creates component instances.
	 * @param category a name of the section inside configuration.
	 * @param components a list with section components
	 * @return a list with section components for chaining
	 * @throws MicroserviceError when creation or configuration of components fails.
	 */
	private static List<IComponent> buildSectionDefaults(
		IComponentFactory factory, String category, List<IComponent> components
	) throws MicroserviceError {
		// Add null discovery by default
		if (category.equals(Category.Discovery) && components.size() == 0) {
			// Todo: complete implementation
		} 
		// Add null log by default
		else if (category.equals(Category.Logs) && components.size() == 0) {
			IComponent log = factory.create(new ComponentDescriptor(Category.Logs, null, "null", null));
			log.configure(new ComponentConfig());
			components.add(log);
		}
		// Add null counters by default
		else if (category.equals(Category.Counters) && components.size() == 0) {
			IComponent counters = factory.create(new ComponentDescriptor(Category.Counters, null, "null", null));
			counters.configure(new ComponentConfig());
			components.add(counters);
		}
		// Add null cache by default
		else if (category.equals(Category.Cache) && components.size() == 0) {
			IComponent cache = factory.create(new ComponentDescriptor(Category.Cache, null, "null", null));
			cache.configure(new ComponentConfig());
			components.add(cache);
		}
		return components;
	}

	/**
	 * Builds components from specific configuration section.
	 * @param factory a component factory that creates component instances.
	 * @param config a microservice configuration
	 * @param category a name of the section inside configuration.
	 * @return a list with created components
	 * @throws MicroserviceError when creation or configuration of components fails.
	 */
	public static List<IComponent> buildSection(
		IComponentFactory factory, MicroserviceConfig config, String category
	) throws MicroserviceError {		
		List<IComponent> components = new ArrayList<IComponent>();
		
		// Get specified configuration section
		List<ComponentConfig> componentConfigs = config.getSection(category);
		
	    // Go through configured components one by one
		for (ComponentConfig componentConfig : componentConfigs) {
			// Create component using component config
			ComponentDescriptor descriptor = componentConfig.getDescriptor();
			IComponent component = factory.create(descriptor);
			// Configure the created component
			component.configure(componentConfig);
			components.add(component);
		}
		
		// Add default components and return the result
		return buildSectionDefaults(factory, category, components);
    }
	
	/**
	 * Builds all microservice components according to configuration.
	 * @param factory a component factory that creates component instances.
	 * @param config a microservice configuration.
	 * @return a component list with all created microservice components
	 * @throws MicroserviceError when creation of configuration of components fails.
	 */
    public static ComponentSet build(
		IComponentFactory factory, MicroserviceConfig config
	) throws MicroserviceError {
    	if (factory == null)
    		throw new NullPointerException("Factory isn't set");
    	if (config == null)
    		throw new NullPointerException("Microservice config isn't set");
    	
    	// Create components section by section
    	ComponentSet components = new ComponentSet();
    	components.addAll(buildSection(factory, config, Category.Discovery));
    	components.addAll(buildSection(factory, config, Category.Logs));
    	components.addAll(buildSection(factory, config, Category.Counters));
    	components.addAll(buildSection(factory, config, Category.Cache));
    	components.addAll(buildSection(factory, config, Category.Clients));
    	components.addAll(buildSection(factory, config, Category.Persistence));
    	components.addAll(buildSection(factory, config, Category.Controllers));
    	components.addAll(buildSection(factory, config, Category.Decorators));
    	components.addAll(buildSection(factory, config, Category.Services));
    	components.addAll(buildSection(factory, config, Category.Addons));    	    	
        return components;
    }
    
}
