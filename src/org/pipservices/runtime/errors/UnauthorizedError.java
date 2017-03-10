package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Access errors caused by missing user identity or security permissions
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class UnauthorizedError extends MicroserviceError {
	private static final long serialVersionUID = 1728971490844757508L;

	public UnauthorizedError(String code, String message) {
		this(null, code, message);
	}

	public UnauthorizedError(IComponent component, String code, String message) {
		super(ErrorCategory.Unauthorized, component, code, message);
		this.setStatus(401);
	}
}
