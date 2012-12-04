/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.request.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.util.Locale;

import org.apache.wicket.WicketTestCase;
import org.apache.wicket.request.resource.ResourceReference.Key;
import org.junit.Test;

/**
 * Test Cases for the {@link ResourceReference.Key} class.
 * 
 * @author David Phillips
 */
public class ResourceReferenceKeyTest extends WicketTestCase
{
	/**
	 * Test of the equals method in the ResourceReferenceKey
	 */
	@Test
	public void equalsTest()
	{
		Locale locale = new Locale("language");

		// Equality test between constructors with and without extension
		Key firstKey = new Key(ResourceReferenceKeyTest.class.getName(), "testName", locale,
			"testStyle", "testVariation");
		Key secondKey = new Key(ResourceReferenceKeyTest.class.getName(), "testName", locale,
			"testStyle", "testVariation", null);
		assertThat(firstKey, is(secondKey));

		// Equality test between the constructor without extension and the ResourceReference
		// constructor
		Key thirdKey = new Key(new ResourceReference(ResourceReferenceKeyTest.class, "testName",
			locale, "testStyle", "testVariation")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public IResource getResource()
			{
				return null;
			}
		});
		assertThat(firstKey, is(thirdKey));
		assertThat(secondKey, is(thirdKey));

		// Equality test for a key containing an extension
		Key fourthKey = new Key(ResourceReferenceKeyTest.class.getName(), "testName", locale,
			"testStyle", "testVariation", "html");
		assertThat(firstKey, is(not(fourthKey)));
		assertThat(secondKey, is(not(fourthKey)));

		// Equality test for two keys containing extensions, otherwise identical
		Key fifthKey = new Key(ResourceReferenceKeyTest.class.getName(), "testName", locale,
			"testStyle", "testVariation", "android.html");
		assertThat(fourthKey, is(not(fifthKey)));
	}
}
