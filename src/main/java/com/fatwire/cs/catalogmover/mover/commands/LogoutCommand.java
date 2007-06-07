package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.NoStatusInResponseException;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;
import com.fatwire.cs.core.http.Post;

public class LogoutCommand extends AbstractCatalogMoverCommand implements
        CatalogMoverCommand {

    public LogoutCommand(final BaseCatalogMover cm) {
        super(cm);
    }

    public void execute() throws CatalogMoverException {
        logout();
    }

    /**
     * @throws CatalogMoverException
     */
    protected void logout() throws CatalogMoverException {
        final Post post = prepareNewPost();
        post.setUrl(cm.getCsPath().getPath());
        post.addMultipartData("ftcmd", "logout");
        post.addMultipartData("killsession", "true");

        final ResponseStatusCode status = cm.executeForResponseStatusCode(post);
        if (status.getResult()) {
            AbstractCatalogMoverCommand.log.info(status.toString());
        } else {
            throw new NoStatusInResponseException();
        }

    }

}
