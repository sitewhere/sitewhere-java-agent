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
 * Constants for agent configuration properties.
 * 
 * @author Derek
 */
public interface IAgentConfiguration {

    /** Property for command processor classname */
    public static final String COMMAND_PROCESSOR_CLASSNAME = "command.processor.classname";

    /** Property for Tenant Id */
    public static final String TENANT = "tenant";

    /** Property for device token */
    public static final String DEVICE_TOKEN = "device.token";

    /** Propertie for area token */
    public static final String AREA_TOKEN = "area.token";

    /** Customer token */
    public static final String CUSTOMER_TOKEN = "customer.token";

    /** Device type token */
    public static final String DEVICE_TYPE_TOKEN = "device.type.token";

    /** Property for MQTT hostname */
    public static final String MQTT_HOSTNAME = "mqtt.hostname";

    /** Property for MQTT port */
    public static final String MQTT_PORT = "mqtt.port";

    /** Property for outbound SiteWhere MQTT topic */
    public static final String MQTT_OUTBOUND_SITEWHERE_TOPIC = "mqtt.outbound.sitewhere.topic";

    /** Property for inbound SiteWhere MQTT topic */
    public static final String MQTT_INBOUND_SITEWHERE_TOPIC = "mqtt.inbound.sitewhere.topic";

    /** Property for inbound command MQTT topic */
    public static final String MQTT_INBOUND_COMMAND_TOPIC = "mqtt.inbound.command.topic";
}