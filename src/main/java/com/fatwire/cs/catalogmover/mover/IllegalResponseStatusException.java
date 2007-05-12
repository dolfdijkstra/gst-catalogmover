package com.fatwire.cs.catalogmover.mover;


public class IllegalResponseStatusException extends CatalogMoverException {


    /**
     * 
     */
    private static final long serialVersionUID = 1952555835410707947L;

    /**
     * 
     */
    public IllegalResponseStatusException() {
        super();
    }

    /**
     * @param s
     */
    public IllegalResponseStatusException(String s) {
        super(s);
    }

    public IllegalResponseStatusException(String url,int statuscode, Throwable cause) {
        super(url +" gave a response code of " + statuscode,cause);
    }

    /**
     * @param cause
     */
    public IllegalResponseStatusException(Throwable cause) {
        super(cause);
    }

    public IllegalResponseStatusException(String url,int statuscode) {
        super(url +" gave a response code of " + statuscode);
    }

}
