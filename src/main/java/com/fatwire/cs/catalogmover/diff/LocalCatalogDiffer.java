package com.fatwire.cs.catalogmover.diff;

import java.util.Comparator;
import java.util.Iterator;

import com.fatwire.cs.catalogmover.catalogs.Row;
import com.fatwire.cs.catalogmover.catalogs.RowComparator;
import com.fatwire.cs.catalogmover.catalogs.SortingRowIterable;
import com.fatwire.cs.catalogmover.mover.LocalCatalog;

public class LocalCatalogDiffer implements Iterable<Row> {

    final private LocalCatalog catalog1;

    final private LocalCatalog catalog2;
    private final Comparator<Row> comparator = new RowComparator();

    /**
     * @param catalog1
     * @param catalog2
     */
    public LocalCatalogDiffer(final LocalCatalog catalog1, final LocalCatalog catalog2) {
        super();
        if (!catalog1.getName().equals(catalog2.getName()))
            throw new IllegalArgumentException("Catalogs don't have the same name");
        this.catalog1 = catalog1;
        this.catalog2 = catalog2;
    }

    /**
     * returns Iterator with rows from catalog1 that are different then catalog2
     * 
     */
    public Iterator<Row> iterator() {
        return new DiffIterator<Row>(new SortingRowIterable(catalog1.getRows(), comparator).iterator(),
                new SortingRowIterable(catalog2.getRows(), comparator).iterator(), comparator);

    }

}
