package com.fatwire.cs.catalogmover.catalogs;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class SortingRowIterable implements Iterable<Row> {

    final Set<Row> sorted = new TreeSet<Row>(new RowComparator());

    public SortingRowIterable(final Iterable<Row> delegate) {

        for (Row row : delegate) {
            sorted.add(row);
        }
    }

    public Iterator<Row> iterator() {

        return sorted.iterator();
    }

}
