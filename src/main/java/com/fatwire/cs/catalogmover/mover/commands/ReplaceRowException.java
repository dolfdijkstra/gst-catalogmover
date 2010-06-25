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

package com.fatwire.cs.catalogmover.mover.commands;

import com.fatwire.cs.catalogmover.mover.CatalogMoverException;

public class ReplaceRowException extends CatalogMoverException {

    /**
     * 
     */
    private static final long serialVersionUID = 1577956655221447740L;

    /**
     * 
     */
    public ReplaceRowException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public ReplaceRowException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public ReplaceRowException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ReplaceRowException(Throwable cause) {
        super(cause);
    }

}
