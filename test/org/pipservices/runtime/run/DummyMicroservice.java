package org.pipservices.runtime.run;

import org.pipservices.runtime.build.*;

/**
 * Dummy microservice class.
 * 
 * @author Sergey Seroukhov
 * @version 1.0
 * @since 2016-06-09
 */
public class DummyMicroservice extends Microservice {
	/**
	 * Creates instance of dummy microservice.
	 */
	public DummyMicroservice() {
		super("pip-services-dummies", DummyFactory.Instance);
	}
}
