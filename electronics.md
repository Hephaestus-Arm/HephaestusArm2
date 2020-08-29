# Electronics introduction

<img src="photos/2.jpg" width="600">

# 0 depenancies and tools


## 0.1 Linux install

Do NOT use a VM. Native install is nessissary for access to the robot arm. 

* A PC running Ubuntu 18.04 linux
```
	sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 71EA898B 
	sudo apt-get install software-properties-common
	sudo add-apt-repository "deb http://ppa.launchpad.net/mad-hephaestus/commonwealthrobotics/ubuntu bionic main" -y
	sudo apt-get update 
	sudo apt-get install bowlerstudio curl git putty  zip unzip lightdm
	sudo dpkg-reconfigure  lightdm 
```
Select lightdm as teh default. 

## 0.2 Tools

* Access to tools listed here: https://github.com/WPIRoboticsEngineering/ZenRobotBuildKit#tools-kit-online-only-suggested



## Optional

* [Sloeber to compile firmware](https://github.com/WPIRoboticsEngineering/RobotInterfaceBoard/blob/master/InstallEclipse.md#linux-bundled-sloeber)

## 0.3 Learn to Solder


[![Alt text](https://img.youtube.com/vi/FWBCbFPXJLg/0.jpg)](https://www.youtube.com/watch?v=FWBCbFPXJLg)


# 1 Firmware


Use the Adafruit flash drive emulation to program the Adafruit ItsyBitsy. The firmware source is found at 

[Source code for reference only](https://github.com/Hephaestus-Arm/HephaestusArm2-firmware/)

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

## 1.3 Troubleshooting

If you see

```
curcuitpython
```

as the flash drive ame instead of

```
ItsyM4Boot
```

then you didnt *DOUBLE CLICK* the button on the ItsyBItsy. 

## 2 Components

<img src="photos/hep-arm-witing.png" width="600">

Solder up the ItsyBitsy with headers and place it in the breadboard.

Find the [74hc126](https://www.digikey.com/product-detail/en/texas-instruments/SN74HC126N/296-8221-5-ND) line driver and place it in the breadboard 

[74hc126 Datasheet](https://www.ti.com/lit/ds/symlink/sn74hc126.pdf?HQS=TI-null-null-digikeymode-df-pf-null-wwe&ts=1597341911818)

Use the double stick tape to attach the barrel jack adapter to the breadboard. 

Use the double sided headers to line up 3 LX-224 connections 

Use the double sided headers to add a 3 pin servo header 

NOTE the servo and LX-224 motors use DIFFERENT voltages for power. 

## 3 LX-224 Pinout

<img src="photos/lx-224-cable-fixed.jpg" width="600">

Looking into the pins, if the notch in the keyed cable is pointing 'down'

From left to right:

```
GND , 7.5v , Serial  
```

## 4 Servo pinout

Brown = ground

red   = 5v from USB

Yellow = Servo Pulse

## 5 PINOUT

<img src="photos/2.jpg" width="600">
<img src="photos/hep-arm-witing.png" width="600">

ItsyBitsy GPIO-12 to Servo Pulse
 
ItsyBitsy GPIO-1  to 74hc126 A (see datasheet)

ItsyBitsy GPIO-0  to LX-224 Serial Pin

ItsyBitsy GPIO-9  to 74HC126 OE

74HC126 Y         to LX-224 Serial Pin

ItsyBitsy GPIO-11  to  Calibrate Switch pin 1

 Calibrate Switch pin 2 to Ground

74HC126 Power to 3.3v

Connect all grounds

Barrel Jack 7.5v to LX-224 Power

## 6 Provision LX-224 ID numbers

Program the FW to the ItsyBitsy and open Putty connecting to the serial port provided by the firmware. 

### 6.1 Start Putty

Plug in the USB to the ItsyBitsy

Open putty and set it to talk to the serial port:

```
putty -serial /dev/ttyACM0 -sercfg 115200,8,n,1,N
```

<img src="photos/putty.png" width="600">

### 6.2 Power up the system

Plug in one and only one motor. 

Plug in the Power cable.

### 6.3 Read the provision state of the first motor

Type 

```
ID
```
 and hit enter. 
 
 The firmware will print out the ID of the motor connected. 
 
 ### 6.4 Provision Motor 1
 
 Now type 
 
 ```
 ID 1
 ```
 
 and hit enter to set the ID of the base motor. 
 
 Verify with step 6.3 above
 
 ### 6.5 Provision Motor 2 
 
 Unplug Motor 1 and plug in the second motor. Type: 
 
  ```
 ID 2
 ```
 
 and hit enter to set the ID of the middle motor. 
 
 Verify with step 6.3 above
 
 ### 6.6 Provision Motor 3 
 
 Unplug Motor 2 and plug in the third motor. Type: 
 
 ```
 ID 3
 ```
 
 and hit enter to set the ID of the elbow motor. 
 
 Verify with step 6.3 above
 
 Now you can plug in all 3 motors. 

## 7 Calibrate before beginning assembly

Now plug in all 3 motors and reboot the firmware. Press and hold the  Calibrate button until the red LED on pin 13 flashes quickly (at least one second) where quickly is 100ms on 100ms off. All motors will be moved to their  Calibrate pose. Keep track of which index is which. 

 


