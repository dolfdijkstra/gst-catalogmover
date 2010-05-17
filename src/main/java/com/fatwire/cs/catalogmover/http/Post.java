package com.fatwire.cs.catalogmover.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

public class Post {

    public static final String DEFAULT_FILE_ENCODING = Charset.defaultCharset()
            .name();

    public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private String url;

    private final Map<String, Part> _multiPartMap = new HashMap<String, Part>();

    public void setUrl(String path) {
        url = path;

    }

    public void addMultipartData(String name, String value) {
        _multiPartMap.put(name, new StringPart(name, value));

    }

    public String getUrl() {
        return url;
    }

    public void addMultipartData(String name, String value, String absolutePath) {
        try {
            _multiPartMap
                    .put(name, new FilePart(name, value,
                            new File(absolutePath), DEFAULT_CONTENT_TYPE,
                            DEFAULT_FILE_ENCODING));
        } catch (FileNotFoundException e) {

            throw new java.lang.RuntimeException(e.getMessage(), e);
        }

    }

    /**
     * File f = new File("/path/fileToUpload.txt");
      PostMethod filePost = new PostMethod("http://host/some_path");
      Part[] parts = {
          new StringPart("param_name", "value"),
          new FilePart(f.getName(), f)
      };
      filePost.setRequestEntity(
          new MultipartRequestEntity(parts, filePost.getParams())
          );
      HttpClient client = new HttpClient();
      int status = client.executeMethod(filePost);

     * 
     */

    public PostMethod createMethod() {
        PostMethod pm = new PostMethod(getUrl());

        pm
                .setRequestEntity(new MultipartRequestEntity(_multiPartMap.values().toArray(new Part[_multiPartMap.size()]), pm
                        .getParams()));
        return pm;

    }

}
