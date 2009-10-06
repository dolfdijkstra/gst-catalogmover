/**
 * 
 */
package com.fatwire.cs.catalogmover.diff;

import java.util.Comparator;
import java.util.Iterator;

public class DiffIterator<T> implements Iterator<T> {

    private final Iterator<T> i1;

    private final Iterator<T> i2;

    private final Comparator<T> comparator;

    private T curVal1;

    private T curVal2;

    public DiffIterator(Iterator<T> i1, Iterator<T> i2, Comparator<T> comparator) {

        this.i1 = i1;
        this.i2 = i2;
        this.comparator = comparator;
        curVal1 = proceed();

    }

    public boolean hasNext() {
        return curVal1 != null;
    }

    public T next() {
        //System.out.println("next() called, will return " + curVal1);
        T ret = curVal1;
        curVal1 = proceed();
        return ret;
    }

    public void remove() {

    }

    private T proceed() {
        while (i1.hasNext()) {
            int c = 1;
            T current1 = i1.next();
            if (curVal1 != null && comparator.compare(current1, curVal1) < 1)
                throw new IllegalStateException(
                        "Ordering in first iterable is not according to comparator order.");

            if (curVal2 != null) {
                c = this.comparator.compare(current1, curVal2);
            }
            if (c < 0) {
                //return current1 if current1 is before curVal2
                return current1;
            } else while (c > 0) {
                //progress in i2
                if (!i2.hasNext()) {
                    return current1;
                }
                T current2 = i2.next();
                if (curVal2 != null
                        && comparator.compare(current2, curVal2) < 1)
                    throw new IllegalStateException(
                            "Ordering in second iterable is not according to comparator order.");

                curVal2 = current2;
                if ((c=comparator.compare(current1, curVal2)) < 0) {
                    return current1;
                }
            }
        }
        return null;

    }

    public String toString() {
        return "curVal1: " + String.valueOf(curVal1);

    }
}