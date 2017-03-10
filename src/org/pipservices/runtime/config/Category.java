package org.pipservices.runtime.config;

/**
 * Category of components or configuration sections that are used to configure components.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class Category {
	/**
	 * Undefined category
	 */
	public static final String Undefined = "undefined";
	
	/**
	 * Component factories
	 */
	public static final String Factories = "factories";
	
	/**
	 * Service discovery components
	 */
	public static final String Discovery = "discovery";
	
	/**
	 * Bootstrap configuration readers
	 */
	public static final String Boot = "boot";
	
	/**
	 * Logging components
	 */
	public static final String Logs = "logs";
	
	/**
	 * Performance counters
	 */
	public static final String Counters = "counters";
	
	/**
	 * Value caches
	 */
	public static final String Cache = "cache";
	
	/**
	 * Persistence components
	 */
	public static final String Persistence = "persistence";
	
	/**
	 * Clients to other microservices or infrastructure services
	 */
	public static final String Clients = "clients";
	
	/**
	 * Any business logic component - controller or decorator
	 */
	public static final String BusinessLogic = "logic";

	/**
	 * Business logic controllers
	 */
	public static final String Controllers = "controllers";
	
	/**
	 * Decorators to business logic controllers
	 */
	public static final String Decorators = "decorators";
	
	/**
	 * API services
	 */
	public static final String Services = "services";
	
	/**
	 * Various microservice addons / extension components
	 */
	public static final String Addons = "addons";
}
