package com.fatwire.cs.catalogmover.catalogs.filter;

import java.util.Iterator;

import com.fatwire.cs.catalogmover.catalogs.Row;

public class FilteringIterable implements Iterable<Row> {
    private final Iterable<Row> delegate;

    final RowFilter[] includeFilters;

    final RowFilter[] excludeFilters;

    public FilteringIterable(final Iterable<Row> delegate,
            final RowFilter[] includeFilters, final RowFilter[] excludeFilters) {
        super();
        this.delegate = delegate;
        this.includeFilters = includeFilters == null ? new RowFilter[0]
                : includeFilters;
        this.excludeFilters = excludeFilters == null ? new RowFilter[0]
                : excludeFilters;
    }

    public Iterator<Row> iterator() {
        final Iterator<Row> delegateIterator = delegate.iterator();
        return new Iterator<Row>() {
            {
                proceedToNext();
            }

            private Row next = null;

            public boolean hasNext() {
                return next != null;
            }

            public Row next() {
                final Row r = next;
                proceedToNext();
                return r;
            }

            public void remove() {
                assert false : "Unsupported operation remove()";

            }

            private void proceedToNext() {

                next = null;
                while (delegateIterator.hasNext() && next == null) {
                    final Row r = delegateIterator.next();
                    next = match(r) ? r : null;

                }
            }

            /**
             * 
             * @param row
             * @return true if this row does not match a exclude filter, or true if there are no includeFilters or if it matches an include filter pattern.
             */
            private boolean match(final Row row) {
                for (RowFilter filter : excludeFilters) {
                    if (filter.matches(row)) {
                        return false;
                    }
                }
                //if there are no include filters all rows match
                if (includeFilters.length == 0) {
                    return true;
                }
                for (RowFilter filter : includeFilters) {
                    if (filter.matches(row)) {
                        return true;
                    }
                }
                return false;

            }

        };
    }

}
