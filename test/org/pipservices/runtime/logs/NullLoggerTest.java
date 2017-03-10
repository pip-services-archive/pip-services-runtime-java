package org.pipservices.runtime.logs;

import org.junit.*;
import org.pipservices.runtime.*;

public class NullLoggerTest {
	private ILogger log;
	private LoggerFixture fixture;
	
	@Before
	public void setUp() throws Exception {
		log = new NullLogger();
		fixture = new LoggerFixture(log);
	}
	
	@Test
	public void testLogLevel() {
		fixture.testLogLevel();
	}

	@Test
	public void testTextOutput() {
		fixture.testTextOutput();
	}

	@Test
	public void testMixedOutput() {
		fixture.testMixedOutput();
	}
}
