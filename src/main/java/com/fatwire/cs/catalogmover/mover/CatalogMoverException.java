package com.fatwire.cs.catalogmover.mover;

public class CatalogMoverException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 2729211296114937836L;

    /**
     * 
     */
    public CatalogMoverException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public CatalogMoverException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public CatalogMoverException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public CatalogMoverException(Throwable cause) {
        super(cause);
    }

}
