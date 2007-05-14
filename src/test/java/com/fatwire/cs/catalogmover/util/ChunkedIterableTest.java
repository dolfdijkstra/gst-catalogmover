package com.fatwire.cs.catalogmover.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

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
            for (final Entry<Integer, String> u : i) {
                y++;
            }
            for (final Entry<Integer, String> u : i) {
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
            for (final Integer u : i) {
                y++;
            }
            j++;
        }
        assertEquals("Number of outer iterations is incorrect", chunckSize, j);
        assertEquals("Number of total iterations is incorrect", l.size(), y);
    }
}
