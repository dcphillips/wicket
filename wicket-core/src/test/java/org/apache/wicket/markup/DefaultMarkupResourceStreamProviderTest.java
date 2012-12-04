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
package org.apache.wicket.markup;

import org.apache.wicket.Device;
import org.apache.wicket.Session;
import org.apache.wicket.WicketTestCase;
import org.junit.Test;

/**
 * @author David Phillips
 */
public class DefaultMarkupResourceStreamProviderTest extends WicketTestCase
{
	/**
	 * Test the logic to build the full extension based upon Device
	 */
	@Test
	public void fullExtensionTest()
	{
		// No Device & no Component
		assertEquals("html", DefaultMarkupResourceStreamProvider.getFullExtension(null, "html"));

		DeviceFoo foo = new DeviceFoo("foo");
		// No Device & Component
		assertEquals("html", DefaultMarkupResourceStreamProvider.getFullExtension(foo, "html"));

		// Device with no fallback
		foo.device = new Device("android", null);
		assertEquals("android.html,html",
			DefaultMarkupResourceStreamProvider.getFullExtension(foo, "html"));

		// Device with a fallback
		foo.device = new Device("android", new Device("mobile", null));
		assertEquals("android.html,mobile.html,html",
			DefaultMarkupResourceStreamProvider.getFullExtension(foo, "html"));

		// Device from Session
		foo.device = null;
		Session.get().setDevice(new Device("android", null));
		assertEquals("android.html,html",
			DefaultMarkupResourceStreamProvider.getFullExtension(foo, "html"));
		assertEquals("android.html,html",
			DefaultMarkupResourceStreamProvider.getFullExtension(null, "html"));

		// Session Device with fallback
		Session.get().setDevice(new Device("android", new Device("mobile", null)));
		assertEquals("android.html,mobile.html,html",
			DefaultMarkupResourceStreamProvider.getFullExtension(foo, "html"));
		assertEquals("android.html,mobile.html,html",
			DefaultMarkupResourceStreamProvider.getFullExtension(null, "html"));

		// Device from Session and Component
		foo.device = new Device("iphone", null);
		assertEquals("iphone.html,html",
			DefaultMarkupResourceStreamProvider.getFullExtension(foo, "html"));

		// Cleanup
		Session.get().setDevice(null);
	}
}