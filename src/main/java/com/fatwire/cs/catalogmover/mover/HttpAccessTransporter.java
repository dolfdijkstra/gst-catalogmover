package com.fatwire.cs.catalogmover.mover;

import java.net.URI;

import com.fatwire.cs.core.http.HostConfig;
import com.fatwire.cs.core.http.HttpAccess;
import com.fatwire.cs.core.http.HttpAccessException;
import com.fatwire.cs.core.http.Post;
import com.fatwire.cs.core.http.RequestState;
import com.fatwire.cs.core.http.Response;

public class HttpAccessTransporter implements Transporter {
    private URI csPath;

    protected HttpAccess httpAccess;

    private String password;

    private String username;

    private String proxyHost;

    private int proxyPort;

    public void init() {
        if (proxyHost == null) {
            final HostConfig hc = new HostConfig(getCsPath());
            httpAccess = new HttpAccess(hc);
        } else {
            final HostConfig hc = new HostConfig(getCsPath().getHost(),
                    getCsPath().getPort(), getCsPath().getScheme(), proxyHost,
                    proxyPort);
            httpAccess = new HttpAccess(hc);

        }
        final RequestState state = new RequestState();
        httpAccess.setState(state);

    }

    /**
     * @return the csPath
     */
    public URI getCsPath() {
        return csPath;
    }

    /**
     * @param csPath the csPath to set
     */
    public void setCsPath(URI csPath) {
        this.csPath = csPath;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the proxyHost
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
     * @param proxyHost the proxyHost to set
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * @return the proxyPort
     */
    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * @param proxyPort the proxyPort to set
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public void close() {
        httpAccess.close();
    }

    public Response execute(Post post) throws HttpAccessException {
        return httpAccess.execute(post);
    }

}
