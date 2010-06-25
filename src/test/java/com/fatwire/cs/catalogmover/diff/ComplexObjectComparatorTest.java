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

package com.fatwire.cs.catalogmover.diff;

import junit.framework.TestCase;

public class ComplexObjectComparatorTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCompare() {
        ComplexObjectComparator complexObjectComparator = new ComplexObjectComparator();
        assertEquals(1,complexObjectComparator.compare(new MockComplexObject(1,1),new MockComplexObject(2,2)));

    }
    public void testCompare2() {
        ComplexObjectComparator complexObjectComparator = new ComplexObjectComparator();
        assertEquals(1,complexObjectComparator.compare(new MockComplexObject(1,1),new MockComplexObject(2,1)));

    }
    public void testCompare3() {
        ComplexObjectComparator complexObjectComparator = new ComplexObjectComparator();
        assertEquals(1,complexObjectComparator.compare(new MockComplexObject(1,1),new MockComplexObject(1,2)));

    }

}
