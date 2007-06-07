package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;
import com.fatwire.cs.core.http.Post;

public class CreateTempTableCommand extends AbstractCatalogMoverCommand {

    private String ttTableName;

    private final String tableName;

    private final String tableKey;

    public CreateTempTableCommand(BaseCatalogMover cm, final String tableName,
            final String tableKey) {
        super(cm);
        this.tableName = tableName;
        this.tableKey = tableKey;
    }

    @Override
    public void execute() throws CatalogMoverException {
        createTempTable();
    }

    /**
     * @throws CatalogMoverException 
     */
    protected void createTempTable() throws CatalogMoverException {
        final Post post = this.prepareNewPost();
        post.addMultipartData("ftcmd", "createtemptable");
        post.addMultipartData("tablekey", tableKey);
        post.addMultipartData("aclList", "Browser,SiteGod");
        post.addMultipartData("parenttablename", tableName);
        final ResponseStatusCode status = cm.executeForResponseStatusCode(post);
        if (status.getResult()) {
            //tablename is in the status message
            ttTableName = status.getParams().get("tablename");
        }

    }

    /**
     * @return the ttTableName
     */
    public String getTempTableName() {
        return ttTableName;
    }


}
