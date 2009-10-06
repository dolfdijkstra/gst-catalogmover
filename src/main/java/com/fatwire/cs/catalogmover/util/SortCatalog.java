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
    public static void main(String[] args) throws CatalogMoverException,
            IOException {

        //File c = new File("c:/temp/Support-tools-cs7/src/SiteCatalog.html");
        File c = new File("c:/temp/Support-tools-cs7/active/SiteCatalog.html");
        System.out.println(c.exists());
        FileReader reader = new FileReader(c);
        

        StringBuilder response = new StringBuilder();//read file
        int ch=0;
        char[] ca = new char[2048];
        while((ch=reader.read(ca))!=-1){
            response.append(ca,0,ch);
        }
        reader.close();
        RemoteCatalog catalog = new RemoteCatalog("SiteCatalog", new File(
                "c:/temp/Support-tools-cs7/active"));
        System.out.println(response.toString());

        TableData tableData;
        tableData = new TableParser().parseHTML(response.toString());
        if (tableData == null) {
            //log.error(response);
            throw new CatalogMoverException("no tableData found");
        }
        Iterable<Row> rows;

        rows = new SortingRowIterable(tableData);
        StringBuffer b = new TableExporter(tableData).exportHTML(rows,
                tableData.getDatabaseType(), false);
        catalog.writeCatalog(b.toString());

    }
}
