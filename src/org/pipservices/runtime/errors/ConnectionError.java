package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Errors happened during connection to remote services.
 * They can be related to misconfiguration, network issues
 * or remote service itself 
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class ConnectionError extends MicroserviceError {
	private static final long serialVersionUID = 5757636441830366775L;

	public ConnectionError(String code, String message) {
		this(null, code, message);
	}

	public ConnectionError(IComponent component, String code, String message) {
		super(ErrorCategory.ConnectionError, component, code, message);
		this.setStatus(500);
	}
}
