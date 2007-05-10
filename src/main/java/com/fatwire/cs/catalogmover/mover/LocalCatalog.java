package com.fatwire.cs.catalogmover.mover;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.catalogs.TableData;
import com.fatwire.cs.catalogmover.catalogs.TableParser;

public class LocalCatalog {

    private final Log log = LogFactory.getLog(getClass());

    private TableData table;

    /**
     * absolute path to the Catalog.html file
     * 
     */
    private final File location;

    private File uploadPath;

    private String catalogName;

    public LocalCatalog(final File path) {
        super();

        if (!path.getName().endsWith(".html")) {
            throw new IllegalArgumentException(path
                    + " is not an Catalog.html file.");
        }
        location = path.getAbsoluteFile();
        log.debug("Catalog: " + location.getAbsolutePath());
        catalogName = path.getName().substring(0,
                path.getName().lastIndexOf('.'));

    }

    public void refresh() throws IOException {
        log.debug("Refreshing Catalog at " + location);

        final TableParser parser = new TableParser();

        table = parser.parseHTML(read());
        uploadPath = new File(location.getParentFile(), table.getTableName());
        log.debug("Catalog upload path: " + uploadPath.getAbsolutePath());

    }

    private String read() throws IOException {
        final FileReader reader = new FileReader(location);
        final StringBuilder builder = new StringBuilder();
        final char[] c = new char[2048];
        int i = 0;
        while ((i = reader.read(c)) != -1) {
            builder.append(c, 0, i);
        }
        return builder.toString();
    }

    public File getLocation() {
        return location;
    }

    public File getUploadPath() {
        return uploadPath;
    }

    public Iterable<Row> getRows() {
        return table;
    }

    public Iterable<Row> getRows1() {
        final int rowCount = 1;//table.getRowCount();

        final List<Row> rows = new ArrayList<Row>();
        for (int rowNum = 0; rowNum < rowCount; rowNum++) {

            rows.add(new Row(table, rowNum));

        }
        return rows;

    }

    public String getName() {

        return table.getTableName();
    }

    public String getTableKey() {

        return table.getTableKey();
    }

}
