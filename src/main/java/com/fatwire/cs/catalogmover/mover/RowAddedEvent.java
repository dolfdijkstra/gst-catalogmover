package com.fatwire.cs.catalogmover.mover;

import com.fatwire.cs.catalogmover.catalogs.Row;

public class RowAddedEvent extends CatalogMoverEvent {
    /**
     * 
     */
    private static final long serialVersionUID = -5068917560675278775L;

    private final Row row;

    public RowAddedEvent(final Object source, final Row row) {
        super(source);
        this.row = row;
    }

    /**
     * @return the row
     */
    public Row getRow() {
        return row;
    }

}
