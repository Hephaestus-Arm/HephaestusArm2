# Hephaestus Arm v2

This robot is for use in RBE 3001 as a teaching platform for robot kinematics and trajecttory planning. 

![Hephaestus Arm V2](cadImage.png)

# CAD and simulation

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

# Firmware

Use Arduino to program the Teensy 3.5

https://github.com/Hephaestus-Arm/HephaestusArm2-firmware

# Camera Calibaration

Get the calibration document:

![Calibration Image](./CalibrationImageDocument.svg)

and print it on an 8.5x11 inch piece of paper. Put double sided tape on the red section. Carefully place the robots base on the paper alligned to the red section coving the red completely. once firmly attached to the base, assemble the base to the wood board, pushing the bolts through the paper, starting with the 2 bolts to the rear of the robot. Start the holes for the bolts using a sharp pencil. Once the robot is bolted down, secure the paper to the wood board using glue or clear tape. The camer should be placed such that it can see the whole piece of paper. 

# BOM and assembly

Main Electronics Kit:

3x motor https://www.amazon.com/LewanSoul-Connectors-Equipped-Position-Temperature/dp/B0817X3Z3W 

1x power supply: https://www.digikey.com/product-detail/en/mean-well-usa-inc/GST60A07-P1J/1866-2147-ND/7703710

1x Barrel Jack adapter: https://www.addicore.com/female-barrel-with-terminals-p/ad470.htm

1x Arduino Teensy 3.5 https://www.digikey.com/product-detail/en/sparkfun-electronics/DEV-14056/1568-1464-ND/6569368

1x Line Driver: https://www.digikey.com/product-detail/en/texas-instruments/SN74HC126N/296-8221-5-ND

1x Breadboard https://www.digikey.com/product-detail/en/dfrobot/FIT0096/1738-1326-ND/7597069

4x resistors https://www.digikey.com/product-detail/en/stackpole-electronics-inc/CF14JT10K0/CF14JT10K0CT-ND/1830374

1x Wire kit: https://www.amazon.com/MCIGICM-Breadboard-Jumper-Cables-Arduino/dp/B081GMJVPB

1x USB cable https://www.amazon.com/AmazonBasics-Male-Micro-Cable-Black/dp/B0711PVX6Z

1x double sided tape https://www.amazon.com/3M-03609-Scotch-Mount-Molding-Tape/dp/B000BO913C

1x CA glue https://www.amazon.com/Starbond-Cyanoacrylate-Microtips-Woodturning-Stabilizing/dp/B00C32ME6G/

1x pin headers https://www.amazon.com/Generic-Breakaway-Headers-Length-Centered/dp/B015KA0RRU

Thrust bearing:

1x https://www.mcmaster.com/5909k41

2x https://www.mcmaster.com/5909K54-5909K54

Some M5 bolts and heated inserts

8x https://www.mcmaster.com/94180a361 (sold in packs of 50)

8x https://www.mcmaster.com/91292A129-91292A129 (sold in packs of 50)

Gripper servo and regulator for the servo

1x https://www.digikey.com/products/en/motors-solenoids-driver-boards-modules/motors-ac-dc/178?k=mg92b

1x https://www.digikey.com/product-detail/en/adafruit-industries-llc/973/1528-1171-ND/5353631

1x https://www.amazon.com/Ship-Hobbywing-Switch-mode-UBEC-Lowest/dp/B008ZNWOYY

Camera Kit

1x https://www.amazon.com/Sony-Station-Camera-Packaging-PlayStation-3/dp/B0735KNH2X/

1x https://www.amazon.com/Magnetic-Adjustable-Indicator-Holder-Digital/dp/B00L5T2ZA8/

1x https://www.amazon.com/Ringlight-Dimmable-Streaming-YouTube-Reading/dp/B07WZCNCP4

