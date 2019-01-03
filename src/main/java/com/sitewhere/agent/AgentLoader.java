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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bootstraps the Java agent.
 * 
 * @author Derek
 */
public class AgentLoader {

    /** Static logger instance */
    private static Logger LOGGER = Logger.getLogger(AgentLoader.class.getName());

    /** Default filename for configuration properties */
    private static final String DEFAULT_CONFIG_FILENAME = "config.properties";

    /** Agent controlled by this loader */
    private static Agent agent = new Agent();

    /**
     * Start the agent loader.
     * 
     * @param args
     */
    public static void main(String[] args) {
	LOGGER.info("SiteWhere Java agent starting...");

	String propsFile = null;
	if (args.length > 0) {
	    LOGGER.info("Loading configuration from default properties file: " + args[0]);
	    propsFile = args[0];
	} else {
	    LOGGER.info("Loading configuration from default properties file: " + DEFAULT_CONFIG_FILENAME);
	    propsFile = DEFAULT_CONFIG_FILENAME;
	}

	FileInputStream in = null;
	try {
	    in = new FileInputStream(propsFile);
	    Properties props = new Properties();
	    props.load(in);
	    agent.load(props);
	} catch (IOException e) {
	    LOGGER.log(Level.SEVERE, "Unable to load configuration from specified file.", e);
	} finally {
	    if (in != null) {
		try {
		    in.close();
		} catch (Exception e) {
		}
	    }
	}

	try {
	    agent.start();
	} catch (SiteWhereAgentException e) {
	    LOGGER.log(Level.SEVERE, "Unable to start agent.", e);
	}
    }
}