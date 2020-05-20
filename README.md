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

# Calibrate Arm Servos

Before you cna calibrate the arm, the servos need to be within their range of motion. this is checked by the Teensy firmware and enforced at the chips boot. if the pose of the motor is such that it could not reach the full range, the motor is moved by the controller to the correct home pose location and stops the execution of the code. THis is a hardware fault and the controller will not continue until the hardware configuration is changed. the process to get this all configured in the first place is listed below. Note that you only need to adjust the horn location if you do not do the procedure below correctly. 

## first time calibration during assembly

While assembling the robot, keep all of the servo horns detatched until the last step.

Power up the arm and connect the electronics. Press the home button and the motors will move to the home positions they need to be at to achieve the calibration pose. The pose is depicted below

![Calibration Pose](armCalibration.png) 

Calibrate one motor at a time to its calibration pose then home the robot. Reset the Teensy in between by plug cycling its usb. Once the base Motor is calibrated, add its horn and tighten the horn screw very well.

Next insert the second motor and arrange the loose links so that the tip is seated into the calibration pose notch in the base. After you home the second link, add the horn for the second link. 

Finally home for a third time, this time add the last links horn. 

## Calibrate each time you power up the Teensy

From now on, all you need to do to calibrate is move the robot to the home pose and hit the calibration button on the controller. You will need to home the robot each time the Teensy is power cycled. 


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

1x double sided tape https://www.amazon.com/Scotch-Double-Dispenser-Standard-237/dp/B0000DH8IT/


1x pin headers https://www.amazon.com/Generic-Breakaway-Headers-Length-Centered/dp/B015KA0RRU

Thrust bearing:

1x https://www.mcmaster.com/5909k41

2x https://www.mcmaster.com/5909K54-5909K54

Some M5 bolts and heated inserts

8x https://www.mcmaster.com/94180a361 (sold in packs of 50)

8x https://www.mcmaster.com/91292A129-91292A129 (sold in packs of 50)

Gripper servo and regulator for the servo

1x https://www.digikey.com/products/en/motors-solenoids-driver-boards-modules/motors-ac-dc/178?k=mg92b

Camera Kit

1x PSEye camera https://www.amazon.com/Sony-Station-Camera-Packaging-PlayStation-3/dp/B0735KNH2X/

1x Adjustable stand https://www.amazon.com/Magnetic-Adjustable-Indicator-Holder-Digital/dp/B00L5T2ZA8/

1x CA glue https://www.amazon.com/Starbond-Cyanoacrylate-Microtips-Woodturning-Stabilizing/dp/B00C32ME6G/

1x Ring light https://www.amazon.com/Ringlight-Dimmable-Streaming-YouTube-Reading/dp/B07WZCNCP4

