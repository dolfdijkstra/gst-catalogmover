package com.fatwire.cs.catalogmover.mover;

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

public class PoolableHttpAccessTransporter extends AbstractHttpAccessTransporter
        implements Transporter {
    private final RequestState state = new RequestState();

    private final ThreadLocal<HttpAccess> httpAccess = new ThreadLocal<HttpAccess>() {

        /* (non-Javadoc)
         * @see java.lang.ThreadLocal#initialValue()
         */
        @Override
        protected HttpAccess initialValue() {
            HttpAccess httpAccess;
            if (getProxyHost() == null) {
                final HostConfig hc = new HostConfig(getCsPath());
                httpAccess = new HttpAccess(hc);
            } else {
                final HostConfig hc = new HostConfig(getCsPath().getHost(),
                        getCsPath().getPort(), getCsPath().getScheme(),
                        getProxyHost(), getProxyPort());
                httpAccess = new HttpAccess(hc);

            }

            httpAccess.setState(state);
            return httpAccess;
        }

    };


    HttpAccess getHttpAccess() {
        return httpAccess.get();
    }

    public void close() {
        getHttpAccess().close();
    }

    public Response execute(Post post) throws HttpAccessException {
        return getHttpAccess().execute(post);
    }

}
