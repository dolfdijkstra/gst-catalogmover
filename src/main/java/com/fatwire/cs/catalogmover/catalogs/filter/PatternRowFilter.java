package com.fatwire.cs.catalogmover.catalogs.filter;

import java.util.Iterator;
import java.util.regex.Pattern;

import com.fatwire.cs.catalogmover.catalogs.Row;

public class PatternRowFilter implements Iterable<Row> {
    private final Iterable<Row> delegate;

    final private Pattern p;

    final private String column;

    public PatternRowFilter(final Iterable<Row> delegate,
            final Pattern pattern, final String columnName) {
        super();
        this.delegate = delegate;
        p = pattern;
        column = columnName;
    }

    public Iterator<Row> iterator() {
        final Iterator<Row> delIterator = delegate.iterator();
        return new Iterator<Row>() {
            {
                proceedToNext();
            }

            private Row next = null;

            public boolean hasNext() {
                return next != null;
            }

            public Row next() {
                final Row r = next;
                proceedToNext();
                return r;
            }

            public void remove() {
                assert false : "Unsupported operation remove()";

            }

            private void proceedToNext() {
                while (delIterator.hasNext()) {
                    final Row r = delIterator.next();
                    if (p.matcher(r.getData(column)).find()) {
                        next = r;
                        break;
                    }
                }
            }

        };
    }

}
