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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sitewhere.communication.protobuf.proto.SiteWhere.Device;
import com.sitewhere.communication.protobuf.proto.SiteWhere.Device.Header;
import com.sitewhere.communication.protobuf.proto.SiteWhere.Device.RegistrationAck;
import com.sitewhere.communication.protobuf.proto.SiteWhere.DeviceEvent.DeviceAcknowledge;
import com.sitewhere.communication.protobuf.proto.SiteWhere.DeviceEvent.DeviceAlert;
import com.sitewhere.communication.protobuf.proto.SiteWhere.DeviceEvent.DeviceLocation;
import com.sitewhere.communication.protobuf.proto.SiteWhere.DeviceEvent.DeviceMeasurement;
import com.sitewhere.communication.protobuf.proto.SiteWhere.DeviceEvent.DeviceRegistrationRequest;
import com.sitewhere.communication.protobuf.proto.SiteWhere.GOptionalDouble;
import com.sitewhere.communication.protobuf.proto.SiteWhere.GOptionalString;
import com.sitewhere.spi.device.event.IDeviceEventOriginator;

/**
 * Base class for command processing. Handles processing of inbound SiteWhere
 * system messages. Processing of specification commands is left up to
 * subclasses.
 * 
 * @author Derek
 */
public abstract class BaseCommandProcessor implements IAgentCommandProcessor {

    /** Static logger instance */
    private static final Logger LOGGER = Logger.getLogger(BaseCommandProcessor.class.getName());

    /** device token */
    private String deviceToken;

    /** area token */
    private String areaToken;

    /** customer token */
    private String customerToken;

    /** device type token */
    private String deviceTypeToken;

