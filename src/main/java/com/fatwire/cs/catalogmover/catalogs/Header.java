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

/**
 * 
 */
package com.fatwire.cs.catalogmover.catalogs;

public class Header {
    private final int column;

    private final String name;

    private final String schema;

    private final int type;

    Header(final int column, final String name, final String schema, final int type) {
        this.column = column;
        this.name = name;
        this.schema = schema;
        this.type = type;
    }

    public int getColumn() {
        return column;
    }

    public String getName() {
        return name;
    }

    public String getSchema() {
        return schema;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "name: " + name + " schema: " + schema + " type: " + type;
    }
}
