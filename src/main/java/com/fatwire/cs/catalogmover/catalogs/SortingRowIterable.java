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

package com.fatwire.cs.catalogmover.catalogs;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class SortingRowIterable implements Iterable<Row> {

    private final Set<Row> sorted;

    public SortingRowIterable(final Iterable<Row> delegate) {
        this(delegate, new RowComparator());
    }

    public SortingRowIterable(final Iterable<Row> delegate, Comparator<Row> comparator) {
        sorted = new TreeSet<Row>(comparator);
        for (Row row : delegate) {
            sorted.add(row);
        }
    }

    public Iterator<Row> iterator() {

        return sorted.iterator();
    }

}
