/*
 * $Id$
 *
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
package org.hibernate.validator.test.engine.customerror;

import static org.hibernate.validator.test.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.validator.test.util.TestUtil.getValidator;

import java.util.Set;

import org.testng.annotations.Test;

import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.Validator;

/**
 * @author Hardy Ferentschik
 */
public class CustomErrorMessageTest {
	/**
	 * HV-297
	 *
	 * @throws Exception in case the test fails.
	 */
	@Test
	public void testReportAsSingleViolationDoesNotInfluenceCustomError() throws Exception {
		Validator validator = getValidator();
		DummyTestClass dummyTestClass = new DummyTestClass();

		Set<ConstraintViolation<DummyTestClass>> constraintViolations = validator.validate( dummyTestClass );
		assertCorrectConstraintViolationMessages( constraintViolations, IsValidValidator.message );
	}
}