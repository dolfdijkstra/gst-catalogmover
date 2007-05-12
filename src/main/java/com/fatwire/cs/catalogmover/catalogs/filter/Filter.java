package com.fatwire.cs.catalogmover.catalogs.filter;


public interface Filter<T> {

    boolean matches(T row);

}
