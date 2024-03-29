// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2008, Red Hat, Inc. and/or its affiliates, and individual contributors
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

import static org.hibernate.validator.test.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.validator.test.util.TestUtil.assertCorrectPropertyPaths;
import static org.hibernate.validator.test.util.TestUtil.assertNumberOfViolations;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.test.util.TestUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.wxxr.javax.validation.ConstraintViolation;
import com.wxxr.javax.validation.Valid;
import com.wxxr.javax.validation.Validator;
import com.wxxr.javax.validation.constraints.NotNull;

/**
 * HV-250
 */
public class ClassValidatorWithTypeVariableTest {

	private Validator validator;

	@BeforeClass
	public void setUp() {
		validator = TestUtil.getValidator();
	}

	@Test
	public void offersNull() {
		Batch batch = new Batch( null );

		Set<ConstraintViolation<Batch>> violations = validator.validate( batch );
		assertNumberOfViolations( violations, 1 );
		assertCorrectPropertyPaths( violations, "offers" );
		assertCorrectConstraintTypes( violations, NotNull.class );
	}

	@Test
	public void offerItemNull() {
		ItemAOffer offer = new ItemAOffer( null );
		Set<ItemOffer<? extends Item>> offers = new HashSet<ItemOffer<? extends Item>>();
		offers.add( offer );
		Batch batch = new Batch( offers );

		Set<ConstraintViolation<Batch>> violations = validator.validate( batch );
		assertNumberOfViolations( violations, 1 );
		assertCorrectPropertyPaths( violations, "offers[].item" );
		assertCorrectConstraintTypes( violations, NotNull.class );
	}

	@Test
	public void offerItemDateNull() {
		ItemA item = new ItemA( null );
		ItemOffer<? extends Item> offer = new ItemAOffer( item );
		Set<ItemOffer<? extends Item>> offers = new HashSet<ItemOffer<? extends Item>>();
		offers.add( offer );
		Batch batch = new Batch( offers );

		Set<ConstraintViolation<Batch>> violations = validator.validate( batch );
		assertNumberOfViolations( violations, 1 );
		assertCorrectPropertyPaths( violations, "offers[].item.date" );
		assertCorrectConstraintTypes( violations, NotNull.class );
	}

	private class Batch {
		@NotNull
		@Valid
		private Set<ItemOffer<? extends Item>> offers = new HashSet<ItemOffer<? extends Item>>();

		public Batch(Set<ItemOffer<? extends Item>> offers) {
			this.offers = offers;
		}
	}

	private abstract class Item {
		@NotNull
		private Date date;

		public Item(Date date) {
			this.date = date;
		}
	}

	private abstract class ItemOffer<T extends Item> {
		@NotNull
		@Valid
		private T item;

		public ItemOffer(T item) {
			this.item = item;
		}
	}

	private class ItemA extends Item {
		public ItemA(Date date) {
			super( date );
		}
	}

	private class ItemAOffer extends ItemOffer<ItemA> {
		public ItemAOffer(ItemA item) {
			super( item );
		}
	}
}


