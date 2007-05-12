package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.mover.LocalCatalog;

public class MoveCatalogCommand extends AbstractCatalogMoverCommand {
    private final LocalCatalog catalog;

    private final IProgressMonitor monitor;

    public MoveCatalogCommand(final BaseCatalogMover cm, final LocalCatalog catalog,
            final IProgressMonitor monitor) {
        super(cm);
        this.catalog = catalog;
        this.monitor = monitor;
    }

    @Override
    public void execute() throws CatalogMoverException {
        monitor.beginTask("Moving rows to ContentServer for catalog "
                + catalog.getName(), -1);

        new MirrorGetConfigCommand(cm).execute();
        CreateTempTableCommand tempTableCommand = new CreateTempTableCommand(
                cm, catalog.getName(), catalog.getTableKey());
        tempTableCommand.execute();
        MoveCatalogRowsCommand moveCatalogRowsCommand = new MoveCatalogRowsCommand(
                cm, tempTableCommand.getTempTableName(), catalog.getTableKey(),
                catalog.getUploadPath(), catalog.getRows(), monitor);
        moveCatalogRowsCommand.execute();
        CommitCommand commitCommand = new CommitCommand(cm,catalog.getName(),tempTableCommand.getTempTableName(), catalog.getTableKey());
        commitCommand.commit();

    }

}
