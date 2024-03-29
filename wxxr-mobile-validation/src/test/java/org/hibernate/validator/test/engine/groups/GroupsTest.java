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
package org.hibernate.validator.test.engine.groups;

import static org.hibernate.validator.test.util.TestUtil.assertCorrectConstraintViolationMessages;

import java.util.Set;

import org.hibernate.validator.test.util.TestUtil;
import org.testng.annotations.Test;

import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.Validator;

/**
 * @author Hardy Ferentschik
 */
public class GroupsTest {

	/**
	 * HV-288
	 */
	@Test
	public void testGroupInheritance() {
		Validator validator = TestUtil.getValidator();
		Try tryMe = new Try();
		tryMe.field2 = "foo";
		tryMe.field3 = "bar";

		Set<ConstraintViolation<Try>> violations = validator.validate( tryMe, Try.GlobalCheck.class );
		assertCorrectConstraintViolationMessages(violations, "field1");
	}
}