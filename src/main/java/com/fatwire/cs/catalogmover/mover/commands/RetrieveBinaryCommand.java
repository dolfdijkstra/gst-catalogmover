package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.http.Post;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.SimpleResponse;

public class RetrieveBinaryCommand extends AbstractCatalogMoverCommand {
    private byte[] binary;

    private final String tablename;

    private final String tablekeyvalue;

    private final String tablekey;

    private final String columnname;

    public RetrieveBinaryCommand(BaseCatalogMover cm, final String tableName, final String tableKey,
            final String tableKeyValue, final String columnName) {
        super(cm);
        this.tablename = tableName;
        this.columnname = columnName;
        this.tablekey = tableKey;
        this.tablekeyvalue = tableKeyValue;
    }

    @Override
    public void execute() throws CatalogMoverException {
        retrieveBinary();
    }

    protected void retrieveBinary() throws CatalogMoverException {
        final Post post = prepareNewPost();
        post.addMultipartData("ftcmd", "retrievebinary");
        post.addMultipartData("tablename", tablename);
        post.addMultipartData("tablekeyvalue", tablekeyvalue);
        post.addMultipartData("tablekey", tablekey);
        post.addMultipartData("columnname", columnname);
        post.addMultipartData("retrievestatus", "false");
        long t = System.nanoTime();
        SimpleResponse response = catalogMover.execute(post);
        if (response.getStatusCode() == 200) {
            binary = response.getBody();
        } else {
            long t1 = (System.nanoTime() - t) / 1000;
            throw new CatalogMoverException("retrievebinary for " + tablename + " " + tablekey + "=" + tablekeyvalue
                    + " returned a status " + response.getStatusCode() + " after " + t1 + " micro seconds");
        }
    }

    /**
     * @return the binary
     */
    public byte[] getBinary() {
        return binary;
    }

}
