package com.fatwire.cs.catalogmover.mover.commands;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fatwire.cs.catalogmover.catalogs.Header;
import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.catalogs.SortingRowIterable;
import com.fatwire.cs.catalogmover.catalogs.TableData;
import com.fatwire.cs.catalogmover.catalogs.TableExporter;
import com.fatwire.cs.catalogmover.catalogs.TableParser;
import com.fatwire.cs.catalogmover.catalogs.filter.Filter;
import com.fatwire.cs.catalogmover.catalogs.filter.FilteringIterable;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.mover.RemoteCatalog;
import com.fatwire.cs.catalogmover.mover.ResponseStatusFailureException;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;
import com.fatwire.cs.catalogmover.util.StringUtils;

public class FilteringExportCatalogCommand extends AbstractCatalogMoverCommand {

    private TableData tableData;

    private final RemoteCatalog catalog;

    private final IProgressMonitor monitor;

    private final List<Filter<Row>> includeFilters;

    private final List<Filter<Row>> excludeFilters;

    private Iterable<Row> rows;

    public FilteringExportCatalogCommand(final BaseCatalogMover cm,
            final RemoteCatalog catalog,
            final List<Filter<Row>> includeFilters,
            final List<Filter<Row>> excludeFilters,
            final IProgressMonitor monitor) {
        super(cm);

        this.catalog = catalog;
        this.monitor = monitor;
        this.includeFilters = includeFilters;
        this.excludeFilters = excludeFilters;
    }

    @Override
    public void execute() throws CatalogMoverException {
        if (monitor.isCanceled()) {
            return;
        }
        monitor.subTask("downloading " + catalog.getTableName());
        final SelectRowsCommand selectRowsCommand = new SelectRowsCommand(cm,
                catalog.getTableName());

        selectRowsCommand.execute();
        final String response = selectRowsCommand.getResponse();
        final ResponseStatusCode status = new ResponseStatusCode();
        //final boolean foundStatus =
        //let the caller deal with the foundStatus
        //don't throw an exception here
        status.setFromData(response);
        if (!status.getResult() && status.getErrorID() !=-101) {
            throw new ResponseStatusFailureException(
                    "Could not export catalog " + catalog.getTableName(),
                    status);
        }

        tableData = new TableParser().parseHTML(response);
        if (tableData == null) {
            log.error(response);
            throw new CatalogMoverException("no tableData found");
        }
        try {
            Iterable<Row> filtered = new FilteringIterable<Row>(tableData,
                    includeFilters, excludeFilters);
            rows = new SortingRowIterable(filtered);
            StringBuffer b = new TableExporter(tableData).exportHTML(rows,
                    tableData.getDatabaseType(), false);
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
            if (header.getName().startsWith("url")) {
                urlFields.add(header.getName());
                log.trace("url: " + header.getName());
            }
        }
        if (urlFields.isEmpty()) {
            return;
        }
        final String tableKey = tableData.getTableKey();
        for (final Row row : this.rows) {
            final String tableKeyValue = row.getData(tableKey);
            if (monitor.isCanceled()) {
                break;
            }

            for (final String headerName : urlFields) {
                //no need to download if no value provided 
                if (StringUtils.goodString(row.getData(headerName))) {

                    monitor.subTask("downloading " + tableKeyValue + " for "
                            + catalog.getTableName());
                    final RetrieveBinaryCommand command = new RetrieveBinaryCommand(
                            cm, catalog.getTableName(), tableKey,
                            tableKeyValue, headerName);
                    command.execute();

                    try {
                        AbstractCatalogMoverCommand.log.debug(row
                                .getData(headerName));
                        monitor.subTask("saving " + tableKeyValue + " for "
                                + catalog.getTableName());

                        //write the url field

                        catalog.writeUrlField(command.getBinary(), row
                                .getData(headerName));

                    } catch (final IOException e) {
                        throw new CatalogMoverException(e.getMessage(), e);
                    }
                }
            }
            monitor.worked(1);
        }

    }
}
