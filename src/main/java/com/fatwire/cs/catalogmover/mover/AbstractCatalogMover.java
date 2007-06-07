package com.fatwire.cs.catalogmover.mover;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

    protected final Log log = LogFactory.getLog(getClass());

    private String password;

    private String username;

    private final CopyOnWriteArrayList<CatalogMoverEventListener> eventListeners = new CopyOnWriteArrayList<CatalogMoverEventListener>();

    public AbstractCatalogMover() {
        super();
    }

    public ResponseStatusCode executeForResponseStatusCode(final Post post)
            throws CatalogMoverException {
        final ResponseStatusCode status = new ResponseStatusCode();
        //let the caller deal with the foundStatus
        //don't throw an exception here
        status.setFromData(executeForResponse(post));
        if (hasListeners()) {
            fireEvent(new CatalogMoverStatusReceivedEvent(this, status));
        }

        return status;

    }

    public String executeForResponse(final Post post)
            throws CatalogMoverException {

        SimpleResponse response = execute(post);
        if (response.getStatusCode() == 200) {
            try {
                return new String(response.getBody(), response
                        .getResponseEncoding());
            } catch (UnsupportedEncodingException e) {
                throw new CatalogMoverException(e);
            }

        } else {
            throw new IllegalResponseStatusException(post.getUrl(), response
                    .getStatusCode());
        }

    }

    public SimpleResponse execute(final Post post)
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
            SimpleResponseImpl simpleResponse = new SimpleResponseImpl();
            simpleResponse.setStatusCode(response.getStatusCode());
            if (response.getStatusCode() == 200) {

                simpleResponse.setResponseEncoding(response
                        .getResponseEncoding());

                final InputStream in = response.getResponseBodyAsStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                final byte[] b = new byte[1024];
                int c = 0;

                try {
                    while ((c = in.read(b)) != -1) {
                        out.write(b, 0, c);
                    }
                    simpleResponse.setBody(out.toByteArray());
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
                if (hasListeners()) {
                    fireEvent(new CatalogMoverResponseReceivedEvent(this,
                            response, simpleResponse));
                }
            }
            return simpleResponse;
        } catch (final HttpAccessException e) {
            throw new CatalogMoverException(e);
        } catch (final IOException e) {
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
        return !eventListeners.isEmpty();
    }

    public void registerEventListener(final CatalogMoverEventListener listener) {
        eventListeners.addIfAbsent(listener);
    }

    public void deregisterEventListener(final CatalogMoverEventListener listener) {
        eventListeners.remove(listener);

    }

    protected void fireEvent(final CatalogMoverEvent event) {
        for (final CatalogMoverEventListener listener : eventListeners) {
            listener.fireEvent(event);
        }
    }

}