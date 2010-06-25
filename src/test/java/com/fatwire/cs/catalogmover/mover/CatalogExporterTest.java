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

package com.fatwire.cs.catalogmover.mover;

import com.fatwire.cs.catalogmover.mover.commands.CatalogMoverCommand;
import com.fatwire.cs.catalogmover.mover.commands.ExportCatalogCommand;

public class CatalogExporterTest extends AbstractTest {

    public void testElementCatalog() throws CatalogMoverException {
        if (isRemoteDisabled())
            return;
        final BaseCatalogMover cm = prepare();
        RemoteCatalog catalog = new RemoteCatalog("ElementCatalog", getExportPath());
        IProgressMonitor monitor = new StdOutProgressMonitor();
        monitor.beginTask("Exporting " + catalog.getTableName(), -1);
        CatalogMoverCommand command = new ExportCatalogCommand(cm, catalog,
                monitor);
        command.execute();

        cm.close();

    }
    public void testAssetType() throws CatalogMoverException {
        if (isRemoteDisabled())
            return;
        final BaseCatalogMover cm = prepare();
        RemoteCatalog catalog = new RemoteCatalog("AssetType", getExportPath());
        IProgressMonitor monitor = new StdOutProgressMonitor();
        monitor.beginTask("Exporting " + catalog.getTableName(), -1);
        CatalogMoverCommand command = new ExportCatalogCommand(cm, catalog,
                monitor);
        command.execute();

        cm.close();

    }
    public void testMungoBlobs() throws CatalogMoverException {
        if (isRemoteDisabled())
            return;
        final BaseCatalogMover cm = prepare();
        RemoteCatalog catalog = new RemoteCatalog("MungoBlobs", getExportPath());
        IProgressMonitor monitor = new StdOutProgressMonitor();
        monitor.beginTask("Exporting " + catalog.getTableName(), -1);
        CatalogMoverCommand command = new ExportCatalogCommand(cm, catalog,
                monitor);
        command.execute();

        cm.close();

    }

}
