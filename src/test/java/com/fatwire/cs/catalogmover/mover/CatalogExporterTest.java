package com.fatwire.cs.catalogmover.mover;

import java.io.File;
import java.net.URI;

import com.fatwire.cs.catalogmover.mover.commands.ExportCatalogCommand;

public class CatalogExporterTest extends AbstractTest {

    public void testElementCatalog() throws CatalogMoverException {
        if (false)
            return;
        final BaseCatalogMover cm = new CatalogExporter();

        cm.setCsPath(URI
                .create("http://radium.nl.fatwire.com:8080/cs/CatalogManager"));
        cm.setUsername("fwadmin");
        cm.setPassword("xceladmin");
        cm.init();
        RemoteCatalog catalog = new RemoteCatalog("SiteCatalog", new File(
                "C:\\tmp\\test\\Populate\\" + Long.toString(System.currentTimeMillis())));
        IProgressMonitor monitor = new StdOutProgressMonitor();
        monitor.beginTask("Exporting " + catalog.getTableName(), -1);
        ExportCatalogCommand command = new ExportCatalogCommand(cm, catalog,
                monitor);
        command.execute();

        cm.close();

    }

}
