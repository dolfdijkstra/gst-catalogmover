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

package com.fatwire.cs.catalogmover.diff;

import java.io.File;
import java.util.Iterator;

import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.mover.LocalCatalog;

/**
 * an Iterable of rows with url fields that are updated since the a passed in
 * timestamp
 *
 * @author Dolf Dijkstra
 * @since 18-mei-2007
 */
public class LocallyUpdatedDiffer implements Iterable<Row> {
    public static long TOLERANCE = 2 * 1000L; // 2 seconds

    private final long timeStamp;

    private final Iterable<Row> delegate;

    private final LocalCatalog catalog;

    /**
     * @param catalog
     * @param delegate
     * @param timeStamp
     */
    public LocallyUpdatedDiffer(final LocalCatalog catalog, final Iterable<Row> delegate, final long timeStamp) {
        super();
        this.catalog = catalog;
        this.delegate = delegate;
        this.timeStamp = timeStamp;
    }

    public Iterator<Row> iterator() {
        final Iterator<Row> delegatingIterator = delegate.iterator();
        return new Iterator<Row>() {
            private Row next = proceed();

            public boolean hasNext() {
                return next != null;
            }

            private Row proceed() {
                next = null;
                while (delegatingIterator.hasNext() && next == null) {
                    Row row = delegatingIterator.next();
                    for (int i = 0; i < row.getNumberOfColumns(); i++) {
                        if (row.getHeader(i).getName().startsWith("url")) {
                            File f = new File(catalog.getUploadPath(), row.getData(i));
                            if (f.exists() && (f.lastModified() > timeStamp)) {
                                next = row;
                                break;
                            }
                        }
                    }
                }
                return next;
            }

            public Row next() {
                Row ret = next;
                next = proceed();
                return ret;
            }

            public void remove() {
                // TODO Auto-generated method stub

            }

        };
    }
}
