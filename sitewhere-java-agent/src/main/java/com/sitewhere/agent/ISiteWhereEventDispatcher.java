/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.agent;

import com.sitewhere.communication.protobuf.proto.SiteWhere.DeviceEvent;

/**
 * Interface for events that can be dispatched to SiteWhere server.
 * 
 * @author Derek
 */
public interface ISiteWhereEventDispatcher {

    /**
     * Register a device.
     * 
     * @param register
     * @param deviceToken
     * @param originator
     * @throws SiteWhereAgentException
     */
    public void registerDevice(DeviceEvent.DeviceRegistrationRequest register, String deviceToken, String originator)
	    throws SiteWhereAgentException;

    /**
     * Send an acknowledgement message.
     * 
     * @param ack
     * @param deviceToken
     * @param originator
     * @throws SiteWhereAgentException
     */
    public void acknowledge(DeviceEvent.DeviceAcknowledge ack, String deviceToken, String originator) throws SiteWhereAgentException;

    /**
     * Send a measurement event.
     * 
     * @param measurement
     * @param deviceToken
     * @param originator
     * @throws SiteWhereAgentException
     */
    public void sendMeasurement(DeviceEvent.DeviceMeasurement measurement, String deviceToken, String originator)
	    throws SiteWhereAgentException;

    /**
     * Send a location event.
     * 
     * @param location
     * @param deviceToken
     * @param originator
     * @throws SiteWhereAgentException
     */
    public void sendLocation(DeviceEvent.DeviceLocation location, String deviceToken, String originator) throws SiteWhereAgentException;

    /**
     * Send an alert event.
     * 
     * @param alert
     * @param deviceToken
     * @param originator
     * @throws SiteWhereAgentException
     */
    public void sendAlert(DeviceEvent.DeviceAlert alert, String deviceToken, String originator) throws SiteWhereAgentException;
}