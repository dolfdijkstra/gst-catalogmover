package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.mover.AbstractTest;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.mover.NullProgressMonitor;

public class ExportAllCatalogsCommandTest extends AbstractTest {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testExecute() throws CatalogMoverException {
        if (false)
            return;
        final BaseCatalogMover cm = prepare();

        IProgressMonitor monitor = new NullProgressMonitor();
        monitor.beginTask("Exporting all catalogs", -1);
        CatalogMoverCommand command = new ExportAllCatalogsCommand(cm,
                getExportPath(), monitor);
        try {
            command.execute();
        } finally {

            cm.close();
        }

    }

}
