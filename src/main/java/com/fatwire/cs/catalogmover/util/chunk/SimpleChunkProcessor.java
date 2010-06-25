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

/**
 * @author Dolf Dijkstra
 * @since Jul 5, 2007
 * @param <T>
 * @param <E>
 */
public class SimpleChunkProcessor<T, E extends Exception> implements ChunkProcessor<T, E> {

    protected final int size;
    protected final Iterable<T> iterable;

    /**
     * @param iterable
     * @param size
     */
    public SimpleChunkProcessor(final Iterable<T> iterable, final int size) {
        this.size = size;
        this.iterable = iterable;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.fatwire.cs.catalogmover.util.chunk.ChunkProcessor#process(com.fatwire
     * .cs.catalogmover.util.chunk.IterableProcessor)
     */
    public void process(final IterableProcessor<T, E> processor) throws E {

        // ChunkedIterable created here, so process can be called more then once
        // on the iterable passed in into this class
        final ChunkedIterable<T> chunkedIterable = new ChunkedIterable<T>(iterable, size);
        for (final Iterable<T> inner : chunkedIterable) {
            processor.process(inner);
        }

    }

}
