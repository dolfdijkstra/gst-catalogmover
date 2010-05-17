package com.fatwire.cs.catalogmover.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.methods.PostMethod;

public class Response {

    final PostMethod pm;
    /**
     * @param pm
     */
    public Response(PostMethod pm) {
        super();
        this.pm = pm;
    }

    public int getStatusCode() {
        return pm.getStatusCode();
    }

    public String getResponseEncoding() throws IOException {
        return pm.getResponseBodyAsString();
    }

    public InputStream getResponseBodyAsStream() throws IOException {
        return pm.getResponseBodyAsStream();
    }

    public void close() {
        pm.releaseConnection();
        
    }

}
