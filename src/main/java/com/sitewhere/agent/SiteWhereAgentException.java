/*
 * Copyright © 2019 SiteWhere, LLC. All rights reserved. https://sitewhere.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sitewhere.agent;

/**
 * Generic SiteWhere Java Agent Exception.
 * 
 * @author Derek
 */
public class SiteWhereAgentException extends Exception {

    private static final long serialVersionUID = 3351303154000958250L;

    public SiteWhereAgentException() {
    }

    public SiteWhereAgentException(String message) {
	super(message);
    }

    public SiteWhereAgentException(Throwable error) {
	super(error);
    }

    public SiteWhereAgentException(String message, Throwable error) {
	super(message, error);
    }
}