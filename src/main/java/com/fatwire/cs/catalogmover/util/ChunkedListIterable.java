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
import java.util.List;

/**
 * Evolution of ChunkedListIterable so that it is now a Iterable
 *
 */
public class ChunkedListIterable<T> implements Iterable<List<T>> {
    private int _chunkSize;

    private List<T> _lst;

    /**
     * Creates an instance given a List and chunk size.
     * 
     * @param lst
     *            Original list
     * @param chunkSize
     *            size of each chunk
     * @throws IllegalArgumentException
     *             if chunkSize is <= 0.
     */
    public ChunkedListIterable(final List<T> lst, final int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "Chunk size must be greater than 0");
        }
        final int size = lst.size();
        _chunkSize = (size > chunkSize) ? chunkSize : size;
        _lst = lst;
    }

    private class ChunkedListIterator implements Iterator<List<T>> {
        int _start, _end; // Start and end of element index for sublist

        /**
         * Any more chunks available for read?
         * 
         * @return <code>true</code> if more chunks are available,
         *         <code>false</code> otherwise
         */
        public boolean hasNext() {
            return _end < _lst.size(); // We have not yet reached the end of
            // list
        }

        /**
         * Returns the next 'chunk'. The object returned is a 'List' size less
         * than = chunk size.
         * 
         * @return List that a chunk of the original list
         */
        public List<T> next() {
            final int nextEndPos = _start + _chunkSize; // Where is the next end
            // position (current
            // position + page size)
            _end = (nextEndPos > _lst.size()) ? _lst.size() : nextEndPos; // Did
            // we
            // overshoot
            // the
            // list's
            // size?
            final List<T> ret = _lst.subList(_start, _end); // Extract sublist
            _start = _end; // For next iteration set the start position
            return ret;
        }

        /**
         * Not supported.
         */
        public void remove() {
            assert false : "Unsupported operation remove()";
        }
    }

    public Iterator<List<T>> iterator() {
        return new ChunkedListIterator();
    }

}
