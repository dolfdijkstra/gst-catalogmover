package com.fatwire.cs.catalogmover.mover;

import com.fatwire.cs.core.http.Post;

public abstract class BaseCatalogMover extends AbstractCatalogMover {

    public static final String DEFAULT_CONTENT_TYPE = "text/html; charset=UTF-8";

    public static final int MIRROR_PROTOCOL_VERSION = 3;

    public BaseCatalogMover(final Transporter transporter) {
        super(transporter);
    }

    public void verifyMirrorProtocolVersion(final String version)
            throws MirrorProtocolVersionMisMatchException {
        if (log.isDebugEnabled()) {
            log.debug("mirrorprotocolversion: " + version);
        }
        if (Integer.parseInt(version) != BaseCatalogMover.MIRROR_PROTOCOL_VERSION) {
            throw new MirrorProtocolVersionMisMatchException();
        }

    }

    public Post prepareNewPost() {
        final Post post = super.prepareNewPost();
        post.addMultipartData("cs.contenttype",
                BaseCatalogMover.DEFAULT_CONTENT_TYPE);
        return post;
    }

}