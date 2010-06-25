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

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ComplexObjectDiffTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private List<MockComplexObject> result(
            final Iterator<MockComplexObject> i1,
            final Iterator<MockComplexObject> i2) {
        final Comparator<MockComplexObject> comparator = new ComplexObjectComparator();

        final Iterator<MockComplexObject> itor = new DiffIterator<MockComplexObject>(
                i1, i2, comparator);
        final List<MockComplexObject> r = new LinkedList<MockComplexObject>();
        while (itor.hasNext()) {
            r.add(itor.next());
        }
        return r;
    }

    public void testDiff() {
        final List<MockComplexObject> l1 = new LinkedList<MockComplexObject>();
        l1.add(new MockComplexObject(1, 1));
        l1.add(new MockComplexObject(2, 2));
        final List<MockComplexObject> l2 = new LinkedList<MockComplexObject>();
        l2.add(new MockComplexObject(1, 1));
        l2.add(new MockComplexObject(2, 1));

        final List<MockComplexObject> result = result(l1.iterator(), l2
                .iterator());

        Assert.assertEquals(new MockComplexObject(2, 2), result.get(0));

    }

}
