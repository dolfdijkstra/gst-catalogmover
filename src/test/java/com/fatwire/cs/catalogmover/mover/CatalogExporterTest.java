package com.fatwire.cs.catalogmover.mover;

import com.fatwire.cs.catalogmover.mover.commands.CatalogMoverCommand;
import com.fatwire.cs.catalogmover.mover.commands.ExportCatalogCommand;

public class CatalogExporterTest extends AbstractTest {

    public void testElementCatalog() throws CatalogMoverException {
        if (true)
            return;
        final BaseCatalogMover cm = prepare();
        RemoteCatalog catalog = new RemoteCatalog("ElementCatalog", getExportPath());
        IProgressMonitor monitor = new StdOutProgressMonitor();
        monitor.beginTask("Exporting " + catalog.getTableName(), -1);
        CatalogMoverCommand command = new ExportCatalogCommand(cm, catalog,
                monitor);
        command.execute();

        cm.close();

    }
    public void testAssetType() throws CatalogMoverException {
        if (false)
            return;
        final BaseCatalogMover cm = prepare();
        RemoteCatalog catalog = new RemoteCatalog("AssetType", getExportPath());
        IProgressMonitor monitor = new StdOutProgressMonitor();
        monitor.beginTask("Exporting " + catalog.getTableName(), -1);
        CatalogMoverCommand command = new ExportCatalogCommand(cm, catalog,
                monitor);
        command.execute();

        cm.close();

    }
    public void testMungoBlobs() throws CatalogMoverException {
        if (false)
            return;
        final BaseCatalogMover cm = prepare();
        RemoteCatalog catalog = new RemoteCatalog("MungoBlobs", getExportPath());
        IProgressMonitor monitor = new StdOutProgressMonitor();
        monitor.beginTask("Exporting " + catalog.getTableName(), -1);
        CatalogMoverCommand command = new ExportCatalogCommand(cm, catalog,
                monitor);
        command.execute();

        cm.close();

    }

}
