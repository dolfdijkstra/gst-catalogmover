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

import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.mover.LocalCatalog;

public class MoveCatalogCommand extends AbstractCatalogMoverCommand {
    private final LocalCatalog catalog;

    private final IProgressMonitor monitor;

    public MoveCatalogCommand(final BaseCatalogMover cm, final LocalCatalog catalog, final IProgressMonitor monitor) {
        super(cm);
        this.catalog = catalog;
        this.monitor = monitor;
    }

    @Override
    public void execute() throws CatalogMoverException {
        monitor.beginTask("Moving rows to ContentServer for catalog " + catalog.getName(), -1);

        new MirrorGetConfigCommand(catalogMover).execute();
        CreateTempTableCommand tempTableCommand = new CreateTempTableCommand(catalogMover, catalog.getName(), catalog
                .getTableKey());
        tempTableCommand.execute();
        MoveCatalogRowsCommand moveCatalogRowsCommand = new MoveCatalogRowsCommand(catalogMover, tempTableCommand
                .getTempTableName(), catalog.getTableKey(), catalog.getUploadPath(), catalog.getRows(), monitor);
        moveCatalogRowsCommand.execute();
        CommitCommand commitCommand = new CommitCommand(catalogMover, catalog.getName(), tempTableCommand
                .getTempTableName(), catalog.getTableKey());
        commitCommand.commit();

    }

}
