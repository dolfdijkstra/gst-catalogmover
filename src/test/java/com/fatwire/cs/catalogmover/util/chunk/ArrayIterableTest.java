/*
 * Copyright 2007 FatWire Corporation. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fatwire.cs.catalogmover.util.chunk;

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

public class ArrayIterableTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testIterator() {
        String[] data = new String[] { "foo", "foo" };
        Iterable<String> i = new ArrayIterable<String>(data);
        int count = 0;
        for (String s : i) {
            count++;
            assertEquals("foo", s);
        }
        assertEquals(2, count);

    }

    public void testNoSuchElement() {
        String[] data = new String[] { "foo" };
        Iterator<String> i = new ArrayIterable<String>(data).iterator();
        i.next();
        boolean found = false;
        try {
            i.next();
        } catch (NoSuchElementException e) {
            found = true;
        }
        assertTrue(found);
    }

}
