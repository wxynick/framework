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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;

import com.wxxr.javax.validation.Constraint;
import com.wxxr.javax.validation.ConstraintValidator;
import com.wxxr.javax.validation.ConstraintValidatorContext;
import com.wxxr.javax.validation.Payload;

/**
 * A test constraint which can lead to a error when trying to reslove the validator.
 *
 * @author Hardy Ferentschik
 */
@Constraint(validatedBy = {
		PostCodeList.PostCodeListValidatorForString.class, PostCodeList.PostCodeListValidatorForNumber.class
})
@Documented
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
public @interface PostCodeList {
	public abstract String message() default "foobar";

	public abstract Class<?>[] groups() default { };

	public abstract Class<? extends Payload>[] payload() default {};

	public class PostCodeListValidatorForNumber
			implements ConstraintValidator<PostCodeList, Collection<? extends Number>> {
		public void initialize(PostCodeList constraintAnnotation) {
		}

		public boolean isValid(Collection<? extends Number> value, ConstraintValidatorContext constraintValidatorContext) {
			return true;
		}
	}

	public class PostCodeListValidatorForString implements ConstraintValidator<PostCodeList, Collection<String>> {
		public void initialize(PostCodeList constraintAnnotation) {
		}

		public boolean isValid(Collection<String> value, ConstraintValidatorContext constraintValidatorContext) {
			if ( value == null ) {
				return true;
			}
			return false;
		}
	}
}
