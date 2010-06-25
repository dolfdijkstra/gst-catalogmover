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

package com.fatwire.cs.catalogmover.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.PostMethod;

public class Response {

    final PostMethod pm;

    /**
     * @param pm
     */
    public Response(PostMethod pm) {
        super();
        this.pm = pm;
    }

    public int getStatusCode() {
        return pm.getStatusCode();
    }

    public String getResponseEncoding() throws IOException {
        return pm.getResponseCharSet();
    }

    public InputStream getResponseBodyAsStream() throws IOException {
        return pm.getResponseBodyAsStream();
    }

    public void close() {
        pm.releaseConnection();

    }

    /**
     * @param headerName
     * @return
     * @see org.apache.commons.httpclient.HttpMethodBase#getResponseHeader(java.lang.String)
     */
    public Header getResponseHeader(String headerName) {
        return pm.getResponseHeader(headerName);
    }

    /**
     * @return
     * @see org.apache.commons.httpclient.HttpMethodBase#getResponseHeaders()
     */
    public Header[] getResponseHeaders() {
        return pm.getResponseHeaders();
    }

}
