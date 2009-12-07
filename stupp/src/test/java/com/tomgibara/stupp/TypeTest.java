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

import com.tomgibara.stupp.Stupp;
import com.tomgibara.stupp.StuppKey;
import com.tomgibara.stupp.StuppType;

import junit.framework.TestCase;

public class TypeTest extends TestCase {

	public void testCanonical() {
		StuppType type1 = StuppType.getInstance(Book.class);
		StuppType type2 = StuppType.getInstance(Book.class);
		assertSame(type1, type2);
		StuppType type3 = StuppType.getInstance(Author.class);
		assertNotSame(type1, type3);
	}
	
	public void testMultipleInterfaces() {
		StuppType baType = StuppType.getInstance(null, null, null, Book.class, Author.class);
		Object instance = baType.newInstance();
		((Book) instance).setName("Book Property");
		((Author) instance).setSurname("Author Property");
	}
	
	public void testPresentKey() {
		StuppType.getInstance(A.class);
	}
	
	public void testDuplicateKey() {
		try {
			StuppType.getInstance(B.class);
			fail();
		} catch (IllegalArgumentException e) {
			/* expected */
		}
	}
	
	public void testOverrideKey() {
		StuppType type = StuppType.getInstance(C.class, "id", Long.class);
		C instance = (C) type.newInstance();
		Stupp.setKey(instance, 1L);
		assertEquals(1L, instance.getId());
	}
	
	private static interface A {
		@StuppKey
		void setKey(String id);
	}
	
	private static interface B {
		@StuppKey
		void setKey(String key);
		@StuppKey
		void setId(long id);
	}
	
	private static interface C {
		@StuppKey
		void setKey(String key);
		void setId(long id);
		long getId();
	}
	
}
