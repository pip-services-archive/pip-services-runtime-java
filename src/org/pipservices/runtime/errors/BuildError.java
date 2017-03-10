package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Errors happened during microservice build process
 * and caused by problems in component factories
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class BuildError extends MicroserviceError {
	private static final long serialVersionUID = 3785086274181773728L;

	public BuildError(String code, String message) {
		this(null, code, message);
	}

	public BuildError(IComponent component, String code, String message) {
		super(ErrorCategory.BuildError, component, code, message);
		this.setStatus(500);
	}
}
