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

import com.fatwire.cs.catalogmover.mover.AbstractTest;
import com.fatwire.cs.catalogmover.mover.BaseCatalogMover;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.IProgressMonitor;
import com.fatwire.cs.catalogmover.mover.NullProgressMonitor;

public class ExportAllCatalogsCommandTest extends AbstractTest {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testExecute() throws CatalogMoverException {
        if (isRemoteDisabled())
            return;
        final BaseCatalogMover cm = prepare();

        IProgressMonitor monitor = new NullProgressMonitor();
        monitor.beginTask("Exporting all catalogs", -1);
        CatalogMoverCommand command = new ExportAllCatalogsCommand(cm,
                getExportPath(), monitor);
        try {
            command.execute();
        } finally {

            cm.close();
        }

    }

}
