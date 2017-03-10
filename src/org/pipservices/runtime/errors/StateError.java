package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Errors related to operations called in wrong component state.
 * For instance, business calls when component is not ready
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class StateError extends MicroserviceError {
	private static final long serialVersionUID = 8713306897733892945L;

	public StateError(String code, String message) {
		this(null, code, message);
	}

	public StateError(IComponent component, String code, String message) {
		super(ErrorCategory.StateError, component, code, message);
		this.setStatus(500);
	}
}
