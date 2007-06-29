package com.fatwire.cs.catalogmover.util.chunk;


/**
 * Class to reduce the boilerplate code to work with ChunkedIterable.
 * 
 * This class process the Iterable passed in, as a ChunkedIterable and passes the chunks
 * 
 * It is silent in the sense that it only logs error, but calls process on other chunks and elements if an exceptions was throw from the processor.
 * 
 * @author Dolf.Dijkstra
 * @since Jun 25, 2007
 * @see ChunkedIterable
 * @see Processor
 * @see BaseIterableProcessor
 * @param <T> the type of the elements to process
 */
public class BaseChunkProcessor<T, E extends Exception> extends AbstractChunkProcessor<T,E>  {

    public BaseChunkProcessor(final Iterable<T> iterable, final int size) {
        super(iterable, size);
    }

    /**
     * 
     * @return a BaseIterableProcessor
     */
    protected IterableProcessor<T,E> getIterableProcessor(Processor<T,E> processor) {
        return new BaseIterableProcessor<T,E>(processor);
    }

}
