/**
 * 
 */
package com.fatwire.cs.catalogmover.catalogs;

public class Header {
    private final int column;

    private final String name;

    private final String schema;

    private final int type;

    Header(final int column, final String name, final String schema,
            final int type) {
        this.column = column;
        this.name = name;
        this.schema = schema;
        this.type = type;
    }

    public int getColumn() {
        return column;
    }

    public String getName() {
        return name;
    }

    public String getSchema() {
        return schema;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "name: " + name + " schema: " + schema + " type: " + type;
    }
}