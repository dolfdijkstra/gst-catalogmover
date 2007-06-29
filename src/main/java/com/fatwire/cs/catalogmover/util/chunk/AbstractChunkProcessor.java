package com.fatwire.cs.catalogmover.util.chunk;

/**
 * Class to reduce the boilerplate code to work with ChunkedIterable.
 * 
 * This class process the Iterable passed in, as a ChunkedIterable and passes the chunks
 * 
 * It is silent in the sense that it calls handleException, but calls process on other chunks and elements if an exceptions was throw from the processor.
 * 
 * @author Dolf.Dijkstra
 * @since Jun 25, 2007
 * @see ChunkedIterable
 * @see Processor
 * @param <T> the type of the elements to process
 * @param <E> the type of the exception to throw
 */
public abstract class AbstractChunkProcessor<T, E extends Exception> {

    private final int size;

    private final Iterable<T> iterable;

    public AbstractChunkProcessor(final Iterable<T> iterable, final int size) {
        this.size = size;
        this.iterable = iterable;
    }

    /**
     * processes the iterable in chunks, to be handled by the Processor 
     * 
     * @param processor
     */
    public void process(final Processor<T, E> processor) throws E {
        final IterableProcessor<T, E> iterableProcessor = getIterableProcessor(processor);
        //ChunkedIterable created here, so process can be called more then once on the iterable passed in into this class  
        final ChunkedIterable<T> chunkedIterable = new ChunkedIterable<T>(
                iterable, size);
        for (final Iterable<T> inner : chunkedIterable) {
            iterableProcessor.process(inner);
        }

    }

    protected abstract IterableProcessor<T, E> getIterableProcessor(
            Processor<T, E> processor);

}
