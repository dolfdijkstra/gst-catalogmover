package com.fatwire.cs.catalogmover.http;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.methods.PostMethod;

import com.fatwire.cs.catalogmover.mover.AbstractHttpAccessTransporter;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.Transporter;

public class HttpClientTransporter extends AbstractHttpAccessTransporter
        implements Transporter {

    HttpClient client;

    /**
     * @param state 
     * @param proxyPort 
     * @param proxyHost 
     * @param client
     */
    public HttpClientTransporter(HttpConnectionManager httpConnectionManager,
            HttpState state, ProxyHost proxyHost) {
        this.client = new HttpClient(httpConnectionManager);
        if (state != null)
            client.setState(state);

        if (proxyHost != null)
            client.getHostConfiguration().setProxyHost(proxyHost);
    }

    public void close() {

    }

    public Response execute(Post post) {
        PostMethod pm = post.createMethod();
        try {
            int status = client.executeMethod(pm);
        } catch (HttpException e) {
            throw new CatalogMoverException(e.getMessage(), e);
        } catch (IOException e) {
            throw new CatalogMoverException(e.getMessage(), e);
        }
        return new Response(pm);
    }

}