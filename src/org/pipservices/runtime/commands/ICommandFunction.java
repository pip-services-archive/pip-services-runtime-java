package org.pipservices.runtime.commands;

import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

/**
 * Interface to use as a functional callback in Java to attach logic to command.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-12
 */
public interface ICommandFunction {
	Object execute(String correlationId, DynamicMap args) throws MicroserviceError;
}
