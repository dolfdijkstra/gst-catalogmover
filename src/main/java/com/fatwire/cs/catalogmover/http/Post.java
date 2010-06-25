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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class Post {

    public static final String DEFAULT_FILE_ENCODING = Charset.defaultCharset().name();
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private URI url;

    private final Map<String, Part> _multiPartMap = new HashMap<String, Part>();

    public void setUrl(URI path) {
        url = path;

    }

    public void addMultipartData(String name, String value) {
        _multiPartMap.put(name, new StringPart(name, value, DEFAULT_CHARSET));

    }

    public URI getUrl() {
        return url;
    }

    public void addMultipartData(String name, String value, String absolutePath) {
        try {
            _multiPartMap.put(name, new FilePart(name, value, new File(absolutePath), DEFAULT_CONTENT_TYPE,
                    DEFAULT_FILE_ENCODING));
        } catch (FileNotFoundException e) {

            throw new java.lang.RuntimeException(e.getMessage(), e);
        }

    }

    public PostMethod createMethod() {
        PostMethod pm = new PostMethod(getUrl().toASCIIString());

        pm.setRequestEntity(new MultipartRequestEntity(_multiPartMap.values().toArray(new Part[_multiPartMap.size()]),
                pm.getParams()));
        return pm;

    }

}
