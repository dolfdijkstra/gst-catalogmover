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
