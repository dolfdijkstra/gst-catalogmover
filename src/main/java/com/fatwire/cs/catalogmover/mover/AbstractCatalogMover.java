package com.fatwire.cs.catalogmover.mover;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.catalogmover.util.ResponseStatusCode;
import com.fatwire.cs.core.http.HttpAccess;
import com.fatwire.cs.core.http.HttpAccessException;
import com.fatwire.cs.core.http.Post;
import com.fatwire.cs.core.http.Response;

public abstract class AbstractCatalogMover {

    private URI csPath;

    protected HttpAccess httpAccess;

    protected final Log log = LogFactory.getLog(this.getClass());

    private String password;

    private String username;

    private final CopyOnWriteArrayList<CatalogMoverEventListener> eventListeners = new CopyOnWriteArrayList<CatalogMoverEventListener>();

    public AbstractCatalogMover() {
        super();
    }

    public ResponseStatusCode execute(final Post post)
            throws CatalogMoverException {
        final ResponseStatusCode status = new ResponseStatusCode();
        //final boolean foundStatus =
        //let the caller deal with the foundStatus
        //don't throw an exception here
        status.setFromData(executeForResponse(post));
        if (this.hasListeners()) {
            this.fireEvent(new CatalogMoverStatusReceivedEvent(this, status));
        }

        return status;

    }

    public String executeForResponse(final Post post)
            throws CatalogMoverException {

        final long t = System.currentTimeMillis();
        Response response = null;
        try {
            response = httpAccess.execute(post);
            if (log.isTraceEnabled()) {
                log.trace("request took "
                        + Long.toString(System.currentTimeMillis() - t)
                        + " ms.");
                log.trace(response);
            }
            if (response.getStatusCode() == 200) {
                final String charset = response.getResponseEncoding();

                final InputStreamReader in = new InputStreamReader(response
                        .getResponseBodyAsStream(), charset);
                final char[] chars = new char[1024];
                int c = 0;

                final StringBuilder out = new StringBuilder();
                try {
                    while ((c = in.read(chars)) != -1) {
                        out.append(chars, 0, c);
                    }
                } finally {
                    if (in != null)
                        in.close();
                }
                if (this.hasListeners()) {
                    this.fireEvent(new CatalogMoverResponseReceivedEvent(this,
                            response, out.toString()));
                }
                return out.toString();
            } else {
                throw new IllegalResponseStatusException(post.getUrl(),
                        response.getStatusCode());
            }
        } catch (HttpAccessException e) {
            throw new CatalogMoverException(e);
        } catch (IOException e) {
            throw new CatalogMoverException(e);
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

    /**
     * @return the csPath
     */
    public URI getCsPath() {
        return csPath;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * 
     * @param csPath the full path to CatalogManager for instance http://localhost:8080/cs/CatalogManager
     */
    public void setCsPath(final URI csPath) {
        this.csPath = csPath;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    protected boolean hasListeners() {
        return !this.eventListeners.isEmpty();
    }

    public void registerEventListener(CatalogMoverEventListener listener) {
        this.eventListeners.addIfAbsent(listener);
    }

    public void deregisterEventListener(CatalogMoverEventListener listener) {
        this.eventListeners.remove(listener);

    }

    protected void fireEvent(CatalogMoverEvent event) {
        for (CatalogMoverEventListener listener : eventListeners) {
            listener.fireEvent(event);
        }
    }

}