package com.fatwire.cs.catalogmover.mover;

import java.util.concurrent.ExecutorService;


/**
 * @author Dolf.Dijkstra
 * @since 11-mei-2007
 */
public class CatalogMoverImpl extends BaseCatalogMover {

    public CatalogMoverImpl(final Transporter transporter, ExecutorService executor) {
        super(transporter, executor);
    }
}
