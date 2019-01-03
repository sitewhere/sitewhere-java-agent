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
 * Interface for classes that process commands for an agent.
 * 
 * @author Derek
 */
public interface IAgentCommandProcessor {

    /**
     * Executes logic that happens before the standard processing loop.
     * 
     * @param deviceToken
     * @param areaToken
     * @param customerToken
     * @param deviceTypeToken
     * @param dispatcher
     * @throws SiteWhereAgentException
     */
    public void executeStartupLogic(String deviceToken, String areaToken, String customerToken, String deviceTypeToken,
	    ISiteWhereEventDispatcher dispatcher) throws SiteWhereAgentException;

    /**
     * Process a SiteWhere system command.
     * 
     * @param message
     * @param dispatcher
     * @throws SiteWhereAgentException
     */
    public void processSiteWhereCommand(byte[] message, ISiteWhereEventDispatcher dispatcher)
	    throws SiteWhereAgentException;

    /**
     * Process a specification command.
     * 
     * @param message
     * @param dispatcher
     * @throws SiteWhereAgentException
     */
    public void processSpecificationCommand(byte[] message, ISiteWhereEventDispatcher dispatcher)
	    throws SiteWhereAgentException;

    /**
     * Set the device Token
     * 
     * @param deviceToken
     * @throws SiteWhereAgentException
     */
    public void setDeviceToken(String deviceToken) throws SiteWhereAgentException;

    /**
     * Set the base area token.
     * 
     * @param areaToken
     * @throws SiteWhereAgentException
     */
    public void setAreaToken(String areaToken) throws SiteWhereAgentException;

    /**
     * Set the base customer.
     * 
     * @param customerToken
     * @throws SiteWhereAgentException
     */
    public void setCustomerToken(String customerToken) throws SiteWhereAgentException;

    /**
     * Set the base device type token.
     * 
     * @param deviceTypeToken
     * @throws SiteWhereAgentException
     */
    public void setDeviceTypeToken(String deviceTypeToken) throws SiteWhereAgentException;

    /**
     * Set the event dispatcher that allows data to be sent back to SiteWhere.
     * 
     * @param dispatcher
     */
    public void setEventDispatcher(ISiteWhereEventDispatcher dispatcher);
}