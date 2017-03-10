package org.pipservices.runtime.build;

import org.pipservices.runtime.boot.*;
import org.pipservices.runtime.cache.*;
import org.pipservices.runtime.counters.*;
import org.pipservices.runtime.logs.*;

/**
 * Component factory that contains registrations of standard runtime components.
 * This factory is typically used as a base for microservice factories.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class DefaultFactory extends ComponentFactory {
	/**
	 * The instance of default factory
	 */
	public final static DefaultFactory Instance = new DefaultFactory();
	
	/**
	 * Creates an instance of default factory with standard runtime components 
	 */
	public DefaultFactory() {
		register(NullLogger.Descriptor, NullLogger.class);
		register(ConsoleLogger.Descriptor, ConsoleLogger.class);
		register(NullCounters.Descriptor, NullCounters.class);
		register(LogCounters.Descriptor, LogCounters.class);
		register(NullCache.Descriptor, NullCache.class);
		register(MemoryCache.Descriptor, MemoryCache.class);
		register(FileBootConfig.Descriptor, FileBootConfig.class);
	}
}
