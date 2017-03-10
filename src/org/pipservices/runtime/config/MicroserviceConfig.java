package org.pipservices.runtime.config;

import java.util.*;

import org.pipservices.runtime.portability.*;

/**
 * Configuration for the entire microservice.
 * It can be either stored in JSON file on disk,
 * kept in remote configuration registry or hardcoded within test.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class MicroserviceConfig {

	private DynamicMap _content;
	
	/**
	 * Creates an empty instance of the microservice configuration.
	 * It can be filled with data manually or loaded from the file.
	 */
	public MicroserviceConfig() {
		_content = new DynamicMap();
	}
	
	/**
	 * Creates instance of the microservice configuration and 
	 * initializes it with data from dynamic map.
	 * @param content map with configuration parameters
	 */
	public MicroserviceConfig(DynamicMap content) {
		if (content == null)
			throw new NullPointerException("Content is not empty");
		_content = content;
	}

	/**
	 * Gets the raw content of the configuration as dynamic map
	 * @return dynamic map with all microservice configuration parameters.
	 */
	public DynamicMap getRawContent() {
		return _content;
	}

	/**
	 * Gets configurations of components for specific section.
	 * @param category a category that defines a section within microservice configuration
	 * @return an array with components configurations
	 */
	public List<ComponentConfig> getSection(String category) {
		List<ComponentConfig> configs = new ArrayList<ComponentConfig>();

		List<Object> values = _content.getArray(category);		
		for (Object value : values) {
			ComponentConfig config = new ComponentConfig(category, DynamicMap.fromValue(value));
			configs.add(config);
		}		
		return configs;
	}

	/**
	 * Removes specified sections from the configuration.
	 * This method can be used to suppress certain functionality in the microservice
	 * like api services when microservice runs inside Lambda function.
	 * @param categories a list of categories / section names to be removed.
	 */
	public void removeSections(String... categories) {
		for (String category : categories) {
			_content.remove(category);
		}
	}
	
	/**
	 * Creates microservice configuration using free-form objects.
	 * @param value a free-form object
	 * @return constructed microservice configuration
	 */
	public static MicroserviceConfig fromValue(Object value) {
        DynamicMap content = DynamicMap.fromValue(value);
        return new MicroserviceConfig(content);
	}

	/**
	 * Creates component configuration using hardcoded parameters.
	 * This method of configuration is usually used during testing.
	 * The configuration is created with 'Undefined' category
	 * since it's not used to create a component.
	 * @param tuples configuration parameters as <key> + <value> tuples
	 * @return constructed microservice configuration
	 */
	public static MicroserviceConfig fromTuples(Object... tuples) {
        DynamicMap content = DynamicMap.fromTuples(tuples);
        return new MicroserviceConfig(content);
	}
    
}
