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
package org.hibernate.validator.test.constraints;

import com.wxxr.javax.validation.ConstraintValidator;
import com.wxxr.javax.validation.ConstraintValidatorContext;

/**
 * @author Hardy Ferentschik
 */
public class CloneableConstraintValidator implements ConstraintValidator<org.hibernate.validator.test.constraints.Cloneable, java.lang.Cloneable> {

	public void initialize(org.hibernate.validator.test.constraints.Cloneable annotation) {
	}

	public boolean isValid(java.lang.Cloneable value, ConstraintValidatorContext constraintValidatorContext) {
		return true;
	}
}
