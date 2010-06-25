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
package com.fatwire.cs.catalogmover.util.chunk;

/**
 * The base ChunkProcessor to pass a chunk to the caller to be processed.
 *
 * @author Dolf Dijkstra
 * @since Jun 25, 2007
 *
 */

public class BaseIterableProcessor<T, E extends Exception> implements IterableProcessor<T, E> {

    private final Processor<T, E> processor;

    public BaseIterableProcessor(final Processor<T, E> processor) {
        this.processor = processor;
    }

    /**
     * This method calls processor.endChunk() in a finally block, so it is
     * guaranteed that endChunk() is called for each previous call to
     * beginChunk();
     *
     * All thrown exceptions are not catched
     */
    /*
     * (non-Javadoc)
     *
     * @see
     * com.fatwire.cs.catalogmover.util.chunk.IterableProcessor#process(java
     * .lang.Iterable)
     */
    public void process(final Iterable<T> innerIterable) throws E {
        if (processor.beginChunk()) {
            try {
                for (final T element : innerIterable) {
                    processor.process(element);
                }
            } finally {
                processor.endChunk();
            }
        }
    }

}
