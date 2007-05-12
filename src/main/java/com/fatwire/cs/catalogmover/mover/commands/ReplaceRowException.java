package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.mover.CatalogMoverException;

public class ReplaceRowException extends CatalogMoverException {

    /**
     * 
     */
    private static final long serialVersionUID = 1577956655221447740L;

    /**
     * 
     */
    public ReplaceRowException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public ReplaceRowException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public ReplaceRowException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ReplaceRowException(Throwable cause) {
        super(cause);
    }

}
