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

package com.fatwire.cs.catalogmover.catalogs.filter;

import java.util.regex.Pattern;

import com.fatwire.cs.catalogmover.catalogs.Row;

public class PatternBasedIdColumnRowFilter implements Filter<Row> {

    final Pattern pattern;

    /**
     * @param columnName
     * @param pattern
     */
    public PatternBasedIdColumnRowFilter(final Pattern pattern) {
        super();
        this.pattern = pattern;
    }

    public boolean matches(final Row row) {
        String data = row.getData(0);
        if (data == null)
            return false;

        return pattern.matcher(data).matches();
    }

}
