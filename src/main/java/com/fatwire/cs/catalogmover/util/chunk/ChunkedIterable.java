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

/*
 * Copyright 2005 FatWire Corporation. Title, ownership rights, and
 * intellectual property rights in and to this software remain with
 * FatWire Corporation. This software is protected by international
 * copyright laws and treaties, and may be protected by other law.
 * Violation of copyright laws may result in civil liability and
 * criminal penalties.
 */
package com.fatwire.cs.catalogmover.util.chunk;

import java.util.Iterator;

/**
 * <p>Evolution of ChunkedListIterable so that it is now a Iterable</p>
 * 
 * <p>Typical usage is like this:</p>
 * 
 * <pre>
 * final List&lt;Integer&gt; l = new LinkedList&lt;Integer&gt;();
 * for (int i = 0; i &lt; 100; i++) {
 *     l.add(i);
 * }
 * int j = 0;
 * for (final Iterable&lt;Integer&gt; i : new ChunkedIterable&lt;Integer&gt;(l, 10)) {
 *     System.out.println(j);
 *     for (final Integer u : i) {
 *         System.out.println(j + &quot;:&quot; + u);
 *     }
 *     j++;
 * }
 * 
 * </pre>
 * 
 * <p>
 * The returned Iterable.iterator is backed by the iterator of the original
 * iterable. This means that the 'inner' iterable can't be iterated over more
 * then once.
 * </p>
 * 
 * In the code below, the second loop over <italic>i</italic>will not return any
 * results:
 * 
 * <pre>
 * for (final Iterable&lt;Entry&lt;Integer, String&gt;&gt; i : new ChunkedIterable&lt;Entry&lt;Integer, String&gt;&gt;(l.entrySet(), 10)) {
 *     System.out.println(j);
 *     for (final Entry&lt;Integer, String&gt; u : i) {
 *         System.out.println(j + &quot;:&quot; + u);
 *     }
 *     for (final Entry&lt;Integer, String&gt; u : i) {
 *         System.out.println(j + &quot;:&quot; + u);
 *     }
 * 
 *     j++;
 * }
 * </pre>
 * 
 * @author Dolf Dijkstra
 * 
 * @param <T>
 */
public class ChunkedIterable<T> implements Iterable<Iterable<T>> {
    private final int _chunkSize;

    private final Iterable<T> _iterable;

    /**
     * Creates an instance given an iterable and chunk size.
     * 
     * @param iterable Original iterable
     * @param chunkSize size of each chunk
     * @throws IllegalArgumentException if chunkSize is <= 0.
     */
    public ChunkedIterable(final Iterable<T> iterable, final int chunkSize) {

        if (chunkSize <= 0) {
            throw new IllegalArgumentException("Chunk size must be greater than 0");
        }
        if (iterable == null) {
            throw new NullPointerException("Iterable can not be null.");
        }
        _chunkSize = chunkSize;
        _iterable = iterable;
    }

    private class ChunkedIterator implements Iterator<Iterable<T>> {

        private final Iterator<T> iterator;

        private int progressCount = 0;

        private int lastCount = 0;

        private int chunckCount = 0;

        public ChunkedIterator(final Iterator<T> iterator) {
            super();
            this.iterator = iterator;
        }

        /**
         * Any more chunks available for read?
         * 
         * @return <code>true</code> if more chunks are available,
         *         <code>false</code> otherwise
         */
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * Returns the next 'chunk'. The object returned is an Iterable with a
         * size less or equals than chunk size.
         * 
         * @return Iterable that a chunk of the original iterable
         */
        public Iterable<T> next() {
            if (chunckCount > 0 && lastCount == progressCount && iterator.hasNext())
                throw new IllegalStateException("Last iteration did not consume any elements from chunk");
            chunckCount++;
            lastCount = progressCount;
            return new Iterable<T>() {

                public Iterator<T> iterator() {

                    return new Iterator<T>() {
                        int counter = 0;

                        public boolean hasNext() {
                            return iterator.hasNext() && (counter < _chunkSize);
                        }

                        public T next() {
                            counter++;
                            progressCount++;
                            return iterator.next();
                        }

                        public void remove() {
                            assert false : "Unsupported operation remove()";

                        }

                    };
                }

            };

        }

        /**
         * Not supported.
         */
        public void remove() {
            assert false : "Unsupported operation remove()";
        }
    }

    /**
     * @return an Iterator that iterates over an Iterable with the maximum size
     *         of chunkSize
     * 
     * @throws IllegalStateException if nothing was consumed of the chunk
     */
    public Iterator<Iterable<T>> iterator() {
        return new ChunkedIterator(this._iterable.iterator());
    }

}
