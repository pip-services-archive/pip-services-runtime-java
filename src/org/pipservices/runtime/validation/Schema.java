package org.pipservices.runtime.validation;

import java.util.*;

/**
 * Represents a validation schema for complex objects.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class Schema {
	private List<PropertySchema> _properties = new ArrayList<PropertySchema>();
	private List<IValidationRule> _rules = new ArrayList<IValidationRule>();
	
	/**
	 * Creates an instance of validation schema
	 */
	public Schema() {}
	
	/**
	 * Gets a list of object properties
	 * @return a list of property validation schemas
	 */
	public List<PropertySchema> getProperties() {
		return _properties;
	}
	
	/**
	 * Gets a validation rules for entire object
	 * @return a list of validation rules
	 */
	public List<IValidationRule> getRules() {
		return _rules;
	}

	/**
	 * Adds to the validation schema a required property defined by a simple type.
	 * @param name a name of the property to be added
	 * @param type simple type that defines the property value
	 * @param rules a set of validation rules for the property
	 * @return a self reference to the schema for chaining
	 */
	public Schema withProperty(String name, String type, IPropertyValidationRule... rules) {
		_properties.add(new PropertySchema(name, false, type, false, rules));
		return this;
	}

	/**
	 * Adds to the validation schema a required property array defined by a simple type.
	 * @param name a name of the property to be added
	 * @param type simple type that defines the property value
	 * @param required a required flag
	 * @param rules a set of validation rules for the property
	 * @return a self reference to the schema for chaining
	 */
	public Schema withArray(String name, String type, IPropertyValidationRule... rules) {
		_properties.add(new PropertySchema(name, true, type, false, rules));
		return this;
	}

	/**
	 * Adds to the validation schema an optional property defined by a simple type.
	 * @param name a name of the property to be added
	 * @param type simple type that defines the property value
	 * @param rules a set of validation rules for the property
	 * @return a self reference to the schema for chaining
	 */
	public Schema withOptionalProperty(String name, String type, IPropertyValidationRule... rules) {
		_properties.add(new PropertySchema(name, false, type, true, rules));
		return this;
	}
	
	/**
	 * Adds to the validation schema an optional property array defined by a simple type.
	 * @param name a name of the property to be added
	 * @param type simple type that defines the property value
	 * @param rules a set of validation rules for the property
	 * @return a self reference to the schema for chaining
	 */
	public Schema withOptionalArray(String name, String type, IPropertyValidationRule... rules) {
		_properties.add(new PropertySchema(name, true, type, true, rules));
		return this;
	}
	
	/**
	 * Adds to the validation schema a required property defined by validation schema.
	 * @param name a name of the property to be added
	 * @param schema validation schema for the property value
	 * @param required a required flag
	 * @param rules a set of validation rules for the property
	 * @return a self reference to the schema for chaining
	 */
	public Schema withPropertySchema(String name, Schema schema, IPropertyValidationRule... rules) {
		_properties.add(new PropertySchema(name, false, schema, false, rules));
		return this;
	}

	/**
	 * Adds to the validation schema a required property array defined by validation schema.
	 * @param name a name of the property to be added
	 * @param schema validation schema for the property value
	 * @param required a required flag
	 * @param rules a set of validation rules for the property
	 * @return a self reference to the schema for chaining
	 */
	public Schema withArraySchema(String name, Schema schema, IPropertyValidationRule... rules) {
		_properties.add(new PropertySchema(name, true, schema, false, rules));
		return this;
	}

	/**
	 * Adds to the validation schema an optional property defined by validation schema.
	 * @param name a name of the property to be added
	 * @param schema validation schema for the property value
	 * @param rules a set of validation rules for the property
	 * @return a self reference to the schema for chaining
	 */
	public Schema withOptionalPropertySchema(String name, Schema schema, IPropertyValidationRule... rules) {
		_properties.add(new PropertySchema(name, false, schema, true, rules));
		return this;
	}

	/**
	 * Adds to the validation schema an optional property array defined by validation schema.
	 * @param name a name of the property to be added
	 * @param schema validation schema for the property value
	 * @param rules a set of validation rules for the property
	 * @return a self reference to the schema for chaining
	 */
	public Schema withOptionalArraySchema(String name, Schema schema, IPropertyValidationRule... rules) {
		_properties.add(new PropertySchema(name, true, schema, true, rules));
		return this;
	}

	/**
	 * Adds a validation rule to this scheme
	 * @param rule a validation rule to be added
	 * @return a self reference to the schema for chaining
	 */
	public Schema withRule(IValidationRule rule) {
		_rules.add(rule);
		return this;
	}
}
