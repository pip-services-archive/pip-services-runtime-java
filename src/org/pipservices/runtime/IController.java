package org.pipservices.runtime;

/**
 * Interface for microservice components that encapsulate
 * business logic. These components are the most valuable for
 * business and the key idea behind this framework is to protect
 * them from changes in persistence, communication or infrastructure
 * to ensure their long life.
 * 
 * @author Sergey Seroukhov
 * @version 1.1
 * @since 2016-06-09
 */
public interface IController extends IBusinessLogic {
}
