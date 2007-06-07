package com.fatwire.cs.catalogmover.catalogs.filter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FilteringIterable<T> implements Iterable<T> {
    private final Iterable<T> delegate;

    private List<Filter<T>> includeFilters = Collections.emptyList();

    private List<Filter<T>> excludeFilters = Collections.emptyList();;

    public FilteringIterable(final Iterable<T> delegate,
            final List<Filter<T>> includeFilters,
            final List<Filter<T>> excludeFilters) {
        super();
        this.delegate = delegate;

        if (includeFilters != null) {
            this.includeFilters = includeFilters;

        }
        if (excludeFilters != null) {
            this.excludeFilters = excludeFilters;
        }

    }

    public Iterator<T> iterator() {
        final Iterator<T> delegateIterator = delegate.iterator();
        return new Iterator<T>() {
            private T next = null;
            {
                proceedToNext();
            }

            

            public boolean hasNext() {
                return next != null;
            }

            public T next() {
                final T r = next;
                proceedToNext();
                return r;
            }

            public void remove() {
                assert false : "Unsupported operation remove()";

            }

            private void proceedToNext() {

                next = null;
                while (delegateIterator.hasNext() && next == null) {
                    final T r = delegateIterator.next();
                    next = match(r) ? r : null;

                }
            }

            /**
             * 
             * @param row
             * @return true if this row does not match an exclude filter, or true if there are no includeFilters or if it matches an include filter pattern.
             */
            private boolean match(final T row) {
                for (Filter<T> filter : excludeFilters) {
                    if (filter.matches(row)) {
                        return false;
                    }
                }
                //if there are no include filters all rows match
                if (includeFilters.isEmpty()) {
                    return true;
                }
                for (Filter<T> filter : includeFilters) {
                    if (filter.matches(row)) {
                        return true;
                    }
                }
                return false;

            }

        };
    }

}
