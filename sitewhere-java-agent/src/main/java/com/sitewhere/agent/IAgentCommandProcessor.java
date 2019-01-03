/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
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
    public void executeStartupLogic(String deviceToken, String areaToken, String customerToken, String deviceTypeToken, ISiteWhereEventDispatcher dispatcher)
	    throws SiteWhereAgentException;

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
     * @param areaToken
     * @throws SiteWhereAgentException
     */
    public void setAreaToken(String areaToken) throws SiteWhereAgentException;

    /**
     * Set the base customer.
     * @param customerToken
     * @throws SiteWhereAgentException
     */
    public void setCustomerToken(String customerToken) throws SiteWhereAgentException;
     
    /**
     * Set the base device type token.
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