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
