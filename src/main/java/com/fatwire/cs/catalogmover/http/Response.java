package com.fatwire.cs.catalogmover.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.Header;
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
        return pm.getResponseCharSet();
    }

    public InputStream getResponseBodyAsStream() throws IOException {
        return pm.getResponseBodyAsStream();
    }

    public void close() {
        pm.releaseConnection();

    }

    /**
     * @param headerName
     * @return
     * @see org.apache.commons.httpclient.HttpMethodBase#getResponseHeader(java.lang.String)
     */
    public Header getResponseHeader(String headerName) {
        return pm.getResponseHeader(headerName);
    }

    /**
     * @return
     * @see org.apache.commons.httpclient.HttpMethodBase#getResponseHeaders()
     */
    public Header[] getResponseHeaders() {
        return pm.getResponseHeaders();
    }

}
