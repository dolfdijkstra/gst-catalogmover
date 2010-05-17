package org.apache.commons.httpclient;

import java.io.IOException;

public class Executor {

    public void executeMethod(HttpClient client, HttpMethod method,
            HostConfiguration hostconfig) throws IOException {
        final HttpState state = client.getState();

        HostConfiguration defaulthostconfig = client.getHostConfiguration();
        if (hostconfig == null) {
            hostconfig = defaulthostconfig;
        }
        URI uri = method.getURI();
        if (hostconfig == defaulthostconfig || uri.isAbsoluteURI()) {
            // make a deep copy of the host defaults
            hostconfig = (HostConfiguration) hostconfig.clone();
            if (uri.isAbsoluteURI()) {
                hostconfig.setHost(uri);
            }
        }

        HttpMethodDirector methodDirector = new HttpMethodDirector(
                client.getHttpConnectionManager(), hostconfig, client.getParams(), state);
        
        methodDirector.executeMethod(method);
        method.getStatusCode();
    }

}
