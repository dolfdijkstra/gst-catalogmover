package com.fatwire.cs.catalogmover.mover.commands;

import java.util.Arrays;
import java.util.List;

import com.fatwire.cs.catalogmover.mover.AbstractTest;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.mover.StdOutProgressMonitor;

public class ExportMultipleCatalogsCommandTest extends AbstractTest {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testExecute() throws CatalogMoverException {
        if (isRemoteDisabled())
            return;
        final BaseCatalogMover cm = prepare();
        List<String> catalogs = Arrays
                .asList("SystemInfo,SiteCatalog,AssetType,SystemSQL"
                        .split(","));

        IProgressMonitor monitor = new StdOutProgressMonitor();
        monitor.beginTask("Exporting multiple catalogs", -1);
        CatalogMoverCommand command = new ExportMultipleCatalogsCommand(cm,
                catalogs, getExportPath(), monitor);
        try {
            command.execute();
        } finally {

            cm.close();
        }

    }

}
