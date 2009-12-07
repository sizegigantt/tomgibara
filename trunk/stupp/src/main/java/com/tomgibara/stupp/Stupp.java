/*
 * Copyright 2009 Tom Gibara
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.tomgibara.stupp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Stupp {

	public static Object getKey(Object object) {
		return getHandler(object).getKey();
	}
	
	public static void setKey(Object object, Object value) {
		getHandler(object).setKey(value, true);
	}
	
	public static StuppType getType(Object object) {
		return getHandler(object).getType();
	}
	
	public static StuppScope getScope(Object object) {
		return getHandler(object).getScope();
	}
	
	static StuppHandler getHandler(Object object) {
		if (!Proxy.isProxyClass(object.getClass())) throw new IllegalArgumentException();
		InvocationHandler handler = Proxy.getInvocationHandler(object);
		if (!(handler instanceof StuppHandler)) throw new IllegalArgumentException();
		return (StuppHandler) handler;
		
	}
	
	static StuppHandler getHandlerOrNull(Object object) {
		if (object == null) return null;
		if (!Proxy.isProxyClass(object.getClass())) return null;
		InvocationHandler handler = Proxy.getInvocationHandler(object);
		if (!(handler instanceof StuppHandler)) return null;
		return (StuppHandler) handler;
	}
	
}
