package org.pipservices.runtime.errors;

import org.pipservices.runtime.*;

/**
 * Errors in read/write file operations
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class FileError extends MicroserviceError {
	private static final long serialVersionUID = 1330544660294516445L;

	public FileError(String code, String message) {
		this(null, code, message);
	}

	public FileError(IComponent component, String code, String message) {
		super(ErrorCategory.FileError, component, code, message);
		this.setStatus(500);
	}
}
