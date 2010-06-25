/*
 * Copyright 2007 FatWire Corporation. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.http.Post;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;

public class CreateTempTableCommand extends AbstractCatalogMoverCommand {

    private String ttTableName;

    private final String tableName;

    private final String tableKey;

    public CreateTempTableCommand(BaseCatalogMover cm, final String tableName, final String tableKey) {
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
        final ResponseStatusCode status = catalogMover.executeForResponseStatusCode(post);
        if (status.getResult()) {
            // tablename is in the status message
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
