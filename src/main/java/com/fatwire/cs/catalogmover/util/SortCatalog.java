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

package com.fatwire.cs.catalogmover.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.catalogs.SortingRowIterable;
import com.fatwire.cs.catalogmover.catalogs.TableData;
import com.fatwire.cs.catalogmover.catalogs.TableExporter;
import com.fatwire.cs.catalogmover.catalogs.TableParser;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.RemoteCatalog;

public class SortCatalog {

    /**
     * @param args
     * @throws CatalogMoverException
     * @throws IOException
     */
    public static void main(String[] args) throws CatalogMoverException, IOException {

        // File c = new File("c:/temp/Support-tools-cs7/src/SiteCatalog.html");
        File c = new File("c:/temp/Support-tools-cs7/active/SiteCatalog.html");
        System.out.println(c.exists());
        FileReader reader = new FileReader(c);

        StringBuilder response = new StringBuilder();// read file
        int ch = 0;
        char[] ca = new char[2048];
        while ((ch = reader.read(ca)) != -1) {
            response.append(ca, 0, ch);
        }
        reader.close();
        RemoteCatalog catalog = new RemoteCatalog("SiteCatalog", new File("c:/temp/Support-tools-cs7/active"));
        System.out.println(response.toString());

        TableData tableData;
        tableData = new TableParser().parseHTML(response.toString());
        if (tableData == null) {
            // log.error(response);
            throw new CatalogMoverException("no tableData found");
        }
        Iterable<Row> rows;

        rows = new SortingRowIterable(tableData);
        StringBuffer b = new TableExporter(tableData).exportHTML(rows, tableData.getDatabaseType(), false);
        catalog.writeCatalog(b.toString());

    }
}
