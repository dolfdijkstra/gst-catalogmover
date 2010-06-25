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

import java.io.File;
import java.io.IOException;

import com.fatwire.cs.catalogmover.mover.commands.MoveCatalogCommand;

public class CatalogMoverTest extends AbstractTest {

    public void testMoveCatalog() throws IOException, CatalogMoverException {
        if (isRemoteDisabled())
            return;
        final BaseCatalogMover cm = prepare();

        final File f = new File(
                "C:\\tmp\\support-tools\\trunk\\cs\\src\\main\\Populate\\ElementCatalog.html");
        final LocalCatalog ec = new LocalCatalog(f);
        ec.refresh();
        MoveCatalogCommand command = new MoveCatalogCommand(cm, ec,
                new StdOutProgressMonitor());
        command.execute();
        final File f2 = new File(
                "C:\\tmp\\support-tools\\trunk\\cs\\src\\main\\Populate\\SiteCatalog.html");
        final LocalCatalog sc = new LocalCatalog(f2);
        sc.refresh();
        command = new MoveCatalogCommand(cm, sc, new StdOutProgressMonitor());
        command.execute();
        cm.close();

    }

}
