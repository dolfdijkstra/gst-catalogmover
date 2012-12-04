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
import java.net.URLEncoder;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.fatwire.cs.catalogmover.mover.AbstractHttpAccessTransporter;
import com.fatwire.cs.catalogmover.mover.CatalogMoverException;
import com.fatwire.cs.catalogmover.mover.Transporter;

public class HttpClientTransporter extends AbstractHttpAccessTransporter
		implements Transporter {

	private HttpClient client;
	private String csrf;

	public HttpClientTransporter() {
		this.client = new HttpClient();
	}

	/**
	 * @param state
	 * @param proxyPort
	 * @param proxyHost
	 * @param client
	 */
	public HttpClientTransporter(HttpConnectionManager httpConnectionManager,
			HttpState state, ProxyHost proxyHost) {
		this.client = new HttpClient(httpConnectionManager);
		if (state != null)
			client.setState(state);

		if (proxyHost != null)
			client.getHostConfiguration().setProxyHost(proxyHost);
	}

	public void close() {

	}

	public Response execute(Post post) {
		PostMethod pm = post.createMethod();
		pm.setRequestHeader("X-CSRF-Token", csrf);
		try {

			client.executeMethod(pm);

		} catch (HttpException e) {
			throw new CatalogMoverException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CatalogMoverException(e.getMessage(), e);
		}
		return new Response(pm);
	}

	@Override
	public void decorate(Post post) {
		post.setUrl(getCsPath());

		post.addMultipartData("authusername", getUsername());
		post.addMultipartData("authpassword", getPassword());

	}

	public void login() {
		GetMethod pm = new GetMethod(getCsPath().toASCIIString());
		try {

			pm.setQueryString("ftcmd=login&username="
					+ URLEncoder.encode(getUsername(), Post.DEFAULT_CHARSET)
					+ "&password="
					+ URLEncoder.encode(getPassword(), Post.DEFAULT_CHARSET));

			int response = client.executeMethod(pm);
			for (Cookie cookie : client.getState().getCookies()) {
				if ("JSESSIONID".equalsIgnoreCase(cookie.getName())) {
					csrf = cookie.getValue();
				}
			}
		} catch (HttpException e) {
			throw new CatalogMoverException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CatalogMoverException(e.getMessage(), e);
		} finally {
			pm.releaseConnection();
		}

	}
}
