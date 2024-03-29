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
package org.hibernate.validator;

import org.hibernate.validator.engine.ConfigurationImpl;
import org.hibernate.validator.engine.ValidatorFactoryImpl;

import com.wxxr.javax.validation.Configuration;
import com.wxxr.javax.validation.ValidatorFactory;
import com.wxxr.javax.validation.spi.BootstrapState;
import com.wxxr.javax.validation.spi.ConfigurationState;
import com.wxxr.javax.validation.spi.ValidationProvider;

/**
 * Default implementation of {@code ValidationProvider} within Hibernate Validator.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class HibernateValidator implements ValidationProvider<HibernateValidatorConfiguration> {

	public HibernateValidatorConfiguration createSpecializedConfiguration(BootstrapState state) {
		return HibernateValidatorConfiguration.class.cast( new ConfigurationImpl( this ) );
	}

	public Configuration<?> createGenericConfiguration(BootstrapState state) {
		return new ConfigurationImpl( state );
	}

	public ValidatorFactory buildValidatorFactory(ConfigurationState configurationState) {
		return new ValidatorFactoryImpl( configurationState );
	}
}
