package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Errors returned by remote services or network during call attempts
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class CallError extends MicroserviceError {
	private static final long serialVersionUID = 7516215539095097503L;

	public CallError(String code, String message) {
		this(null, code, message);
	}

	public CallError(IComponent component, String code, String message) {
		super(ErrorCategory.CallError, component, code, message);
		this.setStatus(500);
	}
}
