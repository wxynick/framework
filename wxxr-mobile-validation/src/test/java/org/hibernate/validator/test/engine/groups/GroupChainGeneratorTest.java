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

import static org.testng.Assert.assertEquals;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.engine.groups.Group;
import org.hibernate.validator.engine.groups.GroupChain;
import org.hibernate.validator.engine.groups.GroupChainGenerator;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.wxxr.javax.validation.GroupDefinitionException;
import com.wxxr.javax.validation.GroupSequence;
import com.wxxr.javax.validation.ValidationException;
import com.wxxr.javax.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
public class GroupChainGeneratorTest {

	GroupChainGenerator generator;

	@BeforeTest
	public void init() {
		generator = new GroupChainGenerator();
	}

	@Test(expectedExceptions = ValidationException.class)
	public void testGroupChainForNonInterface() {
		Set<Class<?>> groups = new HashSet<Class<?>>();
		groups.add( String.class );
		generator.getGroupChainFor( groups );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupChainForNull() {
		generator.getGroupChainFor( null );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testGroupChainForEmptySet() {
		generator.getGroupChainFor( new HashSet<Class<?>>() );
	}

	@Test(expectedExceptions = ValidationException.class)
	public void testCyclicGroupSequences() {
		Set<Class<?>> groups = new HashSet<Class<?>>();
		groups.add( CyclicGroupSequence1.class );
		generator.getGroupChainFor( groups );
	}

	@Test(expectedExceptions = ValidationException.class)
	public void testCyclicGroupSequence() {
		Set<Class<?>> groups = new HashSet<Class<?>>();
		groups.add( CyclicGroupSequence.class );
		generator.getGroupChainFor( groups );
	}

	@Test
	public void testGroupDuplicates() {
		Set<Class<?>> groups = new HashSet<Class<?>>();
		groups.add( First.class );
		groups.add( Second.class );
		groups.add( Last.class );
		GroupChain chain = generator.getGroupChainFor( groups );
		int count = countGroups( chain );
		assertEquals( count, 3, "Wrong number of groups" );

		groups.clear();
		groups.add( First.class );
		groups.add( First.class );
		chain = generator.getGroupChainFor( groups );
		count = countGroups( chain );
		assertEquals( count, 1, "Wrong number of groups" );

		groups.clear();
		groups.add( First.class );
		groups.add( Last.class );
		groups.add( First.class );
		chain = generator.getGroupChainFor( groups );
		count = countGroups( chain );
		assertEquals( count, 2, "Wrong number of groups" );
	}

	@Test(expectedExceptions = GroupDefinitionException.class)
	public void testGroupDefiningSequencePartOfGroupComposingSequence() {
		Set<Class<?>> groups = new HashSet<Class<?>>();
		groups.add( Sequence1.class );
		generator.getGroupChainFor( groups );
	}

	@Test(expectedExceptions = GroupDefinitionException.class)
	public void testUnexpandableSequence() {
		Set<Class<?>> groups = new HashSet<Class<?>>();
		groups.add( Sequence3.class );
		generator.getGroupChainFor( groups );
	}

	@Test
	public void testExpandableSequenceWithInheritance() {
		Set<Class<?>> groups = new HashSet<Class<?>>();
		groups.add( Sequence4.class );
		generator.getGroupChainFor( groups );
	}

	@Test
	public void testSequenceResolution() {
		Set<Class<?>> groups = new HashSet<Class<?>>();
		groups.add( Address.Complete.class );
		GroupChain chain = generator.getGroupChainFor( groups );
		Iterator<List<Group>> sequences = chain.getSequenceIterator();
		List<Group> sequence = sequences.next();

		assertEquals( sequence.get( 0 ).getGroup(), Default.class, "Wrong group" );
		assertEquals( sequence.get( 1 ).getGroup(), Address.HighLevelCoherence.class, "Wrong group" );
	}

	private int countGroups(GroupChain chain) {
		Iterator<Group> groupIterator = chain.getGroupIterator();
		int count = 0;
		while ( groupIterator.hasNext() ) {
			groupIterator.next();
			count++;
		}
		return count;
	}


	interface GroupA extends Default {
	}

	interface GroupB {
	}

	interface GroupC extends Sequence2 {
	}

	@GroupSequence({ GroupA.class, GroupC.class })
	interface Sequence1 {
	}

	@GroupSequence({ GroupB.class, GroupA.class })
	interface Sequence2 {
	}

	@GroupSequence({ Sequence2.class, GroupB.class })
	interface Sequence3 {
	}

	@GroupSequence({ Sequence2.class, GroupA.class })
	interface Sequence4 {
	}
}
