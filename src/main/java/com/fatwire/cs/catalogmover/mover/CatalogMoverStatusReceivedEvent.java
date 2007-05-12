package com.fatwire.cs.catalogmover.mover;

import com.fatwire.cs.catalogmover.util.ResponseStatusCode;

public class CatalogMoverStatusReceivedEvent extends CatalogMoverEvent {
    /**
     * 
     */
    private static final long serialVersionUID = -518795323837872575L;

    private final ResponseStatusCode status;

    public CatalogMoverStatusReceivedEvent(AbstractCatalogMover mover,
            ResponseStatusCode status) {
        super(mover);
        this.status = status;
    }

    /**
     * @return the status
     */
    public ResponseStatusCode getStatus() {
        return status;
    }

}
