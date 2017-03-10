package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Errors due to improper user requests, like
 * missing or wrong parameters 
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class BadRequestError extends MicroserviceError {
	private static final long serialVersionUID = -6858254084911710376L;

	public BadRequestError(String code, String message) {
		this(null, code, message);
	}

	public BadRequestError(IComponent component, String code, String message) {
		super(ErrorCategory.BadRequest, component, code, message);
		this.setStatus(400);
	}
}
