package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Internal errors caused by programming or unexpected errors
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class UnknownError extends MicroserviceError {
	private static final long serialVersionUID = -8513540232023043856L;

	public UnknownError(String code, String message) {
		this(null, code, message);
	}

	public UnknownError(IComponent component, String code, String message) {
		super(ErrorCategory.UnknownError, component, code, message);
		this.setStatus(500);
	}
}
