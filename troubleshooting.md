# Troubleshooting

these instructions are for the various problems people have run into

# I reboot and my KB and mouse are frozen

* Boot into grub onetime menu by holding SHIFT while booting
* select recovery mode
* select login as root
* run
```
sudo apt install lightdm
sudo dpkg-reconfigure  lightdm 
```
And select lightdm as the defualt. 

Reboot and login normally. 
