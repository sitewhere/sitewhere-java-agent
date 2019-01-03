# SiteWhere Java Agent

The SiteWhere Java agent provides a base client platform which runs on any
device that supports Java. The agent allows a device to interact with
SiteWhere over the MQTT transport by sending and receiving messages encoded
in a [Google Protocol Buffers](https://developers.google.com/protocol-buffers/)
format. The agent supports dynamic device registration and sending data events
such as measurements, locations, and alerts to SiteWhere. It also supports receiving
commands from SiteWhere and triggering Java logic based on the requests.

## Agent Usage Example

The agent project includes an example that shows how round-trip processing
is accomplished for a test device. The device registers itself as a Raspberry Pi
based on the device type token provided in the SiteWhere sample data.
Once registered, it starts an event loop that gathers the current JVM
memory statistics, sends them to SiteWhere, then waits a few seconds and
sends another batch. In a real-world scenario, the program could be monitoring
or manipulating GPIO settings to interact with sensors and actuators.
The example also implements the list of commands declared for the Raspberry Pi
device specification, so if commands come in from SiteWhere, the corresponding
methods are invoked on the agent.

### SiteWhere Tenant Configuration

The default SiteWhere tenant configuration should not require any changes in order
to interact with the Java agent. The agents sends messages encoded with Google Protocol
Buffers over the MQTT protocol. In the tenant configuration, there is an event source
declared as shown below that listens for this type of message:

```xml
<!-- Event source for protobuf messages over MQTT -->
<sw:mqtt-event-source sourceId="protobuf" hostname="localhost"
	port="1883" topic="SiteWhere/${tenantId}/input/protobuf">
	<sw:protobuf-event-decoder/>
</sw:mqtt-event-source>
```

On the outbound side, command processing is configured to send messages to Java-oriented
devices such as Android and Raspberry Pi using a hybrid message format that combines
Google Protocol Buffers for system messages and Java serialization for custom commands
declared in the device specification. This allows new commands to be added in the specification
and implemented by only adding a corresponding Java method on the device running the agent.
The tenant configuration for outbound command processing looks something like:

```xml
<sw:command-routing>
	<sw:specification-mapping-router defaultDestination="default">
		<sw:mapping specification="d2604433-e4eb-419b-97c7-88efe9b2cd41"
			destination="hybrid"/>
		<sw:mapping specification="7dfd6d63-5e8d-4380-be04-fc5c73801dfb"
			destination="hybrid"/>
		<sw:mapping specification="5a95f3f2-96f0-47f9-b98d-f5c081d01948"
			destination="hybrid"/>
	</sw:specification-mapping-router>
</sw:command-routing>

<!-- Outbound command destinations -->
<sw:command-destinations>

	<!-- Delivers commands via MQTT -->
	<sw:mqtt-command-destination destinationId="default"
		hostname="localhost" port="1883">
		<sw:protobuf-command-encoder/>
		<sw:hardware-id-topic-extractor commandTopicExpr="SiteWhere/commands/%s"
			systemTopicExpr="SiteWhere/system/%s"/>
	</sw:mqtt-command-destination>

	<!-- Used for devices that expect hybrid protobuf/Java invocations -->
	<sw:mqtt-command-destination destinationId="hybrid"
		hostname="localhost" port="1883">
		<sw:java-protobuf-hybrid-encoder/>
		<sw:hardware-id-topic-extractor commandTopicExpr="SiteWhere/commands/%s"
			systemTopicExpr="SiteWhere/system/%s"/>
	</sw:mqtt-command-destination>

</sw:command-destinations>
```

### Running the Example
The agent project includes a jar file with the compiled code from the project including
the required dependencies. To run the example agent, download (or build) the jar file
and copy the example configuration file from the **config** directory. The contents
are in the standard Java properties file format and will be similar to the values
below:

```INI
mqtt.hostname=localhost
command.processor.classname=com.example.ExampleCommandProcessor
tenant=default
device.token=123-TEST-439829343897429
area.token=southeast
customer.token=acme
device.type.token=galaxytab3
```

If you are using a cloud instance for SiteWhere, edit the MQTT hostname to correspond to 
the IP address or hostname of the remote instance. You can also change the device token
to the value the device should be registered under in SiteWhere. The device type token 
indicates the type of hardware the device uses. The default value corresponds to
the Raspberry Pi specification in the default SiteWhere sample data.

Start the agent by entering:

```sh
java -jar sitewhere-java-agent-x.y.z.jar
```

Note that **x.y.z** above should be replaced by the version number of the agent. The
agent will start and the logs produced in the console will reflect that the device
has registered with SiteWhere successfully. The next step is to send a command from
SiteWhere to affect the device.

Open the SiteWhere administrative application, log in, then click the green arrow
next to the first site in the list. A Raspberry Pi device should appear at the top 
of the assignments list along with the label **Unassociated Device**, which 
indicates it has not been associated with an asset. To send a command to the 
agent from SiteWhere, click the green arrow on the device assignment and open
the **Command Invocations** tab. Click the **Invoke Command** button and choose
**Ping** from the list of commands and **Invoke** to invoke it. A new command
invocation will show up at the top of the list and there should be output in the
agent log indicating that it sent a response to the command. To see the response
that was sent from the agent, click the icon to the right of the command invocation
in the administrative interface to view the invocation.	Click the **Responses**
tab and there should be a response of **'Acknowledged'**.

At this point the device has registered with SiteWhere, SiteWhere has sent a command
which executed Java code on the device, and the device sent a response to SiteWhere 
which was correlated with the original command.

### Building the Example

The example agent is written in Java and may be compiled and packaged using 
[Gradle](https://gradle.org/). Execute the following command to build and
package the agent:

```sh
gradle clean shadowJar
```

The results of the build are located in the **build/libs** folder under the root. The jar
file will be named **sitewhere-java-agent-x.y.z.jar** (where x.y.z is the version).
Once built, the jar can be used as mentioned in the previous section to run the agent.
