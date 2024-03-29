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

package com.fatwire.cs.catalogmover.mover.commands;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

/**
 * Export command for a ContentSever Catalog that uses an include and exclude
 * Filter for selecting the rows to export.
 *
 * @author Dolf Dijkstra
 * @since Nov 18, 2009
 * @see CatalogMoverCommand
 * @see Filter
 */
public class FilteringExportCatalogCommand extends AbstractCatalogMoverCommand {
    private final static Log log = LogFactory.getLog(CatalogMoverCommand.class);

    private final RemoteCatalog catalog;

    private final IProgressMonitor monitor;

    private final List<Filter<Row>> includeFilters;

    private final List<Filter<Row>> excludeFilters;

    public FilteringExportCatalogCommand(final BaseCatalogMover cm, final RemoteCatalog catalog,
            final List<Filter<Row>> includeFilters, final List<Filter<Row>> excludeFilters,
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
        final SelectRowsCommand selectRowsCommand = new SelectRowsCommand(catalogMover, catalog.getTableName());

        selectRowsCommand.execute();
        final String response = selectRowsCommand.getResponse();
        final ResponseStatusCode status = new ResponseStatusCode();
        // final boolean foundStatus =
        // let the caller deal with the foundStatus
        // don't throw an exception here
        status.setFromData(response);
        if (!status.getResult() && status.getErrorID() != -101) {
            throw new ResponseStatusFailureException("Could not export catalog " + catalog.getTableName(), status);
        }
        TableData tableData;
        tableData = new TableParser().parseHTML(response);
        if (tableData == null) {
            log.error(response);
            throw new CatalogMoverException("no tableData found");
        }
        Iterable<Row> rows;
        try {
            Iterable<Row> filtered = new FilteringIterable<Row>(tableData, includeFilters, excludeFilters);
            rows = new SortingRowIterable(filtered);
            StringBuffer b = new TableExporter(tableData).exportHTML(rows, tableData.getDatabaseType(), false);
            monitor.subTask("saving " + catalog.getTableName());
            catalog.writeCatalog(b.toString());
            doUrlFields(tableData, rows);
        } catch (final IOException e) {
            throw new CatalogMoverException(e.getMessage(), e);
        }
        monitor.worked(1);

    }

    protected void doUrlFields(final TableData tableData, final Iterable<Row> rows) throws CatalogMoverException {

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
        List<Future<String>> futures = new LinkedList<Future<String>>();
        for (final Row row : rows) {
            final String tableKeyValue = row.getData(tableKey);
            if (monitor.isCanceled()) {
                break;
            }

            for (final String headerName : urlFields) {
                // no need to download if no value provided
                if (StringUtils.goodString(row.getData(headerName))) {
                    Callable<String> callable = new Callable<String>() {

                        public String call() throws CatalogMoverException {
                            monitor.subTask("downloading " + tableKeyValue + " for " + catalog.getTableName());
                            final RetrieveBinaryCommand command = new RetrieveBinaryCommand(catalogMover, catalog
                                    .getTableName(), tableKey, tableKeyValue, headerName);
                            command.execute();

                            try {
                                if (log.isDebugEnabled())
                                    log.debug(row.getData(headerName));
                                monitor.subTask("saving " + tableKeyValue + " for " + catalog.getTableName());

                                // write the url field

                                catalog.writeUrlField(command.getBinary(), row.getData(headerName));

                            } catch (final IOException e) {
                                throw new CatalogMoverException(e.getMessage() + " on " + catalog.getTableName()
                                        + " key:" + tableKeyValue, e);
                            }
                            monitor.worked(1);
                            return "dummy value";
                        }

                    };
                    futures.add(catalogMover.submit(callable));
                }
            }

        }
        for (Future<String> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                throw new CatalogMoverException(e);
            } catch (ExecutionException e) {
                throw new CatalogMoverException(e);
            }
        }

    }
}
