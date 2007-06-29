package com.fatwire.cs.catalogmover.util.chunk;

import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * Code borrowed from Bruce Eckel (http://www.mindview.net/Etc/Discussions/CheckedExceptions)
 * 
 * It converts any checked exception into a RuntimeException while preserving all the information from the checked exception.
 * 
 * @author Dolf.Dijkstra
 * @since Jun 29, 2007
 */
public class ExceptionAdapter extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = -6725464553770535043L;



    public final Exception originalException;

    public ExceptionAdapter(Exception e) {
        super(e.toString());
        originalException = e;
    }


    public void rethrow() throws Exception {
        throw originalException;
    }


    /**
     * @return
     * @see java.lang.Throwable#getCause()
     */
    public Throwable getCause() {
        return originalException.getCause();
    }


    /**
     * @return
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    public String getLocalizedMessage() {
        return originalException.getLocalizedMessage();
    }


    /**
     * @return
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
        return originalException.getMessage();
    }


    /**
     * @return
     * @see java.lang.Throwable#getStackTrace()
     */
    public StackTraceElement[] getStackTrace() {
        return originalException.getStackTrace();
    }


    /**
     * @param cause
     * @return
     * @see java.lang.Throwable#initCause(java.lang.Throwable)
     */
    public Throwable initCause(Throwable cause) {
        return originalException.initCause(cause);
    }


    /**
     * 
     * @see java.lang.Throwable#printStackTrace()
     */
    public void printStackTrace() {
        originalException.printStackTrace();
    }


    /**
     * @param s
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    public void printStackTrace(PrintStream s) {
        originalException.printStackTrace(s);
    }


    /**
     * @param s
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    public void printStackTrace(PrintWriter s) {
        originalException.printStackTrace(s);
    }


    /**
     * @param stackTrace
     * @see java.lang.Throwable#setStackTrace(java.lang.StackTraceElement[])
     */
    public void setStackTrace(StackTraceElement[] stackTrace) {
        originalException.setStackTrace(stackTrace);
    }
}