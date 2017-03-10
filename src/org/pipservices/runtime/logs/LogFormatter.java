package org.pipservices.runtime.logs;

import java.time.*;
import java.time.format.*;

import org.pipservices.runtime.*;

public class LogFormatter {

	public static String formatLevel(int level) {
		switch (level) {
			case LogLevel.Fatal: return "FATAL"; 
			case LogLevel.Error: return "ERROR"; 
			case LogLevel.Warn:  return "WARN"; 
			case LogLevel.Info:  return "INFO"; 
			case LogLevel.Debug: return "DEBUG"; 
			case LogLevel.Trace: return "TRACE";
			default: return "UNDEF";
		}
	}
	
	public static String formatMessage(Object[] message) {
		if (message == null || message.length == 0) return "";
		if (message.length == 1) return "" + message[0];
		
		String output = "" + message[0];
		
		for (int i = 1; i < message.length; i++)
			output += "," + message[i];
		
		return output;
	}

    public static String format(int level, Object[] message) {
        return ZonedDateTime.now(ZoneId.of("Z"))
        	.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            + " " + formatLevel(level) 
            + " " + formatMessage(message);
    }

}
