package com.fatwire.cs.catalogmover.catalogs.filter;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.fatwire.cs.catalogmover.catalogs.Row;

/**
 * created a list of PatternBasedRowFilters.
 * @author Dolf.Dijkstra
 * @since 6-jun-2007
 * @see PatternBasedRowFilter
 */
public class PatternBasedRowFilterFactory {
    private PatternBasedRowFilterFactory() {
    }

    /**
     * 
     * PatternBasedRowFilterFactory.create(new String[]{"elementname","OpenMarket/.*","elementname","fatwire/.*"});
     * 
     * @param pairs a paired array of Strings. The first elemement of a pair holds the columnname, the second the pattern to check
     * @return a list of PatternBasedRowFilters
     */
    static public List<Filter<Row>> create(String[] pairs) {
        if (pairs.length % 2 != 0)
            throw new IllegalArgumentException(
                    "pairs need to have an even number of elements");
        final List<Filter<Row>> includeList = new LinkedList<Filter<Row>>();
        for (int i = 0; i < pairs.length; i++) {
            includeList.add(new PatternBasedRowFilter(pairs[i], Pattern
                    .compile(pairs[++i])));
        }
        return includeList;
    }
}
