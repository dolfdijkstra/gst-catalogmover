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

package com.fatwire.cs.catalogmover.util.chunk;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Makes an Iterable from an array, so an array can be used in methods that
 * expect an iterable
 *
 * @author Dolf Dijkstra
 * @since Jun 29, 2007
 * @param <T>
 */
public class ArrayIterable<T> implements Iterable<T> {

    private final T[] array;

    public ArrayIterable(T[] array) {
        this.array = array;
    }

    public Iterator<T> iterator() {

        return new Iterator<T>() {
            private int count = 0;

            public boolean hasNext() {
                return count < array.length;
            }

            public T next() {
                if (count >= array.length)
                    throw new NoSuchElementException();
                return array[count++];
            }

            public void remove() {
                throw new UnsupportedOperationException("Remove is not supported.");

            }

        };
    }

}
