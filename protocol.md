# Base Protocol SimplePacketComs

## Protocol Definition

[SimplePacketComs Theory of Operation](https://github.com/madhephaestus/SimplePacketComs)

Our firmware uses an HID implementation built on the Adafruit TinyUSB stack.

## Timing

When you send a command, what you are doing is queing up a packet to be sent. When you read a packet, you are reading the most recent packet recived. There is a thread internal to the command library that will manage the sending and reciving of packets. The round trip time for a packet from the library to the device and back is 2ms max. There is a delay between when you send a packet and when the latest response from that packet can be read. To send a request and wait 3ms, then read it back will always result in the latest data Alternantly using a state machine to manage sending and reciving to prevent busy waits is a much better option. 


# ServoServer

Set the gripper value

NOTE this uses one byte as the value, not a float.


| |ID | byte  |
|--- |--- | --- |
| downstream Bytes |4 | 1 |
| Contents downstream |1962 | Gripper value 0-180|
| upstream Bytes |4 | --- |
| Contents upstream |1962 |---|


# Set Motor Setpoints With Time

Set the setpoint of the Motors with interpolation. Time is in milliseconds. The interpolation mode determines the trajectory the motor takes. Positions are in degrees. 

| |ID | float |float |float |float |float |
|--- |--- | --- |--- | --- |--- | --- | 
| downstream Bytes |4 | 4 | 4 | 4 | 4 | 4 |
| Contents downstream |1848 | mS duration of move| interpolation mode 0=linear,1=sinusoidal | motor 1 target position | motor 2 target  position | motor 3 target  position | 
| upstream Bytes |4 | --- |
| Contents upstream |1848 | ---|

# Get Positions and Setpoint

| |ID | float| float|float | float|float |float |float|
|--- |--- | --- |--- | --- |--- | --- |  --- | 
| downstream Bytes |4 | --- |
| Contents downstream |1910 | --- |
| upstream Bytes |4 | 4 | 4 |4 |4 |4 |4 |4 |
| Contents upstream |1910 | the number of motors | motor 1 Setpoint|  motor 1 position |motor 2 Setpoint | motor 2 position |motor 3 Setpoint | motor 3 position | 

# Get Velocity data

| |ID |  float|float | float|float |float |float|float |float |float|
|--- |--- | --- |--- | --- |--- | --- |  --- | --- | --- |  --- | 
| downstream Bytes |4 | --- |
| Contents downstream |1822 | --- |
| upstream Bytes |4 | 4 |4 |4 |4 |4 |4 |4 |4 |4 |
| Contents upstream |1822 |  motor 1 Velocity Mode Setpoint|  motor 1 velocity |motor 1 computed effort | motor 2 Velocity Mode Setpoint|  motor 2 velocity |motor 2 computed effort |motor 3 Velocity Mode Setpoint|  motor 3 velocity |motor 3 computed effort |

# ERROR packet

If you send an ID that the device doesn't understand you will get this packet back. It happens when you send the wrong ID number, or forget to attach servers in the firmware to the coms stack. 


| |ID | |
|--- |--- | --- |
| downstream Bytes |4 | --- |
| Contents downstream |unknown by server | --- |
| upstream Bytes |4 | --- |
| Contents upstream |99 |---|

