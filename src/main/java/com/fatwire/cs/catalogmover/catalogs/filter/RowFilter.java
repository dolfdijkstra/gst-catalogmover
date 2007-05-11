package com.fatwire.cs.catalogmover.catalogs.filter;

import com.fatwire.cs.catalogmover.catalogs.Row;

public interface RowFilter {

    boolean matches(Row row);

}
