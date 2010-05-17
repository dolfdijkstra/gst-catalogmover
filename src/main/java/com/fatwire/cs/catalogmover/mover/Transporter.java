package com.fatwire.cs.catalogmover.mover;

import java.net.URI;

import com.fatwire.cs.catalogmover.http.Post;
import com.fatwire.cs.catalogmover.http.Response;

public interface Transporter {

    Response execute(Post post) throws CatalogMoverException;

    void close();

    /**
     * 
     * return the full path to CatalogManager for instance http://localhost:8080/cs/CatalogManager
     */

    URI getCsPath();

    String getPassword();

    String getUsername();

}
