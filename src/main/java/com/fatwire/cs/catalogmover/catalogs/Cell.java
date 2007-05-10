package com.fatwire.cs.catalogmover.catalogs;

public class Cell {

    private int row;

    private Header column;

    private String cell;

    public Cell(final int row, final Header column, final String cell) {
        super();
        this.row = row;
        this.column = column;
        this.cell = cell;
    }

    public String getCell() {
        return cell;
    }

    public Header getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public String toString() {
        return "row: " + row + " column:" + column + " cell: " + cell;
    }

}

class Key implements Comparable<Key> {
    private final int row;

    private final int column;

    public Key(final int row, final int column) {
        super();
        this.row = row;
        this.column = column;
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + column;
        result = PRIME * result + row;
        return result;
    }

    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Key other = (Key) obj;
        if (column != other.column) {
            return false;
        }
        if (row != other.row) {
            return false;
        }
        return true;
    }

    public int compareTo(final Key o) {
        if ((column == o.column) && (row == o.row)) {
            return 0;
        }

        if (row < o.row) {
            return -1;
        }
        if ((row == o.row) && (column < o.column)) {
            return -1;
            //		if (column < o.column)
            //			return -1;
        }

        return 1;
    }

}