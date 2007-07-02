package com.fatwire.cs.catalogmover.mover.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.NoStatusInResponseException;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;
import com.fatwire.cs.core.http.Post;

public class LogoutCommand extends AbstractCatalogMoverCommand implements
        CatalogMoverCommand {
    protected final static Log log = LogFactory
    .getLog(LogoutCommand.class);

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
        post.addMultipartData("ftcmd", "logout");
        post.addMultipartData("killsession", "true");

        final ResponseStatusCode status = catalogMover.executeForResponseStatusCode(post);
        if (status.getResult()) {
            log.info(status.toString());
        } else {
            throw new NoStatusInResponseException();
        }

    }

}
