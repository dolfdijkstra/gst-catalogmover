package com.fatwire.cs.catalogmover.util.chunk;

/**
 * @author Dolf.Dijkstra
 * @since Jul 5, 2007
 * @param <T>
 * @param <E>
 */
public interface IterableProcessor<T,E extends Exception> {

    /**
     * 
     * Callback method to process a chunk from the iterable.
     * Mostly used with a ChunkedIterable
     * @param iterable
     * @throws E 
     */
    void process(Iterable<T> innerIterable) throws E;
    


}