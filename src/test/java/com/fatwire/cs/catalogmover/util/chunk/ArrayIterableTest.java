package com.fatwire.cs.catalogmover.util.chunk;

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
        assertEquals(2,count);

    }

}
