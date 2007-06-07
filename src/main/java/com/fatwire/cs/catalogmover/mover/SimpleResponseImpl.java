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
     * @param body the body to set
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
     * @param responseEncoding the responseEncoding to set
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
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

}
