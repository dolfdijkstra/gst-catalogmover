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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Row from " + data.getTableName() + ":" + rowNum;
    }

}
