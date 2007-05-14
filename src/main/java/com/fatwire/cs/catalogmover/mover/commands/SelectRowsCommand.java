package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.core.http.Post;

public class SelectRowsCommand extends AbstractCatalogMoverCommand {
    private final String tableName;

    private String response;

    public SelectRowsCommand(BaseCatalogMover cm, final String tableName) {
        super(cm);
        this.tableName = tableName;
    }

    @Override
    public void execute() throws CatalogMoverException {
        selectRows();

    }

    protected void selectRows() throws CatalogMoverException {
        final Post post = this.prepareNewPost();
        post.addMultipartData("ftcmd", "selectrow(s)");
        post.addMultipartData("tablename", tableName);

        response = cm.executeForResponse(post);

    }

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

}
