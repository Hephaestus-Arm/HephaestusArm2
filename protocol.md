# Base Protocol SimplePacketComs

## Protocol Definition

[SimplePacketComs Theory of Operation](https://github.com/madhephaestus/SimplePacketComs)

Our firmware uses an HID implementation built on the Adafruit TinyUSB stack.

## Float packets

An example float packet from the warehouse robot looks like this:

| |ID | float |float |float |float |float |float |
|--- |--- | --- |--- | --- |--- | --- | --- |
| downstream Bytes |4 | 4 | 4 | 4 | 4 | 4 | 4 |
| Contents downstream |1936 | pickup area | pickup x | pickup z | drop off area | drop off  x | drop off  z |
| upstream Bytes |4 | 0 |
| Contents upstream |1936 | ---|

