# Base Protocol SimplePacketComs

## Protocol Definition

[SimplePacketComs Theory of Operation](https://github.com/madhephaestus/SimplePacketComs)

Our firmware uses an HID implementation built on the Adafruit TinyUSB stack.
### ServoServer

Set the gripper value


| |ID | byte 0 |
|--- |--- | --- |
| downstream Bytes |4 | 1 |
| Contents downstream |1962 | Gripper value 0-180|
| upstream Bytes |4 | --- |
| Contents upstream |1962 |---|


## Set Motor Setpoints With Time

Set the setpoint of the Motors with interpolation. Time is in milliseconds. The interpolation mode determines the trajectory the motor takes. Positions are in degrees. 

| |ID | float |float |float |float |float |
|--- |--- | --- |--- | --- |--- | --- | 
| downstream Bytes |4 | 4 | 4 | 4 | 4 | 4 |
| Contents downstream |1848 | mS duration of move| interpolation mode 0=linear,1=sinusoidal | motor 1 position | motor 2 position | motor 3 position | 
| upstream Bytes |4 | --- |
| Contents upstream |1848 | ---|

