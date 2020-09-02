# Assemble the robot

<img src="photos/85.jpg" width="600">

## 0 Preamble

Before you start this section you should have already:
1. Installed Ubuntu
2. Installed Extra Packages
 (bowlerstudio, curl, git, putty,  zip, unzip, and lightdm)
3. Built The Electronics
4. Provisioned and Calibrated the motors

If you have not done all of the above steps go back complete them and then return to this section

## 1 Pictures of Parts 

Small Servo Screw

<img src="photos/short_servo_screw.jpg" width="300">
 
Servo Horn Bolt

<img src="photos/horn_bolt.jpg" width="300">

Passive Support Screw

<img src="photos/support_screw.jpg" width="300">

Long Bolt

<img src="photos/long_bolt.jpg" width="300">

Short Bolt

<img src="photos/short_bolt.jpg" width="300">

## 2 3D Printed Part Prep

Each 3D printed part comes with support material as shown in the images below, go through each 3D printed part and remove the support material. 

```
Protip: A pair of needle pliers is probably the best tool for the job here, you can use the tip to jam into the support material and then twist and pull the jaws to remove it out.
Protip: A sharp pointy object like an icepick can also be useful for removing the support material from small holes. 
```

<img src="photos/84.jpg" width="300">
<img src="photos/83.jpg" width="300">
<img src="photos/82.jpg" width="300">
<img src="photos/81.jpg" width="300">
<img src="photos/80.jpg" width="300">
<img src="photos/79.jpg" width="300">
<img src="photos/78.jpg" width="300">
<img src="photos/76.jpg" width="300">
<img src="photos/75.jpg" width="300">
<img src="photos/73.jpg" width="300">
<img src="photos/72.jpg" width="300">

After removing the support material , go through each part and chamfer the edges where a motor goes as shown in the pictures below (This step is **critical** for ensuring your smart servos fit easily.)

<img src="photos/temppic.jpg" width="300"> <!--base joint-->
<img src="photos/temppic.jpg" width="300"> <!--middle joint-->


Additionally debur any holes for bolts or screws found on the part.

```
Protip: A file, sanding stick, or a knife works for the motor holes. 
Protip: Use a drill bit or a dedicated deburring tool for the holes. 
```


# 3 Add Threaded Inserts

Hammer in the M8 Tee nut for the into the camera stand.

<img src="photos/71.jpg" width="300">

Use the soldering iron to insert the other inserts. 

```
Protip: About 400C worked well for me.
```

<img src="photos/70.jpg" width="300">
<img src="photos/69.jpg" width="300">
<img src="photos/68.jpg" width="300">
<img src="photos/67.jpg" width="300">
<img src="photos/66.jpg" width="300">
<img src="photos/65.jpg" width="300">
<img src="photos/64.jpg" width="300">
<img src="photos/63.jpg" width="300">
<img src="photos/62.jpg" width="300">
<img src="photos/61.jpg" width="300">
<img src="photos/60.jpg" width="300">

# 4 Attaching Servo Horns

You should have 3 splined horns and 3 idler horns in your kit, if you don't contact an LA. Use only the splined horns for assembly, the idle horns are not needed.

```
Protip: the splined horns are the ones with teeth
```

%add pic for difference between spline and idle horn%

## 4.1 Link 1 Servo Horn

Attach a servo horn to the Link 1 Output Bracket using the small servo screws

<img src="photos/58.jpg" width="300">
<img src="photos/57.jpg" width="300">
<img src="photos/56.jpg" width="300">
<img src="photos/55.jpg" width="300">
<img src="photos/54.jpg" width="300">

## 4.2 Link 2 & 3 Output Brackets

Attach a servo horn to the Link 2 & 3 Output Brackets using the small servo screws

<img src="photos/53.jpg" width="300">
<img src="photos/52.jpg" width="300">

# 6 Motor 1

Plug the smart servo with an ID of 1 into the base part.

```
Protip: Put the cable in before putting motor into base (You'll thank me for this).
Protip: If your smart servo is having a hard time fitting make sure you chamfered the end properly.
```

<img src="photos/51.jpg" width="300">
<img src="photos/50.jpg" width="300">
<img src="photos/48.jpg" width="300">

Using the small servo screws to screw the motor in place.

<img src="photos/47.jpg" width="300">


# 7 Motor 3

Using 3 small servo screws attach motor 3 to the link 3 bracket

<img src="photos/45.jpg" width="300">
<img src="photos/44.jpg" width="300">

