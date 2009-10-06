package com.fatwire.cs.catalogmover.diff;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class DiffTest extends TestCase {
    final Comparator<String> comparator = new Comparator<String>() {
        Collator collator = Collator.getInstance();

        public int compare(String o1, String o2) {

            return collator.compare(o1, o2);
        }
    };

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private List<String> result(Iterator<String> i1, Iterator<String> i2) {
        final Iterator<String> itor = new DiffIterator<String>(i1, i2, comparator);
        final List<String> r = new ArrayList<String>();
        while (itor.hasNext()) {
            r.add(itor.next());
        }
        return r;
    }

    public void testDiff() {
        final List<String> l1 = Arrays.asList("a,b,c".split(","));
        final List<String> l2 = Arrays.asList("a".split(","));

        List result = result(l1.iterator(), l2.iterator());
        assertEquals(Arrays.asList("b,c".split(",")), result);

    }

    public void testDiff2() {
        final List<String> l1 = Arrays.asList("a,b,c".split(","));
        final List<String> l2 = Arrays.asList("a,c".split(","));

        List result = result(l1.iterator(), l2.iterator());
        assertEquals(Arrays.asList("b".split(",")), result);

    }

    public void testDiff3() {
        final List<String> l1 = Arrays.asList("a,b,c".split(","));
        final List<String> l2 = Arrays.asList("b".split(","));

        List result = result(l1.iterator(), l2.iterator());
        assertEquals(Arrays.asList("a,c".split(",")), result);

    }

    public void testDiff4() {
        final List<String> l1 = Arrays.asList("a,b,c,d,e,f,g,h".split(","));
        final List<String> l2 = Arrays.asList("a,b,c,e,f,g,h".split(","));

        List result = result(l1.iterator(), l2.iterator());
        assertEquals(Arrays.asList("d".split(",")), result);

    }

    public void testDiff5() {
        final List<String> l1 = Arrays.asList("a,b,c,d,e,f,g,h".split(","));
        final List<String> l2 = Arrays.asList("a,b,c,e,f,g,h".split(","));

        List result = result(l1.iterator(), l2.iterator());
        assertEquals(Arrays.asList("d".split(",")), result);

    }
    public void testDiff6() {
        final List<String> l1 = Arrays.asList("a,b,c,g,h".split(","));
        final List<String> l2 = Arrays.asList("a,b,c,e,f,h".split(","));

        List result = result(l1.iterator(), l2.iterator());
        assertEquals(Arrays.asList("g".split(",")), result);

    }
    public void testDiff7() {
        final List<String> l1 = Arrays.asList("b,c,g,h".split(","));
        final List<String> l2 = Arrays.asList("a,b,c,e,f,h".split(","));

        List result = result(l1.iterator(), l2.iterator());
        assertEquals(Arrays.asList("g".split(",")), result);

    }

}
