package com.fatwire.cs.catalogmover.mover;

import java.io.File;
import java.io.IOException;

import com.fatwire.cs.catalogmover.mover.commands.MoveCatalogCommand;

public class CatalogMoverTest extends AbstractTest {

    public void testMoveCatalog() throws IOException, CatalogMoverException {
        if (isRemoteDisabled())
            return;
        final BaseCatalogMover cm = prepare();

        final File f = new File(
                "C:\\tmp\\support-tools\\trunk\\cs\\src\\main\\Populate\\ElementCatalog.html");
        final LocalCatalog ec = new LocalCatalog(f);
        ec.refresh();
        MoveCatalogCommand command = new MoveCatalogCommand(cm, ec,
                new StdOutProgressMonitor());
        command.execute();
        final File f2 = new File(
                "C:\\tmp\\support-tools\\trunk\\cs\\src\\main\\Populate\\SiteCatalog.html");
        final LocalCatalog sc = new LocalCatalog(f2);
        sc.refresh();
        command = new MoveCatalogCommand(cm, sc, new StdOutProgressMonitor());
        command.execute();
        cm.close();

    }

}
