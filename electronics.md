# Electronics Instructions

```
Replace with completed board pic
```

# 0 Setup

Before starting on this section you will need to have a working ubuntu partition and all software installed on the initial [readme](README.md), as well as all the required tools mentioned there as well.

## 0.1 Learn to Solder

If you do not know how to solder watch the video below on how to solder electrical components.

[![Alt text](https://img.youtube.com/vi/FWBCbFPXJLg/0.jpg)](https://www.youtube.com/watch?v=FWBCbFPXJLg)


# 1 Install Firmware to the ItsyBitsy

The goal of this section is to flash the needed firmware onto the Adafruit ItsyBitsy.

## 1.1 Firmware Download 

In order to run the robot we will be using the [source code](https://github.com/Hephaestus-Arm/HephaestusArm2-firmware/) provided by the lab.

However you do not need to download or compile this code we have already done that for you. 

To get the pre-complied code, visit [here](https://github.com/Hephaestus-Arm/HephaestusArm2-firmware/releases) and download the lastest version of 

```
CURRENT.UF2
```

## 1.2 Firmware Program

In order to program the ItsyBitsy we will be using Adafruits Flashdrive Emulation.

**Installing The Firmware**

1. Plug in the ItsyBitsy to your computer using the micro-usb cable in your kit.

```
pic
```

2. Open up 2 File Explorer Windows on Ubuntu.

```
pic
```

3. Navigate to the download location of the firmware in one window (Probably your `Downloads` Folder).
```
pic
```

4. *Double Click* the reset button on the ItsyBitsy. This will put it in bootloader mode. The ItsyBitsy should disappear momentarily and then show up as `ITSYM4BOOT`.
```
pic
```
5. Open ITSYM4BOOT in the other window and drag `Current.UF2` into it. 
```
pic
```
	A replace file nofication will show up click the green replace button. 

	The ItsyBitsy should then disconnect and not reappear

Congrats! You've flashed the firmware on the ItsyBitsy!

Go ahead and disconnect it from your computer. 
## 1.3 Troubleshooting

If you see

```
curcuitpython
```

as the flash drive name instead of

```
ItsyM4Boot
```

then *DOUBLE CLICK* the reset button on the ItsyBitsy. 

## 2 Setting Up the Board


1. Solder header pins onto the ItsyBitsy with headers and place it in the breadboard. Note you only need to solder pins to the long sides of the board don't worry about the back. See pic below for more reference.

```
Protip: a pair of pliers makes snapping the correct number of header pins much easier.
```

```
pic itsy in bread board 
```


2. Find the 74hc126 line driver and place it in the breadboard.

```
pic linedriver in bread board 
```

3. Use the double stick tape to attach the barrel jack adapter to the side of the breadboard. 

```
Protip: Apply pressure to the taped objects for at least 30 seconds to ensure a good bond. Cut your tape to exact sizes to ensure neatness.
```

Then wire the barrel jack termianls to their respective rails on the breadboard.
```
pic barel jack go brr

```
4. Wiring up the Smart Servo Cables

In order to connect the smart servo cables to the breadboard we need to use the double header pins. Take three double header pins and insert them into the smart servo cable.

*Note, you will need to bend the outer pins going into the servo cable a little in order to make it fit. See below pic made using pliers for reference.

```
bent servo pic
```

Repeat the above for one side of all 3 servo cables.

```
finished cable end pic 
```

5. Pinning up the gripper servo

Use a set of 3 double header pins to add pins to the end of the gripper servo wire.

```
gripper servo pic 
```

6. Wire up the breadboard following the diagram below


```
add triple smart servo to diagram and label , show power ground and signal
```

<img src="photos/hep-arm-witing.png" width="600">

```
Wired board without servo cables
```
```
Wired board with servo cables
```




## 3 LX-224 Pinout

<img src="photos/lx-224-cable-fixed.jpg" width="600">

Looking into the pins, if the notch in the keyed cable is pointing 'down'

From left to right:

```
GND , 7.5v , Serial  
```

NOTE when it is plugged into in a breadboard it looks reversed, because the image above is looking into the pin holes, but as you assemble it the pin holes face down into the breadboard.

<img src="photos/closeup.jpg" width="600">

## 4 Gripper Servo Pinout

Brown = Ground

Red   = 5v from USB

Yellow = Servo Pulse

## 5 Provision LX-224 ID numbers

This section shows how to identify the motors in the ItsyBitsy's firmware. 

### 5.1 Power up the system

Plug in one and only one motor. 

Plug in the Power cable.

### 5.2 Start Putty

Plug in the USB to the ItsyBitsy

Open putty and set it to talk to the serial port:

```
putty -serial /dev/ttyACM0 -sercfg 115200,8,n,1,N
```

<img src="photos/putty.png" width="600">

### 5.3 Read the provision state of the first motor

Type 

```
ID
```
 and hit enter. 
 
 The firmware will print out the ID of the motor connected. 
 
 ### 5.4 Provision Motor 1
 
 Now type 
 
 ```
 ID 1
 ```
 
 and hit enter to set the ID of the base motor. 
 
 Verify with step 6.3 above
 
 ### 5.5 Provision Motor 2 
 
 Unplug Motor 1 and plug in the second motor. Type: 
 
  ```
 ID 2
 ```
 
 and hit enter to set the ID of the middle motor. 
 
 Verify with step 6.3 above
 
 ### 5.6 Provision Motor 3 
 
 Unplug Motor 2 and plug in the third motor. Type: 
 
 ```
 ID 3
 ```
 
 and hit enter to set the ID of the elbow motor. 
 
 Verify with step 6.3 above
 
 Now you can plug in all 3 motors. 

## 6 Calibrate before beginning assembly

Now plug in all 3 motors and reboot the firmware. Press and hold the  Calibrate button until the red LED on pin 13 flashes quickly (at least 5 seconds) where quickly is 100ms on 100ms off. All motors will be moved to their  Calibrate pose. Keep track of which index is which. 

 ## 7 Extras
 
[74hc126 Datasheet](https://www.ti.com/lit/ds/symlink/sn74hc126.pdf?HQS=TI-null-null-digikeymode-df-pf-null-wwe&ts=1597341911818)

Text-based pinout of the board

ItsyBitsy GPIO-12 to Servo Pulse
 
ItsyBitsy GPIO-1  to 74hc126 A (see datasheet)

ItsyBitsy GPIO-0  to LX-224 Serial Pin

ItsyBitsy GPIO-9  to 74HC126 OE

74HC126 Y         to LX-224 Serial Pin

ItsyBitsy GPIO-11 to  Calibrate Switch pin 1

Calibrate Switch pin 2 to Ground

74HC126 Power to 3.3v

Connect all grounds

Barrel Jack 7.5v to LX-224 Power
