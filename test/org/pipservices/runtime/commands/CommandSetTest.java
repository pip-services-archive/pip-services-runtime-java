package org.pipservices.runtime.commands;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.counters.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.logs.*;
import org.pipservices.runtime.portability.*;

public class CommandSetTest {
	
	private ICommand makeEchoCommand(String name) {
		return new Command(null, name, null, new ICommandFunction() {
			public Object execute(String correlationId, DynamicMap args) {
				return args.get("value");
			}
		});
	}
	
	@Test
	public void testCommands() throws MicroserviceError {
		CommandSet commands = new CommandSet();
		commands.addCommand(makeEchoCommand("command1"));
		commands.addCommand(makeEchoCommand("command2"));
		
		Object result = commands.execute("command1", null, DynamicMap.fromTuples("value", 123));
		assertEquals(123, result);
		
		result = commands.execute("command1", null, DynamicMap.fromTuples("value", "ABC"));
		assertEquals("ABC", result);
		
		result = commands.execute("command2", null, DynamicMap.fromTuples("value", 789));
		assertEquals(789, result);		
	}

	@Test
	public void testInterceptors() throws MicroserviceError {
		NullLogger log = new NullLogger();
		log.configure(new ComponentConfig());
		
		List<ILogger> loggers = new ArrayList<ILogger>();
		loggers.add(log);
		
		NullCounters counters = new NullCounters();
		counters.configure(new ComponentConfig());
		
		CommandSet commands = new CommandSet();		
		commands.addIntercepter(new TracingIntercepter(loggers, "Testing"));
		commands.addIntercepter(new TimingIntercepter(counters, "TestTime"));		
		commands.addCommand(makeEchoCommand("command1"));
		commands.addCommand(makeEchoCommand("command2"));
		
		Object result = commands.execute("command1", null, DynamicMap.fromTuples("value", 123));
		assertEquals(123, result);
		
		result = commands.execute("command1", null, DynamicMap.fromTuples("value", "ABC"));
		assertEquals("ABC", result);
		
		result = commands.execute("command2", null, DynamicMap.fromTuples("value", 789));
		assertEquals(789, result);		
	}
	
}
