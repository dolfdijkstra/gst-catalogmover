package com.fatwire.cs.catalogmover.util.chunk;

public interface IterableProcessor<T,E extends Exception> {

    /**
     * 
     * Callback method to process a chunk from the iterable.
     * Mostly used with a ChunkedIterable
     * @param iterable
     * @throws E 
     */
    public void process(Iterable<T> innerIterable) throws E;
    


}