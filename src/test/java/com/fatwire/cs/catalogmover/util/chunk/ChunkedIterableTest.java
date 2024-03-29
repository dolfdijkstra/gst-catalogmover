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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.fatwire.cs.catalogmover.util.chunk.ChunkedIterable;

import junit.framework.TestCase;

public class ChunkedIterableTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testReIterate() {
        int chunckSize = 10;
        final Map<Integer, String> l = new TreeMap<Integer, String>();
        for (int i = 0; i < 15; i++) {
            l.put(i, String.valueOf(i));
        }
        int j = 0;
        int y = 0;
        for (final Iterable<Entry<Integer, String>> i : new ChunkedIterable<Entry<Integer, String>>(
                l.entrySet(), 10)) {
            for (@SuppressWarnings("unused") final Entry<Integer, String> u : i) {
                y++;
            }
            for (@SuppressWarnings("unused") final Entry<Integer, String> u : i) {
                y++;
            }

            j++;
        }
        assertEquals("Number of outer iterations is incorrect", l.size()
                / chunckSize, j);
        assertEquals("Number of total iterations is incorrect", l.size(), y);

    }

    public void testIterator() {
        int chunckSize = 10;
        final List<Integer> l = new LinkedList<Integer>();
        for (int i = 0; i < 100; i++) {
            l.add(i);
        }
        int j = 0;
        int y = 0;
        for (final Iterable<Integer> i : new ChunkedIterable<Integer>(l,
                chunckSize)) {
            for (@SuppressWarnings("unused") final Integer u : i) {
                y++;
            }
            j++;
        }
        assertEquals("Number of outer iterations is incorrect", chunckSize, j);
        assertEquals("Number of total iterations is incorrect", l.size(), y);
    }

    public void testDidNotConsume() {
        int chunckSize = 2;
        final List<Integer> l = new LinkedList<Integer>();
        for (int i = 0; i < 4; i++) {
            l.add(i);
        }

        try {
            for (@SuppressWarnings("unused") final Iterable<Integer> i : new ChunkedIterable<Integer>(l,
                    chunckSize)) {

            }
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }
    }

}
