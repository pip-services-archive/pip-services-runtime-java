package org.pipservices.runtime.addons;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;

/**
 * Abstract implementation for microservice addons.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-05-01
 */
public abstract class AbstractAddon extends AbstractComponent implements IAddon {
    /**
	 * Creates and initializes instance of the microservice addon
	 * @param descriptor the unique descriptor that is used to identify and locate the component.
	 */
	protected AbstractAddon(ComponentDescriptor descriptor) {
		super(descriptor);
	}	
}
