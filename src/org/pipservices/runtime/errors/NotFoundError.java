package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Error caused by attempt to access missing object
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class NotFoundError extends MicroserviceError {
	private static final long serialVersionUID = -3296918665715724164L;

	public NotFoundError(String code, String message) {
		this(null, code, message);
	}

	public NotFoundError(IComponent component, String code, String message) {
		super(ErrorCategory.NotFound, component, code, message);
		this.setStatus(404);
	}
}
