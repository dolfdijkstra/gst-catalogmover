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

import com.fatwire.cs.catalogmover.http.Post;
import com.fatwire.cs.catalogmover.http.Response;

public class MockTransporter implements Transporter {

    public void close() {

    }

    public Response execute(Post post) {
        return null;

    }

    public URI getCsPath() {
        return URI.create("http://localhost:8080/cs/CatalogManager");
    }

    public String getPassword() {
        return "mockpw";
    }

    public String getUsername() {
        return "mocku";
    }

	@Override
	public void decorate(Post post) {

		
	}

}
