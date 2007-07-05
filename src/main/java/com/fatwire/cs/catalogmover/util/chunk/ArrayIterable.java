package com.fatwire.cs.catalogmover.util.chunk;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Makes an Iterable from an array, so an array can be used in methods that expect an iterable
 * 
 * @author Dolf.Dijkstra
 * @since Jun 29, 2007
 * @param <T>
 */
public class ArrayIterable<T> implements Iterable<T> {

    private final T[] array;

    public ArrayIterable(T[] array) {
        this.array = array;
    }

    public Iterator<T> iterator() {

        return new Iterator<T>() {
            private int count = 0;

            public boolean hasNext() {
                return count < array.length;
            }

            public T next() {
                if (count >= array.length) throw new NoSuchElementException();
                return array[count++];
            }

            public void remove() {
                throw new UnsupportedOperationException(
                        "Remove is not supported.");

            }

        };
    }

}
