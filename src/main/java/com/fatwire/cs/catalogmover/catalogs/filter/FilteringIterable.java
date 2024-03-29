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

package com.fatwire.cs.catalogmover.catalogs.filter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FilteringIterable<T> implements Iterable<T> {
    private final Iterable<T> delegate;

    private List<Filter<T>> includeFilters;

    private List<Filter<T>> excludeFilters;

    public FilteringIterable(final Iterable<T> delegate, final List<Filter<T>> includeFilters,
            final List<Filter<T>> excludeFilters) {
        super();
        this.delegate = delegate;

        this.includeFilters = includeFilters != null ? includeFilters : Collections.<Filter<T>> emptyList();

        this.excludeFilters = excludeFilters != null ? excludeFilters : Collections.<Filter<T>> emptyList();

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
             * @return true if this row does not match an exclude filter, or
             *         true if there are no includeFilters or if it matches an
             *         include filter pattern.
             */
            private boolean match(final T row) {
                for (Filter<T> filter : excludeFilters) {
                    if (filter.matches(row)) {
                        return false;
                    }
                }
                // if there are no include filters all rows match
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
