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
public abstract class AbstractChunkProcessor<T, E extends Exception> extends
        SimpleChunkProcessor<T, E> {

    public AbstractChunkProcessor(final Iterable<T> iterable, final int size) {
        super(iterable, size);
    }

    /**
     * processes the iterable in chunks, to be handled by the Processor 
     * 
     * @param processor
     */
    public void process(final Processor<T, E> processor) throws E {
        process(getIterableProcessor(processor));
    }

    protected abstract IterableProcessor<T, E> getIterableProcessor(
            Processor<T, E> processor);

}
