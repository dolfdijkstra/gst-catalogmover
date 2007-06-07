package com.fatwire.cs.catalogmover.catalogs;

import java.util.NoSuchElementException;

public class Row {

    private final int rowNum;

    private final TableData data;

    public Row(final TableData data, final int rowNum) {
        super();
        this.rowNum = rowNum;
        this.data = data;
    }

    public String getData(final int colNum) {
        return data.getCell(rowNum, colNum).getCell();
    }

    public String getData(final String headerName) {
        int col = -1;
        for (final Header header : data.getHeaders().values()) {
            if (headerName.equals(header.getName())) {
                col = header.getColumn();
                break;
            }
        }
        if (col == -1) {
            throw new NoSuchElementException();
        }
        return getData(col);
    }

    public int getNumberOfColumns() {
        return data.getHeaders().size();
    }

    public Header getHeader(final int colNum) {
        return data.getHeaders().get(colNum);
    }

    public int getRowNum() {
        return rowNum;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Row from " + data.getTableName() +":" + rowNum;
    }

}
