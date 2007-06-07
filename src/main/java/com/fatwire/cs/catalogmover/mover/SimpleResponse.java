package com.fatwire.cs.catalogmover.mover;

public interface SimpleResponse {
    /**
     * @return the body
     */
    public byte[] getBody();

    /**
     * @return the responseEncoding
     */
    public String getResponseEncoding();

    /**
     * @return the statusCode
     */
    public int getStatusCode();

}
