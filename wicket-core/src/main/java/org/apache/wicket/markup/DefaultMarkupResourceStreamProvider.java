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

import java.util.Locale;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Device;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Session;
import org.apache.wicket.core.util.resource.locator.IResourceStreamLocator;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Wicket default implementation for loading the markup resource stream associated with a
 * MarkupContainer.
 * 
 * @author Jonathan Locke
 * @author Juergen Donnerstag
 */
public class DefaultMarkupResourceStreamProvider implements IMarkupResourceStreamProvider
{

	/** Log for reporting. */
	private static final Logger log = LoggerFactory.getLogger(DefaultMarkupResourceStreamProvider.class);

	/**
	 * Constructor.
	 */
	public DefaultMarkupResourceStreamProvider()
	{
	}

	/**
	 * Create a new markup resource stream for the container.
	 * <p>
	 * Note: it will only called once, the IResourceStream will be cached by MarkupCache.
	 * <p>
	 * Note: IResourceStreamLocators should be used in case the strategy to find a markup resource
	 * should be extended for ALL components of your application.
	 * 
	 * @see org.apache.wicket.core.util.resource.locator.IResourceStreamLocator
	 * @see org.apache.wicket.markup.DefaultMarkupResourceStreamProvider
	 * 
	 * @param container
	 * @param containerClass
	 *            The container the markup should be associated with
	 * @return A IResourceStream if the resource was found
	 */
	@Override
	public IResourceStream getMarkupResourceStream(final MarkupContainer container,
		Class<?> containerClass)
	{
		// Get locator to search for the resource
		final IResourceStreamLocator locator = Application.get()
			.getResourceSettings()
			.getResourceStreamLocator();

		String style = container.getStyle();
		String variation = container.getVariation();
		Locale locale = container.getLocale();

		MarkupType markupType = container.getMarkupType();
		String ext = (markupType != null ? markupType.getExtension() : null);

		if ("html".equals(ext))
		{
			ext = getFullExtension(container, ext);
		}

		// Markup is associated with the containers class. Walk up the class
		// hierarchy up to MarkupContainer to find the containers markup
		// resource.
		while (containerClass != MarkupContainer.class)
		{
			String path = containerClass.getName().replace('.', '/');
			IResourceStream resourceStream = locator.locate(container.getClass(), path, style,
				variation, locale, ext, false);

			// Did we find it already?
			if (resourceStream != null)
			{
				return new MarkupResourceStream(resourceStream, new ContainerInfo(container),
					containerClass);
			}

			// Walk up the class hierarchy one level, if markup has not
			// yet been found
			containerClass = containerClass.getSuperclass();
		}

		return null;
	}

	/**
	 * This provides an additional method of adaptation within markup files. In addition to having
	 * multiple version based upon locale and style, this will add multiple extension possibilities
	 * based upon the device currently being used.<br/>
	 * <br/>
	 * The determination for the current device is made it two places.
	 * <ul>
	 * <li>{@link Component#getDevice()} will return the current device as specified by an
	 * individual component.</li>
	 * <li>{@link Session#getDevice()} will return the current device as specified by the entire
	 * session.</li>
	 * </ul>
	 * <br/>
	 * The Device returned will also include a fallback device (e.g. android might fall back to
	 * mobile). The Device and its fallbacks will be built into a comma seperated list of all the
	 * extensions.<br/>
	 * 
	 * This is made public to be accessible by various caching mechanisms.
	 * 
	 * @param container
	 *            The MarkupContainer instance which is currently locating its markup
	 * @param extension
	 *            The default extension
	 * 
	 * @return A comma seperated list of all possible extensions, ordered from the most specific to
	 *         the least.
	 */
	public static String getFullExtension(MarkupContainer container, String extension)
	{
		StringBuilder deviceExts = new StringBuilder();
		deviceExts.append(extension);

		Device device = null;

		if (container != null)
		{
			// Get any component specific device
			device = container.getDevice();
		}

		// If no component specific device, retrieve the device from session
		if (device == null)
		{
			device = Session.get().getDevice();
		}

		if (device != null)
		{
			deviceExts = new StringBuilder();

			// add specific device name extension
			deviceExts.append(device.getKey()).append(".").append(extension).append(",");

			// add all fallback device extensions
			while (device.getFallbackDevice() != null)
			{
				device = device.getFallbackDevice();
				deviceExts.append(device.getKey()).append(".").append(extension).append(",");
			}

			// add the default extension
			deviceExts.append(extension);
		}

		return deviceExts.toString();
	}
}
