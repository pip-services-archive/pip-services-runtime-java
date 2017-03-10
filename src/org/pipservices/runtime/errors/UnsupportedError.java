package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Errors caused by calls to unsupported 
 * or not yet implemented functionality
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-17
 */
public class UnsupportedError extends MicroserviceError {
	private static final long serialVersionUID = -8650683748145033352L;

	public UnsupportedError(String code, String message) {
		this(null, code, message);
	}

	public UnsupportedError(IComponent component, String code, String message) {
		super(ErrorCategory.Unsupported, component, code, message);
		this.setStatus(500);
	}
}
