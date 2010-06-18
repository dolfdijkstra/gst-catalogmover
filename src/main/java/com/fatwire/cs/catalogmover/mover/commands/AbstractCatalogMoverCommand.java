package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.http.Post;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;

public abstract class AbstractCatalogMoverCommand implements CatalogMoverCommand {

    final protected BaseCatalogMover catalogMover;

    /**
     * @param catalogMover
     */
    public AbstractCatalogMoverCommand(final BaseCatalogMover cm) {
        super();
        this.catalogMover = cm;
    }

    public abstract void execute() throws CatalogMoverException;

    protected Post prepareNewPost() {
        return catalogMover.prepareNewPost();
    }

}
