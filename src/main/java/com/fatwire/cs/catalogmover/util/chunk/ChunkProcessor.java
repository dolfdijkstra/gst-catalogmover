package com.fatwire.cs.catalogmover.util.chunk;

public interface ChunkProcessor<T, E extends Exception> {

    public void process(final IterableProcessor<T, E> processor) throws E;

}