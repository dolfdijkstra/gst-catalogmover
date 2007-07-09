package com.fatwire.cs.catalogmover.mover;

import java.util.concurrent.ExecutorService;

/**
 * @author Dolf.Dijkstra
 * @since 11-mei-2007
 */
public class CatalogExporter extends BaseCatalogMover {

    public CatalogExporter(final Transporter transporter,
            ExecutorService executor) {
        super(transporter, executor);
    }

}
