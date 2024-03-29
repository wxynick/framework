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
package org.hibernate.validator.metadata;

import com.wxxr.javax.validation.metadata.PropertyDescriptor;

/**
 * Describe a validated element (class, field or property).
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class PropertyDescriptorImpl extends ElementDescriptorImpl implements PropertyDescriptor {
	private final boolean cascaded;
	private final String property;


	public PropertyDescriptorImpl(Class<?> returnType, boolean cascaded, String property, BeanMetaData<?> beanMetaData) {
		super( returnType, beanMetaData );
		this.cascaded = cascaded;
		this.property = property;
	}

	public boolean isCascaded() {
		return cascaded;
	}

	public String getPropertyName() {
		return property;
	}
}
