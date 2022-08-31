# Troubleshooting

these instructions are for the various problems people have run into

# I reboot and my KB and mouse are frozen

* Boot into grub onetime menu by holding SHIFT while booting
* select recovery mode
* select enable networking
* select login as root
* run
```
sudo apt install lightdm
sudo dpkg-reconfigure  lightdm 
```
And select lightdm as the defualt. 

Reboot and login normally. 

# I power cycle the robot and the LED button to calibrate is not turning on 

* 1st resolution reinstall the firmware in itsybitsy
* If that does not work contact a lab manager/ lab assistant

# I power cycle the robot and calibrate, but motor is not running

* One or all the motors are mostly damaged and you need to replace it. 
* Pleasse contact a lab manager/lab assistant
