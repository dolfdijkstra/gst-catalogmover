package com.fatwire.cs.catalogmover.mover.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.catalogmover.http.Post;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;

public class CommitCommand extends AbstractCatalogMoverCommand {
    private final static Log log = LogFactory
    .getLog(CommitCommand.class);

    String tableName;

    String tempTableName;

    String tableKey;

    public CommitCommand(final BaseCatalogMover cm, String tableName,
            String tempTableName, String tableKey) {
        super(cm);
        this.tableName = tableName;
        this.tempTableName = tempTableName;
        this.tableKey = tableKey;
    }

    @Override
    public void execute() throws CatalogMoverException {
        commit();

    }

    protected void commit() throws CatalogMoverException {
        final Post post = prepareNewPost();
        post.addMultipartData("ftcmd", "committables");
        post.addMultipartData("tablekey0", tableKey);
        post.addMultipartData("aclList", "Browser,SiteGod");
        post.addMultipartData("childtablename0", tempTableName);
        post.addMultipartData("tablename0", tableName);
        final ResponseStatusCode status = catalogMover.executeForResponseStatusCode(post);
        if (status.getResult()) {
            log.info(status.toString());
        }

    }

}
