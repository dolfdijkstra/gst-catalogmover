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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.catalogs.TableData;
import com.fatwire.cs.catalogmover.catalogs.TableParser;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.mover.ResponseStatusFailureException;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;

public class ExportAllCatalogsCommand extends AbstractCatalogMoverCommand {

    public static final String SYSTEM_INFO = "SystemInfo";

    public static final String SYSTEM_ASSETS = "SystemAssets";

    public static final String SYSTEM_PAGE_CACHE = "SystemPageCache";

    public static final String SYSTEM_ITEM_CACHE = "SystemItemCache";

    static final String[] excludes = new String[] { SYSTEM_INFO, SYSTEM_ASSETS, SYSTEM_PAGE_CACHE, SYSTEM_ITEM_CACHE };

    final List<String> catalogs;

    final File exportPath;

    final IProgressMonitor monitor;

    public ExportAllCatalogsCommand(BaseCatalogMover cm, File exportPath, final IProgressMonitor monitor) {
        super(cm);
        catalogs = new ArrayList<String>();
        this.exportPath = exportPath;
        this.monitor = monitor;
    }

    @Override
    public void execute() throws CatalogMoverException {
        readSystemInfo();
        doAllCatalogs();
        monitor.worked(1);

    }

    /**
     * Reads SystemInfo and builds a list of catalogs
     * 
     * @throws CatalogMoverException
     */
    protected void readSystemInfo() throws CatalogMoverException {
        monitor.subTask("downloading SystemInfo");
        final SelectRowsCommand selectRowsCommand = new SelectRowsCommand(catalogMover, "SystemInfo");

        selectRowsCommand.execute();
        final String response = selectRowsCommand.getResponse();
        final ResponseStatusCode status = new ResponseStatusCode();

        status.setFromData(response);
        if (!status.getResult()) {
            throw new ResponseStatusFailureException("Could not export catalog SystemInfo", status);
        }

        final TableData tableData = new TableParser().parseHTML(response);
        final String tableKey = tableData.getTableKey();
        for (Row row : tableData) {
            final String tableName = row.getData(tableKey);
            boolean ignore = false;
            for (String exclude : excludes) {
                if (exclude.equals(tableName)) {
                    ignore = true;
                    break;
                }
            }
            if (!ignore)
                this.catalogs.add(tableName);
        }
    }

    /**
     * downloads all catalogs
     * 
     * @throws CatalogMoverException
     */
    protected void doAllCatalogs() throws CatalogMoverException {
        AbstractCatalogMoverCommand command = new ExportMultipleCatalogsCommand(catalogMover, catalogs, exportPath,
                monitor);
        command.execute();

    }

}
