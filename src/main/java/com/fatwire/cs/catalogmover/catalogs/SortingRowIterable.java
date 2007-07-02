package com.fatwire.cs.catalogmover.catalogs;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class SortingRowIterable implements Iterable<Row> {

    private final Set<Row> sorted;

    public SortingRowIterable(final Iterable<Row> delegate) {
        this(delegate, new RowComparator());
        for (Row row : delegate) {
            sorted.add(row);
        }
    }

    public SortingRowIterable(final Iterable<Row> delegate,
            Comparator<Row> comparator) {
        sorted = new TreeSet<Row>(comparator);
        for (Row row : delegate) {
            sorted.add(row);
        }
    }

    public Iterator<Row> iterator() {

        return sorted.iterator();
    }

}
