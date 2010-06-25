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

package com.fatwire.cs.catalogmover.util.chunk;

import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * Code borrowed from Bruce Eckel (http://www.mindview.net/Etc/Discussions/CheckedExceptions)
 *
 * It converts any checked exception into a RuntimeException while preserving all the information from the checked exception.
 *
 * @author Dolf Dijkstra
 * @since Jun 29, 2007
 */
public class ExceptionAdapter extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -6725464553770535043L;



    public final Exception originalException;

    public ExceptionAdapter(Exception e) {
        super(e.toString());
        originalException = e;
    }


    public void rethrow() throws Exception {
        throw originalException;
    }


    /**
     * @return
     * @see java.lang.Throwable#getCause()
     */
    public Throwable getCause() {
        return originalException.getCause();
    }


    /**
     * @return
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    public String getLocalizedMessage() {
        return originalException.getLocalizedMessage();
    }


    /**
     * @return
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
        return originalException.getMessage();
    }


    /**
     * @return
     * @see java.lang.Throwable#getStackTrace()
     */
    public StackTraceElement[] getStackTrace() {
        return originalException.getStackTrace();
    }


    /**
     * @param cause
     * @return
     * @see java.lang.Throwable#initCause(java.lang.Throwable)
     */
    public Throwable initCause(Throwable cause) {
        return originalException.initCause(cause);
    }


    /**
     *
     * @see java.lang.Throwable#printStackTrace()
     */
    public void printStackTrace() {
        originalException.printStackTrace();
    }


    /**
     * @param s
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    public void printStackTrace(PrintStream s) {
        originalException.printStackTrace(s);
    }


    /**
     * @param s
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    public void printStackTrace(PrintWriter s) {
        originalException.printStackTrace(s);
    }


    /**
     * @param stackTrace
     * @see java.lang.Throwable#setStackTrace(java.lang.StackTraceElement[])
     */
    public void setStackTrace(StackTraceElement[] stackTrace) {
        originalException.setStackTrace(stackTrace);
    }
}