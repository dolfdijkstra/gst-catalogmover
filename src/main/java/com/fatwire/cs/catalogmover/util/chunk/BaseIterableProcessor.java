/**
 * 
 */
package com.fatwire.cs.catalogmover.util.chunk;


/**
 * The base ChunkProcessor to pass a chunk to the caller to be processed.
 * 
 * @author Dolf.Dijkstra
 * @since Jun 25, 2007
 * 
 */

public class BaseIterableProcessor<T,E extends Exception> implements IterableProcessor<T,E> {

    private final Processor<T,E> processor;


    public BaseIterableProcessor(final Processor<T,E> processor) {
        this.processor = processor;
    }

    /**
     * This method calls processor.endChunk() in a finally block, so it is guaranteed that endChunk() is called for each previous call to beginChunk();
     * 
     * All thrown exceptions are not catched 
     */
    /* (non-Javadoc)
     * @see com.fatwire.cs.catalogmover.util.chunk.IterableProcessor#process(java.lang.Iterable)
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