    /** SiteWhere event dispatcher */
    private ISiteWhereEventDispatcher eventDispatcher;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.agent.IAgentCommandProcessor#executeStartupLogic(java.lang.
     * String, java.lang.String, com.sitewhere.agent.ISiteWhereEventDispatcher)
     */
    @Override
    public void executeStartupLogic(String deviceToken, String areaToken, String customerToken, String deviceTypeToken,
	    ISiteWhereEventDispatcher dispatcher) throws SiteWhereAgentException {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.agent.IAgentCommandProcessor#processSiteWhereCommand(byte[],
     * com.sitewhere.agent.ISiteWhereEventDispatcher)
     */
    @Override
    public void processSiteWhereCommand(byte[] message, ISiteWhereEventDispatcher dispatcher)
	    throws SiteWhereAgentException {
	ByteArrayInputStream stream = new ByteArrayInputStream(message);
	try {
	    Header header = Device.Header.parseDelimitedFrom(stream);
	    switch (header.getCommand()) {
	    case REGISTRATION_ACK: {
		RegistrationAck ack = RegistrationAck.parseDelimitedFrom(stream);
		handleRegistrationAck(header, ack);
		break;
	    }
	    case DEVICE_STREAM_ACK: {
		// TODO: Add device stream support.
		break;
	    }
	    case RECEIVE_DEVICE_STREAM_DATA: {
		// TODO: Add device stream support.
		break;
	    }
	    case UNRECOGNIZED: {
		break;
	    }
	    }
	} catch (IOException e) {
	    throw new SiteWhereAgentException(e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.agent.IAgentCommandProcessor#processSpecificationCommand(byte[]
     * , com.sitewhere.agent.ISiteWhereEventDispatcher)
     */
    @Override
    public void processSpecificationCommand(byte[] message, ISiteWhereEventDispatcher dispatcher)
	    throws SiteWhereAgentException {
	try {
	    ByteArrayInputStream encoded = new ByteArrayInputStream(message);
	    ObjectInputStream in = new ObjectInputStream(encoded);

	    String commandName = (String) in.readObject();
	    Object[] parameters = (Object[]) in.readObject();
	    Object[] parametersWithOriginator = new Object[parameters.length + 1];
	    Class<?>[] types = new Class[parameters.length];
	    Class<?>[] typesWithOriginator = new Class[parameters.length + 1];
	    int i = 0;
	    for (Object parameter : parameters) {
		types[i] = parameter.getClass();
		typesWithOriginator[i] = types[i];
		parametersWithOriginator[i] = parameters[i];
		i++;
	    }
	    IDeviceEventOriginator originator = (IDeviceEventOriginator) in.readObject();
	    typesWithOriginator[i] = IDeviceEventOriginator.class;
	    parametersWithOriginator[i] = originator;

	    Method method = null;
	    try {
		method = getClass().getMethod(commandName, typesWithOriginator);
		method.invoke(this, parametersWithOriginator);
	    } catch (NoSuchMethodException e) {
		LOGGER.log(Level.WARNING, "Unable to find method with originator parameter.", e);
		method = getClass().getMethod(commandName, types);
		method.invoke(this, parameters);
	    }
	} catch (StreamCorruptedException e) {
	    LOGGER.log(Level.WARNING, "Unable to decode command in hybrid mode.", e);
	} catch (IOException e) {
	    LOGGER.log(Level.WARNING, "Unable to read command in hybrid mode.", e);
	} catch (ClassNotFoundException e) {
	    LOGGER.log(Level.WARNING, "Unable to resolve parameter class.", e);
	} catch (NoSuchMethodException e) {
	    LOGGER.log(Level.WARNING, "Unable to find method signature that matches command.", e);
	} catch (IllegalAccessException e) {
	    LOGGER.log(Level.WARNING, "Not allowed to call method for command.", e);
	} catch (IllegalArgumentException e) {
	    LOGGER.log(Level.WARNING, "Invalid argument for command.", e);
	} catch (InvocationTargetException e) {
	    LOGGER.log(Level.WARNING, "Unable to call method for command.", e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.agent.IAgentCommandProcessor#setdeviceToken(java.lang.String)
     */
    public void setDeviceToken(String deviceToken) {
	this.deviceToken = deviceToken;
    }

    public String getDeviceToken() {
	return deviceToken;
    }

    public String getAreaToken() {
	return areaToken;
    }

    public void setAreaToken(String areaToken) {
	this.areaToken = areaToken;
    }

    public String getCustomerToken() {
	return customerToken;
    }

    public void setCustomerToken(String customerToken) {
	this.customerToken = customerToken;
    }

    public String getDeviceTypeToken() {
	return deviceTypeToken;
    }

    public void setDeviceTypeToken(String deviceTypeToken) {
	this.deviceTypeToken = deviceTypeToken;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.agent.IAgentCommandProcessor#setEventDispatcher(com.sitewhere.
     * agent .ISiteWhereEventDispatcher)
     */
    public void setEventDispatcher(ISiteWhereEventDispatcher eventDispatcher) {
	this.eventDispatcher = eventDispatcher;
    }

    public ISiteWhereEventDispatcher getEventDispatcher() {
	return eventDispatcher;
    }

    /**
     * Handle the registration acknowledgement message.
     * 
     * @param ack
     * @param originator
     */
    public void handleRegistrationAck(Header header, RegistrationAck ack) {
    }

    /**
     * Convenience method for sending device registration information to SiteWhere.
     * 
     * @param deviceToken
     * @param specificationToken
     * @throws SiteWhereAgentException
     */
    public void sendRegistration(String deviceToken, String areaToken, String customerToken, String deviceTypeToken)
	    throws SiteWhereAgentException {
	DeviceRegistrationRequest.Builder builder = DeviceRegistrationRequest.newBuilder();

	builder.setAreaToken(GOptionalString.newBuilder().setValue(areaToken));
	builder.setCustomerToken(GOptionalString.newBuilder().setValue(customerToken));
	builder.setDeviceTypeToken(GOptionalString.newBuilder().setValue(deviceTypeToken));

	DeviceRegistrationRequest register = builder.build();
	getEventDispatcher().registerDevice(register, deviceToken, null);
    }

    /**
     * Convenience method for sending an acknowledgement event to SiteWhere.
     * 
     * @param deviceToken
     * @param message
     * @param originator
     * @throws SiteWhereAgentException
     */
    public void sendAck(String deviceToken, String message, IDeviceEventOriginator originator)
	    throws SiteWhereAgentException {
	DeviceAcknowledge.Builder builder = DeviceAcknowledge.newBuilder();

	builder.setMessage(GOptionalString.newBuilder().setValue(message));
	DeviceAcknowledge ack = builder.build();

	getEventDispatcher().acknowledge(ack, deviceToken, getOriginatorEventId(originator));
    }

    /**
     * Convenience method for sending a measurement event to SiteWhere.
     * 
     * @param deviceToken
     * @param name
     * @param value
     * @param originator
     * @throws SiteWhereAgentException
     */
    public void sendMeasurement(String deviceToken, String name, double value, IDeviceEventOriginator originator)
	    throws SiteWhereAgentException {
	DeviceMeasurement.Builder builder = DeviceMeasurement.newBuilder();

	builder.setMeasurementName(GOptionalString.newBuilder().setValue(name));
	builder.setMeasurementValue(GOptionalDouble.newBuilder().setValue(value));

	DeviceMeasurement measurement = builder.build();

	getEventDispatcher().sendMeasurement(measurement, deviceToken, getOriginatorEventId(originator));
    }

    /**
     * Convenience method for sending a location event to SiteWhere.
     * 
     * @param deviceToken
     * @param originator
     * @param latitude
     * @param longitude
     * @param elevation
     * @param originator
     * @throws SiteWhereAgentException
     */
    public void sendLocation(String deviceToken, double latitude, double longitude, double elevation,
	    IDeviceEventOriginator originator) throws SiteWhereAgentException {
	DeviceLocation.Builder builder = DeviceLocation.newBuilder();

	builder.setLatitude(GOptionalDouble.newBuilder().setValue(latitude));
	builder.setLongitude(GOptionalDouble.newBuilder().setValue(longitude));
	builder.setElevation(GOptionalDouble.newBuilder().setValue(elevation));

	DeviceLocation location = builder.build();

	getEventDispatcher().sendLocation(location, deviceToken, getOriginatorEventId(originator));
    }

    /**
     * Convenience method for sending an alert event to SiteWhere.
     * 
     * @param deviceToken
     * @param alertType
     * @param message
     * @param originator
     * @throws SiteWhereAgentException
     */
    public void sendAlert(String deviceToken, String alertType, String message, IDeviceEventOriginator originator)
	    throws SiteWhereAgentException {
	DeviceAlert.Builder builder = DeviceAlert.newBuilder();

	builder.setAlertType(GOptionalString.newBuilder().setValue(alertType));
	builder.setAlertMessage(GOptionalString.newBuilder().setValue(message));

	DeviceAlert alert = builder.build();

	getEventDispatcher().sendAlert(alert, deviceToken, getOriginatorEventId(originator));
    }

    /**
     * Gets event id of the originating command if available.
     * 
     * @param originator
     * @return
     */
    protected String getOriginatorEventId(IDeviceEventOriginator originator) {
	if (originator == null) {
	    return null;
	}
	return originator.getEventId().toString();
    }
}