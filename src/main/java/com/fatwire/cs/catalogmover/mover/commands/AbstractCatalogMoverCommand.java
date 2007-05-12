package com.fatwire.cs.catalogmover.mover.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.core.http.Post;

public abstract class AbstractCatalogMoverCommand implements
        CatalogMoverCommand {
    protected final static Log log = LogFactory
            .getLog(AbstractCatalogMoverCommand.class);

    final protected BaseCatalogMover cm;

    /**
     * @param cm
     */
    public AbstractCatalogMoverCommand(final BaseCatalogMover cm) {
        super();
        this.cm = cm;
    }

    public abstract void execute() throws CatalogMoverException;

    protected Post prepareNewPost() {
        final Post post = new Post();
        post.setUrl(cm.getCsPath().getPath());
        post.addMultipartData("authusername", cm.getUsername());
        post.addMultipartData("authpassword", cm.getPassword());
        post.addMultipartData("cs.contenttype",
                BaseCatalogMover.DEFAULT_CONTENT_TYPE);
        return post;
    }

}
