package com.fatwire.cs.catalogmover.util.chunk;

public interface ExceptionHandler<E extends Exception> {

    void handleException(E e);

}
