package com.fatwire.cs.catalogmover.util.chunk;

import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

public class ExceptionAdapterTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetStacktrace() {
        Exception e = new Exception("Test");
        try {
            try {
                throw e;
            } catch (Exception ex) {
                assertSame(e, ex);
                throw new ExceptionAdapter(ex);
            }
        } catch (RuntimeException re) {
            assertTrue(Arrays.equals(e.getStackTrace(), re.getStackTrace()));

        }

    }

    public void testRethrow() {
        Exception e = new IOException("Test");
        try {
            try {
                throw e;
            } catch (Exception ex) {
                assertEquals(e, ex);
                throw new ExceptionAdapter(ex);
            }
        } catch (ExceptionAdapter ea) {
            try {
                ea.rethrow();
            } catch (Exception rt) {
                assertSame(e, rt);
            }
        } catch (Exception re) {
            fail("not a ExceptionAdapter");

        }

    }
}
