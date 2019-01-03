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
    public void acknowledge(DeviceEvent.DeviceAcknowledge ack, String deviceToken, String originator)
	    throws SiteWhereAgentException;

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
    public void sendLocation(DeviceEvent.DeviceLocation location, String deviceToken, String originator)
	    throws SiteWhereAgentException;

    /**
     * Send an alert event.
     * 
     * @param alert
     * @param deviceToken
     * @param originator
     * @throws SiteWhereAgentException
     */
    public void sendAlert(DeviceEvent.DeviceAlert alert, String deviceToken, String originator)
	    throws SiteWhereAgentException;
}