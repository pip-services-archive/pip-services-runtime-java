package org.pipservices.runtime;

import java.util.*;

import org.pipservices.runtime.config.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.errors.UnknownError;

/**
 * A list with references to all microservice components.
 * It is capable of searching and retrieving components by specified criteria.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class ComponentSet {
	private List<IComponent> _components = new ArrayList<IComponent>();
	
	/**
	 * Creates an empty component list
	 */
	public ComponentSet() {}

	/**
	 * Creates a component list and fills with component references
	 * from another list.
	 * @param components a list of components to add to this list.
	 */
	public ComponentSet(List<? extends IComponent> components) {
		for (IComponent component : components) {
			_components.add(component);
		}
	}

	/**
	 * Creates a component list and fills with component references
	 * from component array.
	 * @param components an array of components to add to this list.
	 */
	public ComponentSet(IComponent[] components) {
		for (IComponent component : components) {
			_components.add(component);
		}
	}

	/**
	 * Adds a single component to the list
	 * @param component a component to be added to the list
	 */
	public void add(IComponent component) {
		_components.add(component);
	}

	/**
	 * Adds multiple components to the list
	 * @param components a list of components to be added.
	 */
	public void addAll(List<? extends IComponent> components) {
		for (IComponent component : components) {
			_components.add(component);
		}
	}
	
	/**
	 * Internal utility method to fill a list with components from a specific category.
	 * @param components a component list where found components shall be added
	 * @param category a category to pick components.
	 * @return a reference to the component list for chaining.
	 */
	private List<IComponent> addByCategory(List<IComponent> components, String category) {
		for (IComponent component : _components) {
			if (component.getDescriptor().getCategory().equals(category))
				components.add(component);
		}
		return components;
	}

	/**
	 * Gets a sublist of component references from specific category.
	 * @param category a category to pick components.
	 * @return a list of found components
	 */
	public List<IComponent> getAllByCategory(String category) {
		return addByCategory(new ArrayList<IComponent>(), category);
	}

	/**
	 * Get a list of components in random order.
	 * Since it doesn't perform additional calculations
	 * this operation is faster then getting ordered list. 
	 * @return an unsorted list of components.
	 */
	public List<IComponent> getAllUnordered() {
		return _components;
	}
	
	/**
	 * Gets a list with all component references sorted in strict 
	 * initialization order: Discovery, Logs, Counters, Cache, Persistence, Controller, ...
	 * This sorting order it require to lifecycle management to proper sequencing. 
	 * @return a sorted list of components
	 */
	public List<IComponent> getAllOrdered() {
		List<IComponent> result = new ArrayList<IComponent>();
		addByCategory(result, Category.Discovery);
		addByCategory(result, Category.Boot);
		addByCategory(result, Category.Logs);
		addByCategory(result, Category.Counters);
		addByCategory(result, Category.Cache);
		addByCategory(result, Category.Persistence);
		addByCategory(result, Category.Clients);
		addByCategory(result, Category.Controllers);
		addByCategory(result, Category.Decorators);
		addByCategory(result, Category.Services);
		addByCategory(result, Category.Addons);
		return result;
	}

	/**
	 * Finds all components that match specified descriptor.
	 * The descriptor is used to specify number of search criteria
	 * or their combinations:
	 * <ul>
	 * <li> By category
	 * <li> By logical group
	 * <li> By functional type
	 * <li> By implementation version
	 * </ul>
	 * @param descriptor a component descriptor as a search criteria
	 * @return a list with found components
	 */
	public List<IComponent> getAllOptional(ComponentDescriptor descriptor) {
		if (descriptor == null)
			throw new NullPointerException("Descriptor is not set");

		List<IComponent> result = new ArrayList<IComponent>();
		// Search from the end to account for decorators
		for (int i = _components.size() - 1; i >= 0; i--) {
			IComponent component = _components.get(i);
			if (component.getDescriptor().match(descriptor))
				result.add(component);
		}
		return result;
	}

	/**
	 * Finds the a single component instance (the first one)
	 * that matches to the specified descriptor. 
	 * The descriptor is used to specify number of search criteria
	 * or their combinations:
	 * <ul>
	 * <li> By category
	 * <li> By logical group
	 * <li> By functional type
	 * <li> By implementation version
	 * </ul>
	 * @param descriptor a component descriptor as a search criteria
	 * @return a found component instance or <b>null</b> if nothing was found.
	 */
	public IComponent getOneOptional(ComponentDescriptor descriptor) {
		if (descriptor == null)
			throw new NullPointerException("Descriptor is not set");

		// Search from the end to account for decorators
		for (int i = _components.size() - 1; i >= 0; i--) {
			IComponent component = _components.get(i);
			if (component.getDescriptor().match(descriptor))
				return component;
		}
		return null;
	}
	
	/**
	 * Gets all components that match specified descriptor.
	 * If no components found it throws a configuration error.
	 * The descriptor is used to specify number of search criteria
	 * or their combinations:
	 * <ul>
	 * <li> By category
	 * <li> By logical group
	 * <li> By functional type
	 * <li> By implementation version
	 * </ul>
	 * @param descriptor a component descriptor as a search criteria
	 * @return a list with found components
	 * @throws MicroserviceError when no components found
	 */
	public List<IComponent> getAllRequired(ComponentDescriptor descriptor) throws MicroserviceError {
		if (descriptor == null)
			throw new NullPointerException("Descriptor is not set");

		List<IComponent> result = getAllOptional(descriptor);
		if (result.size() == 0) {
			throw new ConfigError(
				"NoDependency", 
				"At least one component " + descriptor + " must be present to satisfy dependencies"
			).withDetails(descriptor);
		}
		return result;
	}

	/**
	 * Gets a component instance that matches the specified descriptor.
	 * If nothing is found it throws a configuration error.
	 * The descriptor is used to specify number of search criteria
	 * or their combinations:
	 * <ul>
	 * <li> By category
	 * <li> By logical group
	 * <li> By functional type
	 * <li> By implementation version
	 * </ul>
	 * @param descriptor a component descriptor as a search criteria
	 * @return a found component instance
	 * @throws MicroserviceError when no components found
	 */
	public IComponent getOneRequired(ComponentDescriptor descriptor) throws MicroserviceError {
		if (descriptor == null)
			throw new NullPointerException("Descriptor is not set");

		IComponent result = getOneOptional(descriptor);
		if (result == null) {
			throw new ConfigError(
				"NoDependency", 
				"Component " + descriptor + " must be present to satisfy dependencies"
			).withDetails(descriptor);
		}
		return result;
	}

	/**
	 * Gets a component instance that matches the specified descriptor defined
	 * <b>before</b> specified instance. If nothing is found it throws a configuration error.
	 * This method is used primarily to find dependencies between business logic components
	 * in their logical chain. The sequence goes in order as components were configured. 
	 * The descriptor is used to specify number of search criteria
	 * or their combinations:
	 * <ul>
	 * <li> By category
	 * <li> By logical group
	 * <li> By functional type
	 * <li> By implementation version
	 * </ul>
	 * For instance, quite often the descriptor will look as "logic / group / * / *"
	 * @param component a component that searches for prior dependencies
	 * @param descriptor a component descriptor as a search criteria
	 * @return a found component instance
	 * @throws MicroserviceError when no components found
	 */
	public IComponent getOnePrior(IComponent component, ComponentDescriptor descriptor) 
		throws MicroserviceError {

		if (descriptor == null)
			throw new NullPointerException("Descriptor is not set");

		int index = _components.indexOf(component);
		if (index < 0) {
			throw new UnknownError(
				"ComponentNotFound", 
				"Current component " + component + " was not found in the component list"
			);
		}
		
		// Search down from the current component
		for (int i = index - 1; i >= 0; i--) {
			IComponent thisComponent = _components.get(i);
			if (thisComponent.getDescriptor().match(descriptor))
				return thisComponent;
		}

		// Throw exception if nothing was found
		throw new ConfigError(
			"NoDependency", 
			"Compoment " + descriptor + " must be present to satisfy dependencies"
		).withDetails(descriptor);
	}

	/**
	 * Creates a component list from components passed as params
	 * @param components a list of components
	 * @return constructed component set
	 */
	public static ComponentSet fromComponents(IComponent... components) {
		ComponentSet result = new ComponentSet();
		for (IComponent component : components) {
			result.add(component);
		}
		return result;
	}
	
}
