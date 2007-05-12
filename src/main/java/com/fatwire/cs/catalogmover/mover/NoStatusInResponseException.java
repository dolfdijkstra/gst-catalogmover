package com.fatwire.cs.catalogmover.mover;


public class NoStatusInResponseException extends CatalogMoverException {

    /**
     * 
     */
    private static final long serialVersionUID = -7692893765606706248L;

    /**
     * 
     */
    public NoStatusInResponseException() {
        super();
    }

    /**
     * @param s
     */
    public NoStatusInResponseException(String s) {
        super(s);
    }

}
