package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Errors related to mistakes in microservice user-defined configuration
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class ConfigError extends MicroserviceError {
	private static final long serialVersionUID = 3832437788895163030L;

	public ConfigError(String code, String message) {
		this(null, code, message);
	}

	public ConfigError(IComponent component, String code, String message) {
		super(ErrorCategory.ConfigError, component, code, message);
		this.setStatus(500);
	}
}
