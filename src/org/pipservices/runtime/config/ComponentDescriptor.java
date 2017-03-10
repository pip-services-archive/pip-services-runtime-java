package org.pipservices.runtime.config;

/**
 * Component descriptor used to identify the component by descriptive elements:
 * <ul>
 * <li> logical group: typically microservice with or without transaction subgroup 'pip-services-storage:blocks'
 * <li> component category: 'controller', 'services' or 'cache'
 * <li> functional type: 'memory', 'file' or 'mongodb', ...
 * <li> implementation version: '1.0', '1.5' or '10.4'
 * </ul>
 * 
 * The descriptor also checks matching to another descriptor for component search.
 * '*' or null mean that element shall match to any value. 
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class ComponentDescriptor {
	private String _category;
	private String _group;
	private String _type;
	private String _version;
	
	/**
	 * Creates instance of a component descriptor
	 * @param category - component category: 'cache', 'services' or 'controllers' 
	 * @param group - logical group: 'pip-services-runtime', 'pip-services-logging'
	 * @param type - functional type: 'memory', 'file' or 'memcached' 
	 * @param version - implementation version: '1.0'. '1.5' or '10.4'
	 */
	public ComponentDescriptor(String category, String group, String type, String version) {
		if ("*".equals(category)) category = null;
		if ("*".equals(group)) group = null;
		if ("*".equals(type)) type = null;
		if ("*".equals(version)) version = null;
		
		_category = category;
		_group = group;
		_type = type;
		_version = version;
	}

	/**
	 * Gets a component category
	 * @return a component category
	 */
	public String getCategory() { 
		return _category; 
	}
	
	/**
	 * Gets a logical group
	 * @return a logical group
	 */
	public String getGroup() { 
		return _group; 
	}
	
	/**
	 * Gets a functional type
	 * @return a functional type
	 */
	public String getType() { 
		return _type; 
	}
	
	/**
	 * Gets an implementation version
	 * @return an implementation version
	 */
	public String getVersion() { 
		return _version; 
	}
	
	/**
	 * Matches this descriptor to another descriptor.
	 * All '*' or null descriptor elements match to any other value.
	 * Specific values must match exactly.
	 * 
	 * @param descriptor - another descriptor to match this one.
	 * @return <b>true</b> if descriptors match or <b>false</b> otherwise. 
	 */
	public boolean match(ComponentDescriptor descriptor) {
		if (_category != null && descriptor.getCategory() != null) {
			// Special processing if this category is business logic
			if (_category.equals(Category.BusinessLogic)) {
				if (!descriptor.getCategory().equals(Category.Controllers)
					&& !descriptor.getCategory().equals(Category.Decorators)
					&& !descriptor.getCategory().equals(Category.BusinessLogic))
					return false;
			}
			// Special processing is descriptor category is business logic
			else if (descriptor.getCategory().equals(Category.BusinessLogic)) {
				if (!_category.equals(Category.Controllers)
					&& !_category.equals(Category.Decorators)
					&& !_category.equals(Category.BusinessLogic))
					return false;
			}
			// Matching categories
			else if (!_category.equals(descriptor.getCategory())) {
				return false;
			}
		}
		
		// Matching groups
		if (_group != null && descriptor.getGroup() != null 
			&& !_group.equals(descriptor.getGroup())) {
			return false;
		}
		
		// Matching types
		if (_type != null && descriptor.getType() != null 
			&& !_type.equals(descriptor.getType())) {
			return false;
		}
		
		// Matching versions
		if (_version != null && descriptor.getVersion() != null 
			&& !_version.equals(descriptor.getVersion())) {
			return false;
		}
		
		// All checks are passed...
		return true;
	}
	
	@Override
	public boolean equals(Object value) {
		if (value instanceof ComponentDescriptor)
			return match((ComponentDescriptor)value);
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(_category != null ? _category : "*")
			.append("/").append(_group != null ? _group : "*")
			.append("/").append(_type != null ? _type : "*")
			.append("/").append(_version != null ? _version : "*");
		return builder.toString();
	}
}
