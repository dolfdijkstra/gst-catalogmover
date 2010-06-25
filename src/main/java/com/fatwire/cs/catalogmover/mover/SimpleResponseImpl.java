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

public class SimpleResponseImpl implements SimpleResponse {

    private int statusCode;

    private String responseEncoding;

    private byte[] body;

    /**
     * @return the body
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * @param body
     *            the body to set
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    /**
     * @return the responseEncoding
     */
    public String getResponseEncoding() {
        return responseEncoding;
    }

    /**
     * @param responseEncoding
     *            the responseEncoding to set
     */
    public void setResponseEncoding(String responseEncoding) {
        this.responseEncoding = responseEncoding;
    }

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode
     *            the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
