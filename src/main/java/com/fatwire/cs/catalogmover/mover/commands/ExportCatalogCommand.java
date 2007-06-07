package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.mover.RemoteCatalog;

public class ExportCatalogCommand extends FilteringExportCatalogCommand {

    public ExportCatalogCommand(final BaseCatalogMover cm,
            final RemoteCatalog catalog, final IProgressMonitor monitor) {
        super(cm, catalog, null, null, monitor);

    }

}
