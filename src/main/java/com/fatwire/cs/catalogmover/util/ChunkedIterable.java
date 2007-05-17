/*
 * $Logfile:$ $Revision:$ $Date:$
 *
 * Copyright (c) 2005 FatWire Corporation, All Rights Reserved.
 */

/*
 * Copyright 2005 FatWire Corporation. Title, ownership rights, and
 * intellectual property rights in and to this software remain with
 * FatWire Corporation. This software is protected by international
 * copyright laws and treaties, and may be protected by other law.
 * Violation of copyright laws may result in civil liability and
 * criminal penalties.
 */
package com.fatwire.cs.catalogmover.util;

import java.util.Iterator;

/**
 * Evolution of ChunkedListIterable so that it is now a Iterable
 * 
 * 
 * 
 * Typical usage is like this: <code>
 final List<Integer> l = new LinkedList<Integer>();
 for (int i = 0; i < 100; i++) {
 l.add(i);
 }
 int j = 0;
 for (final Iterable<Integer> i : new ChunkedIterable<Integer>(l, 10)) {
 System.out.println(j);
 for (final Integer u : i) {
 System.out.println(j + ":" + u);
 }
 *j++;
 *}
 </code>
 * 
 * <p>
 * The returned Iterable.iterator is backed by the iterator of the original
 * iterable. This means that the 'inner' iterable can't be iterated over more
 * then once.
 * </p>
 * 
 * In the code below, the second loop over <italic>i</italic>will not return
 * any results: <code>
 for (final Iterable<Entry<Integer, String>> i : new ChunkedIterable<Entry<Integer, String>>(
 l.entrySet(), 10)) {
 System.out.println(j);
 for (final Entry<Integer, String> u : i) {
 System.out.println(j + ":" + u);
 }
 for (final Entry<Integer, String> u : i) {
 System.out.println(j + ":" + u);
 }

 j++;
 }
 </code>
 * 
 */
/**
 * @author Dolf.Dijkstra
 * 
 * @param <T>
 */
public class ChunkedIterable<T> implements Iterable<Iterable<T>> {
    private final int _chunkSize;

    private final Iterable<T> _iterable;

    /**
     * Creates an instance given a List and chunk size.
     * 
     * @param iterable
     *            Original iterable
     * @param chunkSize
     *            size of each chunk
     * @throws IllegalArgumentException
     *             if chunkSize is <= 0.
     */
    public ChunkedIterable(final Iterable<T> iterable, final int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "Chunk size must be greater than 0");
        }
        if (iterable == null) {
            throw new NullPointerException("Iterable can not be null.");
        }
        _chunkSize = chunkSize;
        _iterable = iterable;
    }

    private class ChunkedIterator implements Iterator<Iterable<T>> {

        private final Iterator<T> iterator;

        public ChunkedIterator(final Iterator<T> iterator) {
            super();
            this.iterator = iterator;
        }

        /**
         * Any more chunks available for read?
         * 
         * @return <code>true</code> if more chunks are available,
         *         <code>false</code> otherwise
         */
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * Returns the next 'chunk'. The object returned is an Iterable with a
         * size less or equals than chunk size.
         * 
         * @return Iterable that a chunk of the original iterable
         */
        public Iterable<T> next() {
            return new Iterable<T>() {

                public Iterator<T> iterator() {

                    return new Iterator<T>() {
                        int counter = 0;

                        public boolean hasNext() {
                            return iterator.hasNext() && (counter < _chunkSize);
                        }

                        public T next() {
                            counter++;
                            return iterator.next();
                        }

                        public void remove() {
                            assert false : "Unsupported operation remove()";

                        }

                    };
                }

            };

        }

        /**
         * Not supported.
         */
        public void remove() {
            assert false : "Unsupported operation remove()";
        }
    }

    public Iterator<Iterable<T>> iterator() {
        return new ChunkedIterator(this._iterable.iterator());
    }


}
