package com.fatwire.cs.catalogmover.catalogs;

import java.text.Collator;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class SortingRowIterable implements Iterable<Row> {

    final Set<Row> sorted = new TreeSet<Row>(new RowComparator());

    public SortingRowIterable(TableData delegate) {
        for (Row row : delegate) {
            sorted.add(row);
        }
    }

    public Iterator<Row> iterator() {

        return sorted.iterator();
    }

}

class RowComparator implements Comparator<Row> {
    private final Collator usCollator = Collator.getInstance(Locale.US);

    RowComparator() {
        usCollator.setStrength(Collator.PRIMARY);
    }

    /**
     * Compare on tableKey field
     */
    public int compare(Row row1, Row row2) {

        return usCollator.compare(row1.getData(0), row2.getData(0));
    }

}
