# 1 Hephaestus Arm v2

This robot is for use in RBE 3001 as a teaching platform for robot kinematics and trajectory planning and image recognition. 

```
Replace this with Pic of arm
```


## 1.1 READ THIS SECTION BEFORE YOU DO ANYTHING

You need to follow all instructions in this guide **in order**, doing steps out of order means you may need to take apart the robot or risk breaking a component. 

Read through each section completely and then do the actions in the guide. Doing this will make sure you have an understanding of why and what you are doing. 

## 1.2 Dependancies and Tools

### 1.2.1 Linux Install
You need to install Ubuntu 18.04 on your personal computer

[How to install Ubuntu](https://github.com/arjungandhi/3001-Ubuntu-Install)

The rest of the guide assumes you have a base familiarity with the linux terminal and Ubuntu.

If you do not here are resources to help you get familiar

* [Ubuntu UI Help](https://youtu.be/lmeDvSgN6zY?t=68)
* [Terminal Basics](https://ubuntu.com/tutorials/command-line-for-beginners#1-overview)





### 1.2.2 Some Basic Programs 

Once you have Linux open a terminal (Ctrl+Alt+t) and run the following commands 

```
	sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 71EA898B 
	sudo apt-get install software-properties-common
	sudo add-apt-repository "deb http://ppa.launchpad.net/mad-hephaestus/commonwealthrobotics/ubuntu bionic main" -y
	sudo apt-get update 
	sudo apt-get install bowlerstudio curl git putty  zip unzip lightdm
	sudo dpkg-reconfigure  lightdm 
```

Doing so will install some programs that you need for later steps, as well as configure your login manager to avoid a known bug with the default one.

The programs installed are
* BowlerStudio (Used for calibration and modeling of the robot arm)
* curl (allows you to get files from the internet)
* git (allows you to access and work with code repositories)
* zip/unzip (allows you to zip and unzip files. duh.)
* lightdm (an alternative login manager)

### 1.2.3 Matlab

[Guide To Install Matlab](https://github.com/Hephaestus-Arm/RBE3001_Matlab)

### 1.2.4 Required Tools 

Tools in this section are stuff you **will** need to complete the build

Items that start with :wrench: are found in the [Bookstore Tool Kit](https://wpi.bncollege.com/shop/BNCB_TextbookDetailView?displayStoreId=32554&urlRequestType=Base&catalogId=10001&productId=650018123733&langId=-1&partNumber=98_844002999&storeId=32554&sectionId=97501629&item=N).

* [Needle Nose Pliers](https://www.amazon.com/Tools-VISE-GRIP-Pliers-6-Inch-2078216/dp/B000A0OW2M/ref=sr_1_3?dchild=1&keywords=needle+nose+pliers&qid=1598832659&s=industrial&sr=1-3)

* [Ph1 Screw Driver](https://www.amazon.com/Wera-05118024001-Kraftform-Electronics-Screwdriver/dp/B0001P18M8/ref=sr_1_3?crid=5UV2I9OLZR1P&dchild=1&keywords=ph1+screwdriver&qid=1598832754&sprefix=ph1+s%2Caps%2C181&sr=8-3)
:wrench:
* :wrench: [Wire Stripper](https://www.pololu.com/product/1923)

* :wrench: [Soldering Iron](https://www.sparkfun.com/products/14456)

* :wrench: [Soldering Stand](https://www.sparkfun.com/products/9477)

* :wrench: [Lead Free Solder](https://www.digikey.com/product-detail/en/aven-tools/17551LF/243-1341-ND/5252791)

* [Double Sided Foam Tape](https://www.amazon.com/Scotch-Mounting-0-75-inch-350-inches-110-LongDC/dp/B009NP1OBC)

* [Wire Kit](https://www.amazon.com/REXQualis-Breadboard-Assorted-Prototyping-Circuits/dp/B081H2JQRV)

### 1.2.4 Useful Tools 

Items that start with :wrench: are found in the [Bookstore Tool Kit](https://wpi.bncollege.com/shop/BNCB_TextbookDetailView?displayStoreId=32554&urlRequestType=Base&catalogId=10001&productId=650018123733&langId=-1&partNumber=98_844002999&storeId=32554&sectionId=97501629&item=N).

Tools in this section are stuff you **might** need to complete the build

* :wrench: [Solder Wick](https://www.sparkfun.com/products/9327)

* [Zipties](https://www.amazon.com/Cable-Nylon-Locking-Pieces-Black/dp/B07VRSQ6YL)

* :wrench: [Digital Multi Meter](https://www.sparkfun.com/products/12966)

* [220 Grit Sandpaper](https://www.amazon.com/Fandeli-36027-Multipurpose-Sandpaper-25-Sheet/dp/B00WSVNHBS/ref=sr_1_2?crid=1YRPC72JKS2L9&dchild=1&keywords=220+sandpaper&qid=1598832549&s=industrial&sprefix=220+s%2Cindustrial%2C169&sr=1-2)

Feel free to source these tools from wherever is nearby and cheap, this is just the first amazon link or what was included in the tools kit. 


# 3 Build the electronics

[Wire the electronics](electronics.md)

# 4 Build the arm

[Build instructions](assembly.md)

# 5 Calibrating the arm

![Calibration Pose](armCalibration.png) 

![Calibration Pose](photos/calibrationPose.jpg) 

From now on, all you need to do to calibrate is move the robot to the  Calibrate pose and hit the calibration button on the breadboard. You will need to  calibrate the robot each time the ItsyBitsy is power cycled. 

# 6 Communication

[Communication Protocol](protocol.md)

# 7 Matlab Setup

[Setup Matlab and begin working](https://github.com/Hephaestus-Arm/RBE3001_Matlab)

# 8 Bill Of Materials

## Main Kit:

3x motor https://www.amazon.com/dp/B081CTX6DM/ref=twister_B0817ZKWF9

```NO Engineering substitutions here```

1x power supply: https://www.digikey.com/product-detail/en/mean-well-usa-inc/GST60A07-P1J/1866-2147-ND/7703710

```Engineering substitutions for cost ok here```

1x Barrel Jack adapter: https://www.sparkfun.com/products/10288

```Engineering substitutions for cost ok here```

1x Adafruit ItsyBitsy https://www.adafruit.com/product/3800

```NO Engineering substitutions here```

1x Line Driver: https://www.digikey.com/product-detail/en/texas-instruments/SN74HC126N/296-8221-5-ND

```Engineering substitutions for cost ok here```


1x Breadboard https://www.sparkfun.com/products/12002

```Engineering substitutions for cost ok here```

4x resistors https://www.digikey.com/product-detail/en/stackpole-electronics-inc/CF14JT10K0/CF14JT10K0CT-ND/1830374

```Engineering substitutions for cost ok here```

1x Wire kit: https://www.amazon.com/MCIGICM-Breadboard-Jumper-Cables-Arduino/dp/B081GMJVPB

```Engineering substitutions for cost ok here```

1x USB cable https://www.amazon.com/AmazonBasics-Male-Micro-Cable-Black/dp/B0711PVX6Z

```Engineering substitutions for cost ok here```


1x pin headers https://www.pololu.com/product/1065

```Engineering substitutions for cost ok here```

1x Thrust bearing: https://www.mcmaster.com/5909k41

```Engineering substitutions for cost ok here```

2x Thrust bearing surface:  https://www.mcmaster.com/5909K54-5909K54

```Engineering substitutions for cost ok here```

12x inserts https://www.mcmaster.com/94180a361 (sold in packs of 50)

```Engineering substitutions for cost ok here```

3x M5x25 https://www.mcmaster.com/91292A129-91292A129 (sold in packs of 50)

```Engineering substitutions for cost ok here```

13x M5x12 https://www.mcmaster.com/91290A228 (sold in  pack of 100)

```Engineering substitutions for cost ok here```

1x Gripper servo  https://www.digikey.com/products/en/motors-solenoids-driver-boards-modules/motors-ac-dc/178?k=mg92b

   ```These are much cheaper in bulk from https://www.alibaba.com/product-detail/MG92B-Digital-Metal-Gear-Servo-For_60765301994.html ```
   
2x Button (comes in 20 pack for $2.50) https://www.digikey.com/products/en?mpart=367&v=1528 

1x Power cord https://www.digikey.com/product-detail/en/mean-well-usa-inc/YP12-YC12/1866-5006-ND/7707223

## Camera Kit

1x PSEye camera https://www.amazon.com/Sony-Station-Camera-Packaging-PlayStation-3/dp/B0735KNH2X/

```NO Engineering substitutions here, sourcing is flexible```

1x Adjustable stand https://www.amazon.com/Magnetic-Adjustable-Indicator-Holder-Digital/dp/B00L5T2ZA8/

```If its possible to source something without the base, just the M8 threaded end, that would be even better if cheaper```

1x CA glue https://www.amazon.com/Starbond-Cyanoacrylate-Microtips-Woodturning-Stabilizing/dp/B00C32ME6G/

  ``` Any small super glue is fine here```

1x Ring light https://www.newegg.com/p/0SW-01GA-00025?Item=9SIAK0NAUS4868&Tpk=9SIAK0NAUS4868
  
  ```Sourcing a cheaper one is also good```

1 x nut for mounting camera stand https://www.mcmaster.com/98965A410

## Camera Kit v2

1 x https://www.adafruit.com/product/1643  $7.50

# 7 CAD and simulation

Open BowlerStudio, in the menu

```
Add device -> creatures -> from git
```
and enter
```
https://github.com/Hephaestus-Arm/HephaestusArm2.git
```
then select
```
hephaestus.xml 
```

This will generate the CAD and run the simulation. 



## STL's 

[Current Release Printable STL's](https://github.com/Hephaestus-Arm/HephaestusArm2/releases/download/0.1.0/release-0.1.0.zip)

print with supports everywhere, do not reorent parts. 

