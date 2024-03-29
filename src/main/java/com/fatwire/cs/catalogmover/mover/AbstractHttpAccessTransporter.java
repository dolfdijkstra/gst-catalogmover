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

import java.net.URI;

import org.apache.commons.lang.StringUtils;

import com.fatwire.cs.catalogmover.http.Post;
import com.fatwire.cs.catalogmover.http.Response;

public abstract class AbstractHttpAccessTransporter {

    private URI csPath;

    private String password;

    private String username;

    private String proxyHost;

    private String proxyPassword;

    private String proxyUsername;

    private int proxyPort;

    public abstract Response execute(Post post) throws Exception;

    public abstract void close();

    public AbstractHttpAccessTransporter() {
        super();
    }

    /**
     * @return the csPath
     */
    public URI getCsPath() {
        return csPath;
    }

    /**
     * @param csPath
     *            the csPath to set
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
     * @param password
     *            the password to set
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
     * @param proxyHost
     *            the proxyHost to set
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = StringUtils.isBlank(proxyHost) ? null : proxyHost;
    }

    /**
     * @return the proxyPort
     */
    public int getProxyPort() {
        return proxyPort;
    }

    /**
     * @param proxyPort
     *            the proxyPort to set
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
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the proxyPassword
     */
    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * @param proxyPassword
     *            the proxyPassword to set
     */
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = StringUtils.isBlank(proxyPassword) ? null : proxyPassword;
    }

    /**
     * @return the proxyUsername
     */
    public String getProxyUsername() {
        return proxyUsername;
    }

    /**
     * @param proxyUsername
     *            the proxyUsername to set
     */
    public void setProxyUsername(String proxyUsername) {

        this.proxyUsername = StringUtils.isBlank(proxyUsername) ? null : proxyUsername;
    }

}
