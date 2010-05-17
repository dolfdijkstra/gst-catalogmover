package com.fatwire.cs.catalogmover.util;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class HttpClientUtil {
    public static MultiThreadedHttpConnectionManager getConnectionManager(
            int size) {
        MultiThreadedHttpConnectionManager connectionManager;
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(60000);
        connectionManager.getParams().setMaxTotalConnections(size+1);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(size+1);

        return connectionManager;
    }

}
