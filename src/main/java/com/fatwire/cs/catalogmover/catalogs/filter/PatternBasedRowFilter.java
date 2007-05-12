package com.fatwire.cs.catalogmover.catalogs.filter;

import java.util.regex.Pattern;

import com.fatwire.cs.catalogmover.catalogs.Row;

public class PatternBasedRowFilter implements Filter<Row> {

    final String columnName;

    final Pattern pattern;

    /**
     * @param columnName
     * @param pattern
     */
    public PatternBasedRowFilter(final String columnName, final Pattern pattern) {
        super();
        this.columnName = columnName;
        this.pattern = pattern;
    }

    public boolean matches(final Row row) {
        String data = row.getData(columnName);
        if (data == null)
            return false;

        return pattern.matcher(data).matches();
    }

}
