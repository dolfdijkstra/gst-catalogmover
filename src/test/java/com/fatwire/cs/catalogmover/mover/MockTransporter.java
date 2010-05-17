package com.fatwire.cs.catalogmover.mover;

import java.net.URI;

import com.fatwire.cs.catalogmover.http.Post;
import com.fatwire.cs.catalogmover.http.Response;

public class MockTransporter implements Transporter {

    public void close() {

    }

    public Response execute(Post post) {
        return null;

    }

    public URI getCsPath() {
        return URI.create("http://localhost:8080/cs/CatalogManager");
    }

    public String getPassword() {
        return "mockpw";
    }

    public String getUsername() {
        return "mocku";
    }

}
