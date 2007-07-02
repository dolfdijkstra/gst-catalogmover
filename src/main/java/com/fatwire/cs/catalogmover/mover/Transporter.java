package com.fatwire.cs.catalogmover.mover;

import java.net.URI;

import com.fatwire.cs.core.http.HttpAccessException;
import com.fatwire.cs.core.http.Post;
import com.fatwire.cs.core.http.Response;

public interface Transporter {

    Response execute(Post post) throws HttpAccessException;

    void close();

    /**
     * 
     * return the full path to CatalogManager for instance http://localhost:8080/cs/CatalogManager
     */

    URI getCsPath();

    String getPassword();

    String getUsername();

}
