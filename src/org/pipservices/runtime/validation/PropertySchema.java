package org.pipservices.runtime.validation;

import java.util.*;

/**
 * Represents a validation schema for object property.
 * The schema can use simple types like: "string", "number", "object", "DummyObject"
 * or specific schemas for object values
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-13
 */
public class PropertySchema {
	private String _name;
	private boolean _array;
	private boolean _optional;
	private String _type;
	private Schema _schema;
	private List<IPropertyValidationRule> _rules = new ArrayList<IPropertyValidationRule>();

	/**
	 *  Creates instance of the object property schema defined by a simple type
	 * @param name the name of the property
	 * @param array the array flag
	 * @param type the simple value type
	 * @param optional the optional flag
	 * @param rules a list of validation rules
	 */
	public PropertySchema(String name, boolean array, String type, boolean optional, IPropertyValidationRule[] rules) {
		_name = name;
		_array = array;
		_optional = optional;
		_type = type;
		
		if (rules != null) {
			for (IPropertyValidationRule rule : rules)
				_rules.add(rule);
		}
	}
	
	/**
	 *  Creates instance of the object property schema defined by complex schema
	 * @param name the name of the property
	 * @param array the array flag
	 * @param schema the value type schema
	 * @param optional the optional flag
	 * @param rules a list of validation rules
	 */
	public PropertySchema(String name, boolean array, Schema schema, boolean optional, IPropertyValidationRule[] rules) {
		_name = name;
		_array = array;
		_optional = optional;
		_schema = schema;

		if (rules != null) {
			for (IPropertyValidationRule rule : rules)
				_rules.add(rule);
		}
	}
	
	/***
	 * Gets the property name
	 * @return the name of the property
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Gets the property array flag
	 * @return <b>true</b> if the property is array and <b>false</b> if it is a simple value
	 */
	public boolean isArray() {
		return _array;
	}

	/**
	 * Gets the property optional flag (opposite to required)
	 * @return <b>true</b> if the property optional and <b>false</b> if it is required
	 */
	public boolean isOptional() {
		return _optional;
	}

	/**
	 * Gets the simple type describing property value
	 * @return a simple value type: 'int', 'float', 'number', 'string', 'boolean', 'string', ...
	 */
	public String getType() {
		return _type;
	}

	/**
	 * Gets the complex property schema describing property value
	 * @return a schema object
	 */
	public Schema getSchema() {
		return _schema;
	}
	
	/**
	 * Gets a list of validation rules associated with this property
	 * @return a list of validation rules
	 */
	public List<IPropertyValidationRule> getRules() {
		return _rules;
	}
}
