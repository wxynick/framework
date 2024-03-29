// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.validator.engine.resolver;

import java.lang.annotation.ElementType;

import com.wxxr.javax.validation.Path;
import com.wxxr.javax.validation.Path.Node;
import com.wxxr.javax.validation.TraversableResolver;

/**
 * A JPA 2 aware <code>TraversableResolver</code>.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class DefaultTraversableResolver implements TraversableResolver {

	  @Override
	  public boolean isCascadable(Object traversableObject,
	      Node traversableProperty, Class<?> rootBeanType,
	      Path pathToTraversableObject, ElementType elementType) {
	    return true;
	  }

	  @Override
	  public boolean isReachable(Object traversableObject,
	      Node traversableProperty, Class<?> rootBeanType,
	      Path pathToTraversableObject, ElementType elementType) {
	    return true;
	  }

}
