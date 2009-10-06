package com.fatwire.cs.catalogmover.diff;

import java.util.Comparator;

public class ComplexObjectComparator implements Comparator<MockComplexObject> {

    public int compare(MockComplexObject o1, MockComplexObject o2) {
        final long idc = o2.getId() - o1.getId();
        if (idc == 0) {
            return o2.getVersion() - o1.getVersion();
        } else {
            return idc < 0 ? -1 : 1;
        }
    }

}
