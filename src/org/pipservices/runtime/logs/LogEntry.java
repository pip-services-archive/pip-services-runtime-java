package org.pipservices.runtime.logs;

import java.time.*;
import com.fasterxml.jackson.annotation.*;

public class LogEntry {
	private ZonedDateTime _time;
	private String _component;
	private int _level;
	private String _correlationId;
	private Object[] _message;
	
    public LogEntry() { }

    public LogEntry(int level, String component, String correlationId, Object... message) {
    	_time = ZonedDateTime.now(ZoneId.of("Z"));
        _level = level;
        _component = component;
        _correlationId = correlationId;
        _message = message;
    }

    @JsonProperty("time")
    public ZonedDateTime getTime() { return _time; }
    public void setTime(ZonedDateTime value) { _time = value; }

    @JsonProperty("component")
    public String getComponent() { return _component; }
    public void setComponent(String value) { _component = value; }

    @JsonProperty("level")
    public int getLevel() { return _level; }
    public void setLevel(int value) { _level = value; }

    @JsonProperty("correlationId")
    public String getCorrelationId() { return _correlationId; }
    public void setCorrelationId(String value) { _correlationId = value; }

    @JsonProperty("message")
    public Object[] getMessage() { return _message; }
    public void setMessage(Object[] value) { _message = value; }
}
