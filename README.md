# 1 Hephaestus Arm v2

This robot is for use in RBE 3001 as a teaching platform for robot kinematics and trajecttory planning. 

![Hephaestus Arm V2](cadImage.png)

# 2 CAD and simulation

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

# 3 Firmware

Install the firmware using Arduino with the Teensy driver installed.

## 3.1 Arduino Download

Install from here: https://github.com/WPIRoboticsEngineering/RobotInterfaceBoard/blob/master/InstallEclipse.md

## 3.2 Firmware Download

Use Arduino to program the Teensy 3.5

https://github.com/Hephaestus-Arm/HephaestusArm2-firmware

# 4 Calibrate Arm Servos

Before you cna calibrate the arm, the servos need to be within their range of motion. this is checked by the Teensy firmware and enforced at the chips boot. if the pose of the motor is such that it could not reach the full range, the motor is moved by the controller to the correct home pose location and stops the execution of the code. THis is a hardware fault and the controller will not continue until the hardware configuration is changed. the process to get this all configured in the first place is listed below. Note that you only need to adjust the horn location if you do not do the procedure below correctly. 

## 4.1 first time calibration during assembly

While assembling the robot, keep all of the servo horns detatched until the last step.

Power up the arm and connect the electronics. Press the home button and the motors will move to the home positions they need to be at to achieve the calibration pose. The pose is depicted below

![Calibration Pose](armCalibration.png) 

Calibrate one motor at a time to its calibration pose then home the robot. Reset the Teensy in between by plug cycling its usb. Once the base Motor is calibrated, add its horn and tighten the horn screw very well.

Next insert the second motor and arrange the loose links so that the tip is seated into the calibration pose notch in the base. After you home the second link, add the horn for the second link. 

Finally home for a third time, this time add the last links horn. 

## 4.2 Calibrate each time you power up the Teensy

From now on, all you need to do to calibrate is move the robot to the home pose and hit the calibration button on the controller. You will need to home the robot each time the Teensy is power cycled. 


# 5 Camera Calibaration

Get the calibration document:

![Calibration Image](./CalibrationImageDocument.svg)

and print it on an 8.5x11 inch piece of paper. Put double sided tape on the red section. Carefully place the robots base on the paper alligned to the red section coving the red completely. once firmly attached to the base, assemble the base to the wood board, pushing the bolts through the paper, starting with the 2 bolts to the rear of the robot. Start the holes for the bolts using a sharp pencil. Once the robot is bolted down, secure the paper to the wood board using glue or clear tape. The camer should be placed such that it can see the whole piece of paper. 

# 6 BOM and assembly

## Main Kit:

3x motor https://www.amazon.com/dp/B081CTX6DM/ref=twister_B0817ZKWF9

```NO Engineering substitutions here```

1x power supply: https://www.digikey.com/product-detail/en/mean-well-usa-inc/GST60A07-P1J/1866-2147-ND/7703710

```Engineering substitutions for cost ok here```

1x Barrel Jack adapter: https://www.sparkfun.com/products/10288

```Engineering substitutions for cost ok here```

1x Arduino Teensy 3.5 https://www.digikey.com/product-detail/en/sparkfun-electronics/DEV-14056/1568-1464-ND/6569368

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

1x double sided tape https://www.amazon.com/Scotch-Double-Dispenser-Standard-237/dp/B0000DH8IT/

```Engineering substitutions for cost ok here```

1x pin headers https://www.pololu.com/product/1065

```Engineering substitutions for cost ok here```

1x Thrust bearing: https://www.mcmaster.com/5909k41

```Engineering substitutions for cost ok here```

2x Thrust bearing surface:  https://www.mcmaster.com/5909K54-5909K54

```Engineering substitutions for cost ok here```

12x inserts https://www.mcmaster.com/94180a361 (sold in packs of 50)

```Engineering substitutions for cost ok here```

8x M5x25 https://www.mcmaster.com/91292A129-91292A129 (sold in packs of 50)

```Engineering substitutions for cost ok here```

1x Gripper servo  https://www.digikey.com/products/en/motors-solenoids-driver-boards-modules/motors-ac-dc/178?k=mg92b

   ```These are much cheaper in bulk from https://usa.banggood.com/Towerpro-MG92B-Robot-13_8g-3_5KG-Torque-Mental-Gear-Digital-Servo-p-978253.html?cur_warehouse=USA ```

## Camera Kit

1x PSEye camera https://www.amazon.com/Sony-Station-Camera-Packaging-PlayStation-3/dp/B0735KNH2X/

```NO Engineering substitutions here, sourcing is flexible```

1x Adjustable stand https://www.amazon.com/Magnetic-Adjustable-Indicator-Holder-Digital/dp/B00L5T2ZA8/

```If its possible to source something without the base, just the 5/16-18 threaded end, that would be even better if cheaper```

1x CA glue https://www.amazon.com/Starbond-Cyanoacrylate-Microtips-Woodturning-Stabilizing/dp/B00C32ME6G/

  ``` Any small super glue is fine here```

1x Ring light https://www.newegg.com/p/0SW-01GA-00025?Item=9SIAK0NAUS4868&Tpk=9SIAK0NAUS4868
  
  ```Sourcing a cheaper one is also good```

1 x nut for mounting camera stand https://www.mcmaster.com/90975A030

