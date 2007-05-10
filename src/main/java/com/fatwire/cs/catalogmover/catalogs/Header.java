/**
 * 
 */
package com.fatwire.cs.catalogmover.catalogs;

public class Header {
    private int column;

    private String header;

    private String schema;

    private int type;

    Header(final int column, final String header, final String schema,
            final int type) {
        this.column = column;
        this.header = header;
        this.schema = schema;
        this.type = type;
    }

    public int getColumn() {
        return column;
    }

    public String getHeader() {
        return header;
    }

    public String getSchema() {
        return schema;
    }

    public int getType() {
        return type;
    }

    public String toString() {
        return "header: " + header + " schema: " + schema + " type: " + type;
    }
}