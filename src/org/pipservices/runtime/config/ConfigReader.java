package org.pipservices.runtime.config;

import java.io.*;
import java.util.*;

import org.pipservices.runtime.errors.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ConfigReader {
	private static ObjectMapper jsonMapper = new ObjectMapper();
	private static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
	private static TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};

	/**
	 * Reads microservice configuration from JSON file on disk.
	 * @param path a path to configuration file.
	 * @return MicroserviceConfig with file content
	 * @throws MicroserviceError when reading fails.
	 */
    public static MicroserviceConfig read(String path) throws MicroserviceError {
    	if (path == null)
        	throw new ConfigError("NoPath", "Missing config file path");
    	
        int index = path.lastIndexOf('.');
        String ext = index > 0 ? path.substring(index + 1).toLowerCase() : "";
        
        if (ext.equals("json"))
        	return readJson(path);
        else if (ext.equals("yaml"))
        	return readYaml(path);
        
        // By default read as JSON
        return readJson(path);
    }

	/**
	 * Reads microservice configuration from JSON file on disk.
	 * @param path a path to configuration file.
	 * @return MicroserviceConfig with file content
	 * @throws MicroserviceError when reading fails.
	 */
    public static MicroserviceConfig readJson(String path) throws MicroserviceError {
    	if (path == null)
        	throw new ConfigError("NoPath", "Missing config file path");
    	
        try {
            File file = new File(path); 
        	Map<String, Object> map = jsonMapper.readValue(file, typeRef);
        	return MicroserviceConfig.fromValue(map);
        } catch (Exception ex) {
        	throw new FileError(
    			"ReadFailed", 
    			"Failed reading configuration " + path + ": " + ex
			)
        	.withDetails(path)
    		.withCause(ex);
        }
    }

    /**
	 * Reads microservice configuration from JSON file on disk.
	 * @param path a path to configuration file.
	 * @return MicroserviceConfig with file content
	 * @throws MicroserviceError when reading fails.
	 */
    public static MicroserviceConfig readYaml(String path) throws MicroserviceError {
    	if (path == null)
        	throw new ConfigError("NoPath", "Missing config file path");
    	
        try {
            File file = new File(path); 
        	Map<String, Object> map = yamlMapper.readValue(file, typeRef);
        	return MicroserviceConfig.fromValue(map);
        } catch (Exception ex) {
        	throw new FileError(
    			"ReadFailed", 
    			"Failed reading configuration " + path + ": " + ex
			)
        	.withDetails(path)
    		.withCause(ex);
        }
    }

}
