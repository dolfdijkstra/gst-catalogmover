package com.fatwire.cs.catalogmover.mover;

import java.util.EventListener;

public interface CatalogMoverEventListener extends EventListener{

    void fireEvent(CatalogMoverEvent event);

}
