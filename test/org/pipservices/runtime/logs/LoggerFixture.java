package org.pipservices.runtime.logs;

import static org.junit.Assert.*;

import org.pipservices.runtime.*;

public class LoggerFixture {
    private ILogger _logger;

    public LoggerFixture(ILogger logger) {
        _logger = logger;
    }

    public void testLogLevel() {
        assertTrue(_logger.getLevel() >= LogLevel.None);
        assertTrue(_logger.getLevel() <= LogLevel.Trace);
    }

    public void testTextOutput() {
        _logger.log(LogLevel.Fatal, "ABC", "123", new Object[] { "Fatal error..." });
        _logger.log(LogLevel.Error, "ABC", "123", new Object[] { "Recoverable error..."});
        _logger.log(LogLevel.Warn, "ABC", "123", new Object[] { "Warning..."});
        _logger.log(LogLevel.Info, "ABC", "123", new Object[] { "Information message..."});
        _logger.log(LogLevel.Debug, "ABC", "123", new Object[] { "Debug message..."});
        _logger.log(LogLevel.Trace, "ABC", "123", new Object[] { "Trace message..."});
    }

    public class TestClass {
    	public String abc = "ABC";
    }
    
    public void testMixedOutput() {
    	TestClass obj = new TestClass();
    	
    	_logger.log(LogLevel.Fatal, "ABC", "123", new Object[] { 123, "ABC", obj });
    	_logger.log(LogLevel.Error, "ABC", "123", new Object[] { 123, "ABC", obj });
    	_logger.log(LogLevel.Warn, "ABC", "123", new Object[] { 123, "ABC", obj });
    	_logger.log(LogLevel.Info, "ABC", "123", new Object[] { 123, "ABC", obj });
    	_logger.log(LogLevel.Debug, "ABC", "123", new Object[] { 123, "ABC", obj });
    	_logger.log(LogLevel.Trace, "ABC", "123", new Object[] { 123, "ABC", obj });
    }
}