package com.fatwire.cs.catalogmover.mover;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.catalogmover.catalogs.Cell;
import com.fatwire.cs.catalogmover.catalogs.Header;
import com.fatwire.cs.catalogmover.catalogs.TableData;
import com.fatwire.cs.catalogmover.catalogs.TableParser;

public class ElementCatalog {

    private final Log log = LogFactory.getLog(getClass());

    private Collection<ElementEntry> elements;

    /**
     * absolute path to the LocalCatalog.html file
     * 
     */
    private final File location;

    private final File elementCatalogPath;

    public ElementCatalog(final File path) {
        super();
        ;
        if (!"LocalCatalog.html".equals(path.getName())) {
            throw new IllegalArgumentException(path
                    + " is not an LocalCatalog.html file.");
        }
        location = path.getAbsoluteFile();
        log.debug("LocalCatalog: " + location.getAbsolutePath());

        elementCatalogPath = new File(location.getParentFile(), "LocalCatalog/");
        log.debug("LocalCatalog: " + elementCatalogPath.getAbsolutePath());

    }

    public ElementEntry findEntry(final String url) {
        if ((url == null) || (url.length() == 0)) {
            throw new IllegalArgumentException("url can't be null or empty.");
        }
        final String normalized = url.replace('\\', '/');
        for (final ElementEntry entry : elements) {
            if (normalized.equals(entry.getUrl())) {
                return entry;
            }

        }
        return null;
    }

    public void refresh() throws IOException {
        log.debug("Refreshing LocalCatalog at " + location);

        final TableParser parser = new TableParser();
        TableData table;
        table = parser.parseHTML(read());

        final Map<Integer, ElementEntry> rows = new HashMap<Integer, ElementEntry>();

        for (final Cell cell : table.getCells().values()) {
            final int rowNum = cell.getRow();
            ElementEntry entry = rows.get(rowNum);
            if (entry == null) {
                entry = new ElementEntry();
                rows.put(rowNum, entry);
            }
            final Header header = cell.getColumn();

            // log.debug(header.getHeader());
            if ("elementname".equals(header.getHeader())) {
                entry.setElementname(cell.getCell());
            } else if ("description".equals(header.getHeader())) {
                entry.setDescription(cell.getCell());
            } else if ("url".equals(header.getHeader())) {
                String v = cell.getCell();
                if (v != null) {
                    v = v.replace('\\', '/');
                    entry.setUrl(v);
                    final File tmp = new File(elementCatalogPath, v);
                    if (!tmp.exists()) {
                        // todo: report an error
                        log.debug(tmp + " does not exist.");
                    }
                }
            } else if ("resdetails1".equals(header.getHeader())) {
                entry.setResdetails1(cell.getCell());
            } else if ("resdetails2".equals(header.getHeader())) {
                entry.setResdetails2(cell.getCell());
            }

            // log.debug(cell);

        }
        elements = rows.values();

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

    public ElementEntry findEntry(final File file) {
        final URI fileURI = file.toURI();

        final URI relativeUri = new File(location.getParentFile(),
                "LocalCatalog").toURI().relativize(fileURI);
        log.debug("findEntry, relativeUri:" + relativeUri.toString());
        return findEntry(relativeUri.toString());
    }

    public File getElementCatalogPath() {
        return elementCatalogPath;
    }
}
