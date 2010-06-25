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

import java.util.Comparator;

public class ComplexObjectComparator implements Comparator<MockComplexObject> {

    public int compare(MockComplexObject o1, MockComplexObject o2) {
        final long idc = o2.getId() - o1.getId();
        if (idc == 0) {
            return o2.getVersion() - o1.getVersion();
        } else {
            return idc < 0 ? -1 : 1;
        }
    }

}
