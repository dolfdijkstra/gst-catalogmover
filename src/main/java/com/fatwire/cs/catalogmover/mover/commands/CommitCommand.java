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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.catalogmover.http.Post;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;

public class CommitCommand extends AbstractCatalogMoverCommand {
    private final static Log log = LogFactory.getLog(CommitCommand.class);

    String tableName;

    String tempTableName;

    String tableKey;

    public CommitCommand(final BaseCatalogMover cm, String tableName, String tempTableName, String tableKey) {
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
