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

package com.fatwire.cs.catalogmover.mover;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.catalogs.filter.Filter;
import com.fatwire.cs.catalogmover.catalogs.filter.PatternBasedRowFilter;
import com.fatwire.cs.catalogmover.catalogs.filter.PatternBasedRowFilterFactory;
import com.fatwire.cs.catalogmover.mover.commands.CatalogMoverCommand;
import com.fatwire.cs.catalogmover.mover.commands.FilteringExportCatalogCommand;

public class FilteringCatalogExporterTest extends AbstractTest {

    public void testElementCatalog() throws CatalogMoverException {
        if (isRemoteDisabled()) {
            return;
        }
        final Pattern p = Pattern.compile("WebServices/.*");
        assertTrue(p.matcher("WebServices/ajkg").matches());

        final BaseCatalogMover cm = prepare();
        final RemoteCatalog catalog = new RemoteCatalog("ElementCatalog",
                getExportPath());
        final IProgressMonitor monitor = new StdOutProgressMonitor();
        monitor.beginTask("Exporting " + catalog.getTableName(), -1);
        final Filter<Row> filter = new PatternBasedRowFilter("elementname", p);
        final List<Filter<Row>> includeList = new ArrayList<Filter<Row>>();
        includeList.add(filter);
        final CatalogMoverCommand command = new FilteringExportCatalogCommand(
                cm, catalog, includeList, null, monitor);
        command.execute();

        cm.close();

    }

    public void testElementCatalog2() throws CatalogMoverException {
        if (isRemoteDisabled()) {
            return;
        }

        final BaseCatalogMover cm = prepare();
        final RemoteCatalog catalog = new RemoteCatalog("ElementCatalog",
                getExportPath());
        final IProgressMonitor monitor = new StdOutProgressMonitor();
        monitor.beginTask("Exporting " + catalog.getTableName(), -1);

        final List<Filter<Row>> includeList = PatternBasedRowFilterFactory
                .create(new String[] { "resdetails1", "tid=.*", "resdetails1",
                        "eid=.*" });
        final CatalogMoverCommand command = new FilteringExportCatalogCommand(
                cm, catalog, includeList, null, monitor);
        command.execute();

        cm.close();

    }

}
