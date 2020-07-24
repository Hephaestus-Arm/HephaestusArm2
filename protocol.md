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
| Contents upstream |1962 ||


## Float packets

An example float packet from the warehouse robot looks like this:

| |ID | float |float |float |float |float |float |
|--- |--- | --- |--- | --- |--- | --- | --- |
| downstream Bytes |4 | 4 | 4 | 4 | 4 | 4 | 4 |
| Contents downstream |1936 | pickup area | pickup x | pickup z | drop off area | drop off  x | drop off  z |
| upstream Bytes |4 | 0 |
| Contents upstream |1936 | ---|

