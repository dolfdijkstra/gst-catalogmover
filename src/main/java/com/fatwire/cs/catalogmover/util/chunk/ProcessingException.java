package com.fatwire.cs.catalogmover.util.chunk;

/**
 * Exception that can be thrown by a Processor to indicate a failure when processing an element.
 * @author Dolf.Dijkstra
 * @since Jun 26, 2007
 * @see Processor
 */
public class ProcessingException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 7600840872740188777L;

    public ProcessingException(String message) {
        super(message);
    }

    public ProcessingException(String message, Throwable cause) {
        super(message, cause);

    }

}
