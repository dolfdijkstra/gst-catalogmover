package com.fatwire.cs.catalogmover.mover;

import java.io.IOException;

import org.apache.commons.httpclient.Executor;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.lang.StringUtils;

import com.fatwire.cs.core.http.HostConfig;
import com.fatwire.cs.core.http.HttpAccess;
import com.fatwire.cs.core.http.HttpAccessException;
import com.fatwire.cs.core.http.Post;
import com.fatwire.cs.core.http.RequestState;
import com.fatwire.cs.core.http.Response;

/**
 * this class holds HttpAccess in a ThreadLocal and shares the RequestState
 * @author Dolf.Dijkstra
 * @since Jul 4, 2007
 */

public class PoolableHttpAccessTransporter extends
        AbstractHttpAccessTransporter implements Transporter {
    private final HttpConnectionManager httpConnectionManager;

    private final ThreadLocal<HttpAccess> httpAccess = new ThreadLocal<HttpAccess>() {

        /* (non-Javadoc)
         * @see java.lang.ThreadLocal#initialValue()
         */
        @Override
        protected HttpAccess initialValue() {
            HttpAccess httpAccess;
            final RequestState state = new RequestState();
            if (StringUtils.isBlank(getProxyHost())) {
                final HostConfig hc = new HostConfig(getCsPath());
                httpAccess = new HttpAccess(hc);
            } else {
                final HostConfig hc = new HostConfig(getCsPath().getHost(),
                        getCsPath().getPort(), getCsPath().getScheme(),
                        getProxyHost(), getProxyPort());
                if (!StringUtils.isBlank(getProxyUsername())) {
                    state.setProxyCredentials(RequestState.AuthScope.ANY,
                            getProxyUsername(), getProxyPassword());
                }
                httpAccess = new MyHttpAccess(hc);

            }

            httpAccess.setState(state);
            return httpAccess;
        }

    };

    /**
     * 
     */
    public PoolableHttpAccessTransporter(
            HttpConnectionManager httpConnectionManager) {
        super();
        this.httpConnectionManager = httpConnectionManager;
    }

    HttpAccess getHttpAccess() {
        return httpAccess.get();
    }

    public void close() {
    }

    public Response execute(Post post) throws HttpAccessException {
        final HttpAccess ha = getHttpAccess();

        final Response r = ha.execute(post);
        return r;
    }

    class MyHttpAccess extends HttpAccess {

        public MyHttpAccess(HostConfig config) {
            super(config);
        }

        /* (non-Javadoc)
         * @see com.fatwire.cs.core.http.HttpAccess#executeMethod(org.apache.commons.httpclient.HttpClient, org.apache.commons.httpclient.HttpMethod, org.apache.commons.httpclient.HostConfiguration)
         */

        protected void executeMethod(HttpClient client, HttpMethod method,
                HostConfiguration hostconfig) throws IOException {
            client.setHttpConnectionManager(httpConnectionManager);
            new Executor().executeMethod(client, method, hostconfig);
        }

        /* (non-Javadoc)
         * @see com.fatwire.cs.core.http.HttpAccess#close()
         */
        @Override
        public void close() {
            System.out.println("close");
            super.close();
        }

    }
}
