package com.fatwire.cs.catalogmover.mover;

import com.fatwire.cs.core.http.HostConfig;
import com.fatwire.cs.core.http.HttpAccess;
import com.fatwire.cs.core.http.RequestState;


public abstract class BaseCatalogMover extends AbstractCatalogMover {

    public static final String DEFAULT_CONTENT_TYPE = "text/html; charset=UTF-8";

    public static final int MIRROR_PROTOCOL_VERSION = 3;

    protected String mirrorprotocolversion = null;

    public BaseCatalogMover() {
        super();
    }

    public void setMirrorProtocolVersion(String version)
            throws MirrorProtocolVersionMisMatchException {
        if (log.isDebugEnabled()) {
            log.debug("mirrorprotocolversion: " + version);
        }
        if (Integer.parseInt(version) != MIRROR_PROTOCOL_VERSION) {
            throw new MirrorProtocolVersionMisMatchException();
        }

    }

    public void init() {
        final HostConfig hc = new HostConfig(getCsPath());
        httpAccess = new HttpAccess(hc);
        final RequestState state = new RequestState();
        httpAccess.setState(state);
    
    }

    public void close() {
        httpAccess.close();
    
    }

}