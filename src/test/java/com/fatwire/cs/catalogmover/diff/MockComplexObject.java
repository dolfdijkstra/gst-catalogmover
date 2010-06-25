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

public class MockComplexObject {

    private final long id;

    private final int version;

    /**
     * @param id
     * @param version
     */
    public MockComplexObject(final long id, final int version) {
        super();
        this.id = id;
        this.version = version;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (int) (id ^ (id >>> 32));
        result = PRIME * result + version;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MockComplexObject other = (MockComplexObject) obj;
        if (id != other.id)
            return false;
        if (version != other.version)
            return false;
        return true;
    }

}
