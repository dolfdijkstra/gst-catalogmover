package com.fatwire.cs.catalogmover.util;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class HttpClientUtil {
    public static MultiThreadedHttpConnectionManager getConnectionManager(
            int size) {
        MultiThreadedHttpConnectionManager connectionManager;
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(5000);
        connectionManager.getParams().setMaxTotalConnections(size);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(size);

        return connectionManager;
    }

}
