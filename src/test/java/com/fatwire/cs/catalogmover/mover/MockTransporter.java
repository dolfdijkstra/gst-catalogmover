package com.fatwire.cs.catalogmover.mover;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import com.fatwire.cs.core.http.HttpAccessException;
import com.fatwire.cs.core.http.Post;
import com.fatwire.cs.core.http.RequestState;
import com.fatwire.cs.core.http.Response;

public class MockTransporter implements Transporter {

    public void close() {

    }

    public Response execute(Post post) throws HttpAccessException {
        return new Response(){

            public void close() {
                // TODO Auto-generated method stub
                
            }

            public byte[] getResponseBody() throws IOException {
                // TODO Auto-generated method stub
                return null;
            }

            public InputStream getResponseBodyAsStream() throws IOException {
                // TODO Auto-generated method stub
                return null;
            }

            public String getResponseBodyAsString() throws IOException {
                // TODO Auto-generated method stub
                return null;
            }

            public String getResponseEncoding() {
                // TODO Auto-generated method stub
                return null;
            }

            public Map getResponseFooters() {
                // TODO Auto-generated method stub
                return null;
            }

            public Map getResponseHeaders() {
                // TODO Auto-generated method stub
                return null;
            }

            public RequestState getState() {
                // TODO Auto-generated method stub
                return null;
            }

            public int getStatusCode() {
                // TODO Auto-generated method stub
                return 0;
            }

            public String getStatusLine() {
                // TODO Auto-generated method stub
                return null;
            }

            public String getStatusText() {
                // TODO Auto-generated method stub
                return null;
            }
            
        };
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
