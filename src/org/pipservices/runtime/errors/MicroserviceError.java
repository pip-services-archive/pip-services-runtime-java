package org.pipservices.runtime.errors;

import org.apache.commons.lang3.ArrayUtils;
import org.pipservices.runtime.*;

import com.fasterxml.jackson.annotation.*;

/**
 * Base class for all errors thrown by microservice implementation
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class MicroserviceError extends Exception {
	private static final long serialVersionUID = -5846403471784245155L;

	private String _category;
	private String _component;
	private String _code = "Undefined";
	private int _status = 500;
	private Object[] _details;
	private String _correlationId;
	
	public MicroserviceError() {
		this(ErrorCategory.UnknownError, (String)null, null, null);
	}

	public MicroserviceError(String category, IComponent component, String code, String message) {
		this(category, component != null ? component.getDescriptor().toString() : null, code, message);
	}

	public MicroserviceError(String category, String component, String code, String message) {
		super(message != null ? message : "Unknown error");
		_code = code != null ? code : "Undefined";
		_category = category != null ? category : ErrorCategory.UnknownError;
		_component = component;
	}
	
    @JsonProperty("category")
	public String getCategory() { return _category; }
	public void setCategory(String value) { _category = value; }
	
    @JsonProperty("component")
	public String getComponent() { return _component; }	
	public void setComponent(String value) { _component = value; }
	
    @JsonProperty("code")
	public String getCode() { return _code; }
    public void setCode(String value) { _code = value; }
    
    @JsonProperty("status")
	public int getStatus() { return _status; }
    public void setStatus(int value) { _status = value; }

    @JsonProperty("details")
    public Object[] getDetails() { return _details; }
    public void setDetails(Object[] value) { _details = value; }
    
    @JsonProperty("correlation_id")
    public String getCorrelationId() { return _correlationId; }
    public void setCorrelationId(String value) { _correlationId = value; }
	
	public MicroserviceError forComponent(String component) {
		_component = component;
		return this;
	}
	
	public MicroserviceError withCode(String code) {
		_code = code != null ? code : "Undefined";
		return this;
	}
	
	public MicroserviceError withStatus(int status) {
		_status = status;
		return this;
	}
	
	public MicroserviceError withDetails(Object... details) {
		_details = _details != null 
			? ArrayUtils.addAll(_details, details)
			: details;
		return this;
	}
	
	public MicroserviceError withCause(Throwable cause) {
		super.initCause(cause);
		return this;
	}
	
	public MicroserviceError withCorrelationId(String correlationId) {
		_correlationId = correlationId;
		return this;
	}

	public MicroserviceError wrap(Throwable cause) {
		if (cause instanceof MicroserviceError) 
			return (MicroserviceError)cause;

		this.withCause(cause);
		return this;
	}

	public static MicroserviceError wrap(MicroserviceError error, Throwable cause) {
		if (cause instanceof MicroserviceError) 
			return (MicroserviceError)cause;

		error.withCause(cause);
		return error;
	}
	
}
