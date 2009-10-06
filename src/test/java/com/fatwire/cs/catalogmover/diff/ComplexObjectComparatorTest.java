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
