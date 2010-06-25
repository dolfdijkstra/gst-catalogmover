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

import java.util.Map;

import com.fatwire.cs.catalogmover.util.ResponseStatusCode;

public class ResponseStatusFailureException extends CatalogMoverException {
    /**
     * 
     */
    private static final long serialVersionUID = 1156142017455917291L;

    private final ResponseStatusCode status;

    /**
     * 
     */
    public ResponseStatusFailureException(final String message, final ResponseStatusCode status) {
        super(message + " reason: " + status.getReason() + ", errorID: " + status.getErrorID());
        this.status = status;
    }

    /**
     * @param cause
     */
    public ResponseStatusFailureException(final String message, final ResponseStatusCode status, final Throwable cause) {
        super(message + " reason: " + status.getReason(), cause);
        this.status = status;
    }

    /**
     * @return
     * @see com.fatwire.cs.catalogmover.util.ResponseStatusCode#getCommand()
     */
    public String getCommand() {
        return status.getCommand();
    }

    /**
     * @return
     * @see com.fatwire.cs.catalogmover.util.ResponseStatusCode#getErrorID()
     */
    public int getErrorID() {
        return status.getErrorID();
    }

    /**
     * @return
     * @see com.fatwire.cs.catalogmover.util.ResponseStatusCode#getParams()
     */
    public Map<String, String> getParams() {
        return status.getParams();
    }

    /**
     * @return
     * @see com.fatwire.cs.catalogmover.util.ResponseStatusCode#getReason()
     */
    public String getReason() {
        return status.getReason();
    }

    /**
     * @return
     * @see com.fatwire.cs.catalogmover.util.ResponseStatusCode#getResult()
     */
    public boolean getResult() {
        return status.getResult();
    }

}
