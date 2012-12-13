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
package org.apache.wicket;

import java.io.Serializable;

import org.apache.wicket.util.string.Strings;

/**
 * A POJO that represents the device currently being used to access the application. This will be
 * utilized to determine which version of the resources should be loaded.<br/>
 * <br/>
 * The Device's key will be used as the prefix to the normal extension, as such:<br/>
 * <ul>
 * 1. name[style][locale][.device-key].[extension] <br>
 * 2. name[style][locale].[extension] <br>
 * 3. name[locale][.device-key].[extension] <br>
 * 4. name[locale].[extension] <br>
 * 5. name[style][.device-key].[extension] <br>
 * 6. name[style].[extension] <br>
 * 7. name[.device-key].[extension] <br>
 * 8. name.[extension] <br>
 * <ul/>
 * <br/>
 * 
 * The fallback device specifies a device to be attempted if no resource is found for the current
 * device. For example if the current device is android, it may have a fallback device of mobile.
 * Thus if the component does not have an android markup file, it would still use the more generic
 * mobile markup before defaulting to the general markup.
 * 
 * @see org.apache.wicket.Session
 * @see org.apache.wicket.Session#getDevice()
 * 
 * @author David Phillips
 */
public class Device implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String key;
	private Device fallbackDevice;

	/**
	 * Constructor.
	 * 
	 * @param key
	 *            The Device's key used for building the dynamic extensions when loading resources
	 * @param fallbackDevice
	 *		The Device which will be used if a resource is not found for this key 
	 */
	public Device(String key, Device fallbackDevice)
	{
		super();

		if (Strings.isEmpty(key))
		{
			throw new WicketRuntimeException("Null or empty device keys are not allowed.");
		}

		this.key = key;
		this.fallbackDevice = fallbackDevice;
	}

	/**
	 * Get the key of this Device.
	 * 
	 * @return The key of this Device
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * Get the fallback device of this Device.
	 * 
	 * @return The fallback device of this Device
	 */
	public Device getFallbackDevice()
	{
		return fallbackDevice;
	}
}
