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
