/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jpa.repository.query;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.data.jpa.repository.query.StringQuery.LikeBinding;
import org.springframework.data.repository.query.parser.Part.Type;

/**
 * @author Oliver Gierke
 */
public class LikeBindingUnitTests {

	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullName() {
		new LikeBinding(null, Type.CONTAINING);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsEmptyName() {
		new LikeBinding("", Type.CONTAINING);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullType() {
		new LikeBinding("foo", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsInvalidType() {
		new LikeBinding("foo", Type.SIMPLE_PROPERTY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsInvalidPosition() {
		new LikeBinding(0, Type.CONTAINING);
	}

	@Test
	public void setsUpInstanceForName() {

		LikeBinding binding = new LikeBinding("foo", Type.CONTAINING);

		assertThat(binding.hasName("foo"), is(true));
		assertThat(binding.hasName("bar"), is(false));
		assertThat(binding.hasName(null), is(false));
		assertThat(binding.hasPosition(0), is(false));
		assertThat(binding.getType(), is(Type.CONTAINING));
	}

	@Test
	public void setsUpInstanceForIndex() {

		LikeBinding binding = new LikeBinding(1, Type.CONTAINING);

		assertThat(binding.hasName("foo"), is(false));
		assertThat(binding.hasName(null), is(false));
		assertThat(binding.hasPosition(0), is(false));
		assertThat(binding.hasPosition(1), is(true));
		assertThat(binding.getType(), is(Type.CONTAINING));
	}

	@Test
	public void augmentsValueCorrectly() {

		assertAugmentedValue(Type.CONTAINING, "%value%");
		assertAugmentedValue(Type.ENDING_WITH, "%value");
		assertAugmentedValue(Type.STARTING_WITH, "value%");

		assertThat(new LikeBinding(1, Type.CONTAINING).prepare(null), is(nullValue()));
	}

	private static void assertAugmentedValue(Type type, Object value) {

		LikeBinding binding = new LikeBinding("foo", type);
		assertThat(binding.prepare("value"), is(value));
	}
}
