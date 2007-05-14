package com.fatwire.cs.catalogmover.mover.commands;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.fatwire.cs.catalogmover.catalogs.Header;
import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.catalogs.SortingRowIterable;
import com.fatwire.cs.catalogmover.catalogs.TableData;
import com.fatwire.cs.catalogmover.catalogs.TableExporter;
import com.fatwire.cs.catalogmover.catalogs.TableParser;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.mover.RemoteCatalog;

public class ExportCatalogCommand extends AbstractCatalogMoverCommand {

    private TableData tableData;

    private final RemoteCatalog catalog;

    private final IProgressMonitor monitor;

    public ExportCatalogCommand(final BaseCatalogMover cm,
            final RemoteCatalog catalog, final IProgressMonitor monitor) {
        super(cm);

        this.catalog = catalog;
        this.monitor = monitor;
    }

    @Override
    public void execute() throws CatalogMoverException {
        if (monitor.isCanceled()) {
            return;
        }
        monitor.subTask("downloading " + catalog.getTableName());
        final SelectRowsCommand selectRowsCommand = new SelectRowsCommand(cm, catalog
                .getTableName());

        selectRowsCommand.execute();
        final String response = selectRowsCommand.getResponse();
        tableData = new TableParser().parseHTML(response);
        

        try {
            StringBuffer b = new TableExporter(tableData).exportHTML(new SortingRowIterable(tableData), "", false);
            monitor.subTask("saving " + catalog.getTableName());
            catalog.writeCatalog(b.toString());
        } catch (final IOException e) {
            throw new CatalogMoverException(e.getMessage(), e);
        }
        monitor.worked(1);
        doUrlFields();

    }

    protected void doUrlFields() throws CatalogMoverException {

        final Set<String> urlFields = new HashSet<String>();
        for (final Header header : tableData.getHeaders().values()) {
            if (header.getHeader().startsWith("url")) {
                urlFields.add(header.getHeader());
            }
        }
        if (urlFields.isEmpty()) {
            return;
        }
        final String tableKey = tableData.getTableKey();
        for (final Row row : tableData) {
            final String tableKeyValue = row.getData(tableKey);
            if (monitor.isCanceled()) {
                break;
            }

            for (final String headerName : urlFields) {
                monitor.subTask("downloading " + tableKeyValue + " for "
                        + catalog.getTableName());
                final RetrieveBinaryCommand command = new RetrieveBinaryCommand(cm,
                        catalog.getTableName(), tableKey, tableKeyValue,
                        headerName);
                command.execute();

                try {
                    AbstractCatalogMoverCommand.log.debug(row.getData(headerName));
                    monitor.subTask("saving " + tableKeyValue + " for "
                            + catalog.getTableName());

                    //write the url field

                    catalog.writeUrlField(command.getBinary(), row
                            .getData(headerName));

                } catch (final IOException e) {
                    throw new CatalogMoverException(e.getMessage(), e);
                }

            }
            monitor.worked(1);
        }

    }
}
