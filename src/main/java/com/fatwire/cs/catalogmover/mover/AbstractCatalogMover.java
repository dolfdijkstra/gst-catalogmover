/*
 * Copyright 2007 FatWire Corporation. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fatwire.cs.catalogmover.mover;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.cs.catalogmover.http.Post;
import com.fatwire.cs.catalogmover.http.Response;
import com.fatwire.cs.catalogmover.util.ResponseStatusCode;

public abstract class AbstractCatalogMover {
    private final Transporter transporter;

    protected final Log log = LogFactory.getLog(getClass());

    private final CopyOnWriteArrayList<CatalogMoverEventListener> eventListeners = new CopyOnWriteArrayList<CatalogMoverEventListener>();

    public AbstractCatalogMover(final Transporter transporter) {
        super();
        this.transporter = transporter;
    }

    public ResponseStatusCode executeForResponseStatusCode(final Post post) throws CatalogMoverException {
        final ResponseStatusCode status = new ResponseStatusCode();
        // let the caller deal with the foundStatus
        // don't throw an exception here
        status.setFromData(executeForResponse(post));
        if (hasListeners()) {
            fireEvent(new CatalogMoverStatusReceivedEvent(this, status));
        }

        return status;

    }

    protected Post prepareNewPost() {
        final Post post = new Post();
        transporter.decorate(post);
        return post;
    }

    public String executeForResponse(final Post post) throws CatalogMoverException {

        SimpleResponse response = execute(post);
        if (response.getStatusCode() == 200) {
            try {
                return new String(response.getBody(), response.getResponseEncoding());
            } catch (UnsupportedEncodingException e) {
                throw new CatalogMoverException(e);
            }

        } else {
            throw new IllegalResponseStatusException(post.getUrl().toASCIIString(), response.getStatusCode());
        }

    }

    public SimpleResponse execute(final Post post) throws CatalogMoverException {

        Response response = null;
        try {
            final long t = System.currentTimeMillis();
            response = transporter.execute(post);
            if (log.isTraceEnabled()) {
                log.trace("request took " + Long.toString(System.currentTimeMillis() - t) + " ms.");
                log.trace(response);
            }
            SimpleResponseImpl simpleResponse = new SimpleResponseImpl();
            simpleResponse.setStatusCode(response.getStatusCode());
            if (response.getStatusCode() == 200) {

                simpleResponse.setResponseEncoding(response.getResponseEncoding());

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
                    fireEvent(new CatalogMoverResponseReceivedEvent(this, response, simpleResponse));
                }
            }
            return simpleResponse;
        } catch (final IOException e) {
            throw new CatalogMoverException(e);
        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

    public void close() {
        // transporter.close();
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
