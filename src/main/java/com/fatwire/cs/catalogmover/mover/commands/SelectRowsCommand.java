package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.http.Post;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;

public class SelectRowsCommand extends AbstractCatalogMoverCommand {
    private final String tableName;

    private String response;

    public SelectRowsCommand(final BaseCatalogMover cm, final String tableName) {
        super(cm);
        this.tableName = tableName;
    }

    @Override
    public void execute() throws CatalogMoverException {
        selectRows();

    }

    protected void selectRows() throws CatalogMoverException {
        final Post post = prepareNewPost();
        post.addMultipartData("ftcmd", "selectrow(s)");
        post.addMultipartData("tablename", tableName);

        response = catalogMover.executeForResponse(post);

    }

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

}
