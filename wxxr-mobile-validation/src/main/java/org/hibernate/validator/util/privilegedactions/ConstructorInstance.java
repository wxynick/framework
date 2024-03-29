/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and/or its affiliates, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// $Id$

package org.hibernate.validator.util.privilegedactions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivilegedAction;

import com.wxxr.javax.validation.ValidationException;

/**
 * Execute instance creation as privileged action.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class ConstructorInstance<T> implements PrivilegedAction<T> {
	private final Constructor<T> constructor;
	private final Object[] initArgs;

	public static <T> ConstructorInstance<T> action(Constructor<T> constructor, Object... initArgs) {
		return new ConstructorInstance<T>( constructor, initArgs );
	}

	private ConstructorInstance(Constructor<T> constructor, Object... initArgs) {
		this.constructor = constructor;
		this.initArgs = initArgs;
	}

	public T run() {
		try {
			return constructor.newInstance( initArgs );
		}
		catch ( InstantiationException e ) {
			throw new ValidationException( "Unable to instantiate" + constructor.getName(), e );
		}
		catch ( IllegalAccessException e ) {
			throw new ValidationException( "Unable to instantiate" + constructor.getName(), e );
		}
		catch ( InvocationTargetException e ) {
			throw new ValidationException( "Unable to instantiate" + constructor.getName(), e );
		}
		catch ( RuntimeException e ) {
			throw new ValidationException( "Unable to instantiate" + constructor.getName(), e );
		}
	}
}