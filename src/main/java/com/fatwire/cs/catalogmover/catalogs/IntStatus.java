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

package com.fatwire.cs.catalogmover.catalogs;

/**
 * An object to pass around as needed to be able to get multiple returns from
 * various functions where the return result is data but an int return is also
 * required
 * <p/>
 * Member is public to bypass overhead of a method call on a simple container.
 */
public class IntStatus {
    /**
     * The value of the object, an int
     */
    public int value;

    /**
     * Constructor, defaults the status to 0
     */
    public IntStatus() {
        value = 0;
    } // a decent value?

    /**
     * Constructor, set the value
     * 
     * @param v
     */
    public IntStatus(final int v) {
        value = v;
    }
}
