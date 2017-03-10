package org.pipservices.runtime;

/**
 * Decorators are used to inject custom behavior into
 * existing microservice. They alter business logic before or after
 * execution or may override it entirely. 
 * The custom logic can make use of custom fields  
 * in persisted data or may call custom services.
 * 
 * @author Seroukhov Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public interface IDecorator extends IBusinessLogic {
}
