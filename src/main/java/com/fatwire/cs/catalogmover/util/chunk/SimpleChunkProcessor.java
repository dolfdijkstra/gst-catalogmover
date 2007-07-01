package com.fatwire.cs.catalogmover.util.chunk;

public class SimpleChunkProcessor<T, E extends Exception> implements ChunkProcessor<T, E> {

    protected final int size;
    protected final Iterable<T> iterable;

    public SimpleChunkProcessor(final Iterable<T> iterable, final int size) {
            this.size = size;
            this.iterable = iterable;
        }

    /* (non-Javadoc)
     * @see com.fatwire.cs.catalogmover.util.chunk.ChunkProcessor#process(com.fatwire.cs.catalogmover.util.chunk.IterableProcessor)
     */
    public void process(final IterableProcessor<T, E> processor) throws E {
    
        //ChunkedIterable created here, so process can be called more then once on the iterable passed in into this class  
        final ChunkedIterable<T> chunkedIterable = new ChunkedIterable<T>(
                iterable, size);
        for (final Iterable<T> inner : chunkedIterable) {
            processor.process(inner);
        }
    
    }

}