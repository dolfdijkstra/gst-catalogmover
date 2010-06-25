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
 *
 * Class to handle an element.
 *
 * It is expected that implementations handle the exception and ignore further
 * calls to process if needed.
 *
 * @author Dolf Dijkstra
 * @since Jun 26, 2007
 * @param <T>
 * @see ChunkProcessor
 */
public interface Processor<T, E extends Exception> {

    /**
     * Called when a new chunk will be processed
     *
     * @return true on success
     */
    boolean beginChunk() throws E;

    /**
     *
     * @param element
     *            the element to process
     * @return
     * @throws ProcessingException
     *             when processing threw an exception. Ideally the root
     *             exception can be found by calling getCause() on the thrown
     *             exception.
     */

    boolean process(T element) throws E;

    /**
     * Called when processing a chunk is done
     *
     * @return true if succesful.
     */
    boolean endChunk() throws E;

}
