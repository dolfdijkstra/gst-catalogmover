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
