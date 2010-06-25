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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.fatwire.cs.catalogmover.http.Post;

public abstract class BaseCatalogMover extends AbstractCatalogMover {

    public static final String DEFAULT_CONTENT_TYPE = "text/html; charset=UTF-8";

    public static final int MIRROR_PROTOCOL_VERSION = 3;
    private final ExecutorService executor;

    public BaseCatalogMover(final Transporter transporter, final ExecutorService executor) {
        super(transporter);
        this.executor = executor;
    }

    public void verifyMirrorProtocolVersion(final String version) throws MirrorProtocolVersionMisMatchException {
        if (log.isDebugEnabled()) {
            log.debug("mirrorprotocolversion: " + version);
        }
        if (Integer.parseInt(version) != BaseCatalogMover.MIRROR_PROTOCOL_VERSION) {
            throw new MirrorProtocolVersionMisMatchException();
        }

    }

    public Post prepareNewPost() {
        final Post post = super.prepareNewPost();
        post.addMultipartData("cs.contenttype", BaseCatalogMover.DEFAULT_CONTENT_TYPE);
        return post;
    }

    /**
     * @param <T>
     * @param task
     * @return
     * @see java.util.concurrent.ExecutorService#submit(java.util.concurrent.Callable)
     */
    public <T> Future<T> submit(final Callable<T> task) {
        return executor.submit(task);
    }

}