# 8 Gripper Assembely
Push the gripper servo into its hole on the main gripper body. 

**Please note we are aware of a tolerancing issue on this part. You can check if you are affected, by measuring the width of the slot (if you get a measurement of less than 11.9 you are affected), or by attempting to seat the servo. If you are affected by this contact the lab staff to get a replacement, if you are virtual follow the instructions on the protip below for how to widen the slot. **
```
Protip: Do not apply force on the spline of the servo you risk damaging it 
Protip: If your gripper servo is not able to be pushed in by hand, do not hammer it or force it in, doing so risks damaging the servo lightly sand the outside of the servo and file the inside of the slot as needed.
```

<img src="photos/41.jpg" width="300">
<img src="photos/40.jpg" width="300">

Then use a short bolt to attach the other part of the gripper.

<img src="photos/42.jpg" width="300">


# 9 Attach Electronics to Base

Remove the backing of the breadboard and use the double stick tape to attach the breadboard to the base

%ADD more pics%

# 10 Calibrate Motors

Attach all motors to the breadboard (just like you did in the electronics section) and hold down the big button you added on the breadboard for 10 seconds, this should calibrate all the motors while they are in place. 

```
Protip: In order to verify this worked plug the microcontroller into the computer and run the calibration with putty on.
```

**Skipping this step will make you have to reassemble the arm**

<img src="photos/34.jpg" width="300">


# 11 Thrust Bearing

Add the thrust bearing surface to both the base and the link 1 output bracket 

<img src="photos/33.jpg" width="300">

# 12 Passive Support Screws

Use the passive support screw to attach the printed links to the passive side of the motor (the side without the spline).

<img src="photos/29.jpg" width="300">
<img src="photos/28.jpg" width="300">


# 13 Caibration Pose & Servo Horns

The pose below is the calibration pose. It is where the servos assume they are when they boot up. Assemble your robot in this pose and do **not** rotate the servo at all. Doing so will mean you might need to take apart the robot later.

```
Protip: providing power to the motors mean they will hold their position which can be helpful for preventing unwanted rotation
```

<img src="armCalibration.png" width="300">


## 13.1 Servo 2 

Place Servo 2 into the Link 1 Output Backet. But do not screw it in

<img src="photos/23.jpg" width="300">

Then move the robot into the calibration pose.


##  13.2 Servo 1 Horn Screw

Remove Servo 2 and use the servo horn bolt to screw the horn to servo one with the Link 1 Output Bracket in the calibration pose.

<img src="photos/23.jpg" width="300">


## 13.3 Servo 2 

Place Servo 2 back into the Link 1 Output bracket. 

<img src="photos/27.jpg" width="300">

## 13.4 Servo 2 Horn Screw


Using the servo horn bolt, screw the horn to Servo 2 with the Link 2 Output bracket in the calibration pose. Then Attach the Link 2 Output Bracket to the link 3 bracket using the long bolts. 

<img src="photos/22.jpg" width="300">
<img src="photos/21.jpg" width="300">
<img src="photos/20.jpg" width="300">

# 13.5 Motor 3 Horn Screw

Using the servo horn bolt, screw the horn to Servo 3 with the Link 3 Output bracket in the calibration pose. Then use the short bolts to attach the Output bracket to the gripper body

<img src="photos/19.jpg" width="300">
<img src="photos/18.jpg" width="300">
<img src="photos/17.jpg" width="300"> 



# 14 Calibrate the Robot

 Calibrate the robot in its calibration pose now that all motors are in place. 
 
 ## 14.1 Robot stays in calibration pose
 
  If your robot does not move during calibration, CONGRATS! you have a fully assembled robot arm.
  
  Proceed to the next step to finish assembeling the robot. 
  
## 14.1 Robot moves from calibration pose

 If one of the motors comes off the calibrtation pose, pull that horn and reset it closer to the correct pose, if you have to err to one side go past the calibration pose away from where the motor moved itself. Then re preform Step 14.
 
 
# 16 Servo Case Screws

Use the small servo screws to screw in the case screw for each Servos 2/3 these are the 7 small holes found on the bottom and top of the servo. 

<img src="photos/3.jpg" width="300">
<img src="photos/4.jpg" width="300">


# 15 Gripper

Plug in the gripper servo and power up the system

The gripper will power up fully open, place the horn on as shown

<img src="photos/11.jpg" width="300">

Then add the rubber band arround the gripper.

Use solid core wire to hold the gripper open in the toggle pose

<img src="photos/10.jpg" width="300">
<img src="photos/8.jpg" width="300">
