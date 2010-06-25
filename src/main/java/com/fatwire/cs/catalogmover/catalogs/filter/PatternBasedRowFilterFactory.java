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

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.fatwire.cs.catalogmover.catalogs.Row;

/**
 * created a list of PatternBasedRowFilters.
 *
 * @author Dolf Dijkstra
 * @since 6-jun-2007
 * @see PatternBasedRowFilter
 */
public class PatternBasedRowFilterFactory {
    private PatternBasedRowFilterFactory() {
    }

    /**
     *
     * PatternBasedRowFilterFactory.create(new
     * String[]{"elementname","OpenMarket/.*","elementname","fatwire/.*"});
     *
     * @param pairs
     *            a paired array of Strings. The first elemement of a pair holds
     *            the columnname, the second the pattern to check
     * @return a list of PatternBasedRowFilters
     */
    static public List<Filter<Row>> create(String[] pairs) {
        if (pairs.length % 2 != 0)
            throw new IllegalArgumentException("pairs need to have an even number of elements");
        final List<Filter<Row>> includeList = new LinkedList<Filter<Row>>();
        for (int i = 0; i < pairs.length; i++) {
            includeList.add(new PatternBasedRowFilter(pairs[i], Pattern.compile(pairs[++i])));
        }
        return includeList;
    }
}
