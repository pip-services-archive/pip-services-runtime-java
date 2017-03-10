package org.pipservices.runtime;

/**
 * Addons are microservice extensions that are not directly
 * participate in handling business transactions. 
 * They can do additional service functions, like randomly
 * shutting down component for resilience testing (chaos monkey),
 * register VM where microservice is running or collecting usage stats
 * from microservice deployments.
 *  
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public interface IAddon extends IComponent {
}
