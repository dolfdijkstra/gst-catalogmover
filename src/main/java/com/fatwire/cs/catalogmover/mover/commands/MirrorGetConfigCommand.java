package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.MirrorProtocolVersionNotFoundException;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;
import com.fatwire.cs.core.http.Post;

public class MirrorGetConfigCommand extends AbstractCatalogMoverCommand
        implements CatalogMoverCommand {

    public MirrorGetConfigCommand(BaseCatalogMover cm) {
        super(cm);
    }

    public void execute() throws CatalogMoverException {
        this.mirrorGetConfig();
    }

    /**
     * @throws CatalogMoverException
     */
    protected void mirrorGetConfig() throws CatalogMoverException {

        final Post post = this.prepareNewPost();
        post.addMultipartData("ftcmd", "mirrorgetconfig");

        final ResponseStatusCode status = cm.execute(post);
        if (status.getResult()) {
            cm.setMirrorProtocolVersion(status.getParams().get(
                    "mirrorprotocolversion"));
        } else {
            throw new MirrorProtocolVersionNotFoundException();
        }

    }

}
