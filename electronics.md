# Electronics introduction

<img src="photos/2.jpg" width="600">

# 1 Firmware


Use the Adafruit flash drive emulation to program the Adafruit ItsyBitsy. The firmware source is found at 

https://github.com/Hephaestus-Arm/HephaestusArm2-firmware/

but you will not need to compile the source. the FW is distributed as

## 1.1 Firmware Download

https://github.com/Hephaestus-Arm/HephaestusArm2-firmware/releases

download the latest version of:

```
CURRENT.UF2
```

## 1.2 Firmware program

Double click the reset button on the ItsyBitsy while plugged in. It will show up as a flash drive to the computer. Drag and drop 'CURRENT.UF2' onto the root of the flash drive. 

The electronics for the Hephaerstus Arm are made using an Adafruit ItsyBitsy, a line driver and a button. 

## 2 Components

Solder up the ItsyBitsy with headers and place it in the breadboard.

Find the 74hc126 line driver and place it in the breadboard https://www.digikey.com/product-detail/en/texas-instruments/SN74HC126N/296-8221-5-ND

Use the double stick tape to attach the barrel jack adapter to the breadboard. 

Use the double sided headers to line up 3 LX-224 connections 

Use the double sided headers to add a 3 pin servo header 

NOTE the servo and LX-224 motors use DIFFERENT voltages for power. 

## 3 LX-224 Pinout

If the notch in the keyed cable is pointing 'down'

From left to right:

```
Serial  , 7.5v  , GND
```

## 4 Servo pinout

Brown = ground

red   = 5v from USB

Yellow = Servo Pulse

## 5 PINOUT

ItsyBitsy GPIO-12 to Servo Pulse
 
ItsyBitsy GPIO-1  to 74hc126 A (see datasheet)

ItsyBitsy GPIO-0  to LX-224 Serial Pin

ItsyBitsy GPIO-9  to 74HC126 OE

74HC126 Y         to LX-224 Serial Pin

ItsyBitsy GPIO-9  to Home Switch pin 1

Home Switch pin 2 to Ground

74HC126 Power to 3.3v

Connect all grounds

Barrel Jack 7.5v to LX-224 Power

## 6 Provision LX-224 ID numbers

Program the FW to the ItsyBitsy and open Putty connecting to the serial port provided by the firmware. 

Plug in one and only one motor. 

Type 

```
ID
```
 and hit enter. 
 
 The firmware will print out the ID of the motor connected. 
 
 Now type 
 
 ```
 ID 1
 ```
 
 and hit enter to set the ID of the base motor. Unplug and plug in the second motor. Type: 
 
  ```
 ID 2
 ```
 
 and hit enter to set the ID of the middle motor. Unplug and plug in the second motor. Type: 
 
 ```
 ID 3
 ```
 
 and hit enter to set the ID of the elbow motor. Unplug and plug in the second motor. 

## 7 Calibrate before beginning assembly

Now plug in all 3 motors and reboot the firmware. Press and hold the home button until the red LED on pin 13 flashes quickly (at least one second) where quickly is 100ms on 100ms off. All motors will be moved to their home pose. Keep track of which index is which. 

 


