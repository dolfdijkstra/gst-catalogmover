package com.fatwire.cs.catalogmover.catalogs;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class RowComparator implements Comparator<Row> {
    private final Collator usCollator = Collator.getInstance(Locale.US);

    public RowComparator() {
        usCollator.setStrength(Collator.PRIMARY);
    }

    /**
     * Compare on tableKey field
     */
    public int compare(Row source, Row target) {

        return usCollator.compare(source.getData(0), target.getData(0));
    }

}
