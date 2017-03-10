package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Errors raised by conflict in object versions
 * posted by user and stored on server.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class ConflictError extends MicroserviceError {
	private static final long serialVersionUID = -3421059253211761993L;

	public ConflictError(String code, String message) {
		this(null, code, message);
	}

	public ConflictError(IComponent component, String code, String message) {
		super(ErrorCategory.Conflict, component, code, message);
		this.setStatus(409);
	}
}
