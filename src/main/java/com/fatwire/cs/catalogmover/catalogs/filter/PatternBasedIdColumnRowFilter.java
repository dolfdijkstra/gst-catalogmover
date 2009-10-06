package com.fatwire.cs.catalogmover.catalogs.filter;

import java.util.regex.Pattern;

import com.fatwire.cs.catalogmover.catalogs.Row;

public class PatternBasedIdColumnRowFilter implements Filter<Row> {

    final Pattern pattern;

    /**
     * @param columnName
     * @param pattern
     */
    public PatternBasedIdColumnRowFilter(final Pattern pattern) {
        super();
        this.pattern = pattern;
    }

    public boolean matches(final Row row) {
        String data = row.getData(0);
        if (data == null)
            return false;

        return pattern.matcher(data).matches();
    }

}
