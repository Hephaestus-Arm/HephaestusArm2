@GrabResolver(name='nr', root='https://oss.sonatype.org/service/local/repositories/releases/content/')
@GrabResolver(name='mvnRepository', root='https://repo1.maven.org/maven2/')
@Grab(group='net.java.dev.jna', module='jna', version='4.2.2')
@Grab(group='com.neuronrobotics', module='SimplePacketComsJava', version='0.12.0')
@Grab(group='com.neuronrobotics', module='SimplePacketComsJava-HID', version='0.13.0')
@Grab(group='org.hid4java', module='hid4java', version='0.5.0')

import Jama.Matrix;
import edu.wpi.SimplePacketComs.*;
import edu.wpi.SimplePacketComs.phy.*;

import com.neuronrobotics.sdk.addons.kinematics.AbstractLink
import com.neuronrobotics.sdk.addons.kinematics.AbstractRotoryLink
import com.neuronrobotics.sdk.addons.kinematics.INewLinkProvider
import com.neuronrobotics.sdk.addons.kinematics.LinkConfiguration
import com.neuronrobotics.sdk.addons.kinematics.LinkFactory
import com.neuronrobotics.sdk.addons.kinematics.imu.*;
import edu.wpi.SimplePacketComs.BytePacketType;
import edu.wpi.SimplePacketComs.FloatPacketType;
import edu.wpi.SimplePacketComs.*;
import edu.wpi.SimplePacketComs.phy.UDPSimplePacketComs;
import edu.wpi.SimplePacketComs.device.gameController.*;
import edu.wpi.SimplePacketComs.device.*

import java.util.Arrays;

import edu.wpi.SimplePacketComs.FloatPacketType;
import edu.wpi.SimplePacketComs.PacketType;
public class NumberOfPID {
	private int myNum = -1;

	public int getMyNum() {
		return myNum;
	}

	public void setMyNum(int myNum) {
		this.myNum = myNum;
	}
	

}

public class RBE3001Robot  extends HIDSimplePacketComs{
	FloatPacketType setSetpoint = new FloatPacketType(1848, 64);
	FloatPacketType pidStatus = new FloatPacketType(1910, 64);
	FloatPacketType getConfig = new FloatPacketType(1857, 64);
	FloatPacketType setConfig = new FloatPacketType(1900, 64);

	PacketType SetPIDVelocity = new FloatPacketType(1811, 64);
	PacketType SetPDVelocityConstants = new FloatPacketType(1810, 64);
	PacketType GetPIDVelocity = new FloatPacketType(1822, 64);
	PacketType GetPDVelocityConstants = new FloatPacketType(1825, 64);

	double[] numPID = new double[1];
	double[] pidConfigData = new double[15];
	double[] pidVelConfigData = new double[15];

	double[] piddata = new double[15];
	double[] veldata = new double[15];
	NumberOfPID myNum = new NumberOfPID();
	public RBE3001Robot(int vidIn, int pidIn) throws Exception {
		super(vidIn,  pidIn);
		setupPidCommands(3);
		connect();
//		if(isVirtual())
//			throw new RuntimeException("Device is virtual!");
	}
	 void setupPidCommands(int numPID) {
		//new Exception().printStackTrace();
		myNum.setMyNum(numPID);
		SetPIDVelocity.waitToSendMode();
		SetPDVelocityConstants.waitToSendMode();
		GetPIDVelocity.pollingMode();
		GetPDVelocityConstants.oneShotMode();

		getConfig.oneShotMode();
		setConfig.waitToSendMode();
		setSetpoint.waitToSendMode();

		for (PacketType pt : Arrays.asList(pidStatus, getConfig, setConfig, setSetpoint, SetPIDVelocity,
				SetPDVelocityConstants, GetPIDVelocity, GetPDVelocityConstants)) {
			addPollingPacket(pt);
		}

		addEvent(GetPDVelocityConstants.idOfCommand,  {
			try {
				readFloats(GetPDVelocityConstants.idOfCommand, pidVelConfigData);
				for (int i = 0; i < 3; i++) {
					System.out.print("\n vp " + getVKp(i));
					System.out.print(" vd " + getVKd(i));
					System.out.println("");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		addEvent(getConfig.idOfCommand,  {
			try {
				readFloats(getConfig.idOfCommand, pidConfigData);
				for (int i = 0; i < 3; i++) {
					System.out.print("\n p " + getKp(i));
					System.out.print(" i " + getKi(i));
					System.out.print(" d " + getKd(i));
					System.out.println("");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		addEvent(pidStatus.idOfCommand,  {
			try {
				if (piddata == null) {
					// piddata = new double[15];
					readFloats(pidStatus.idOfCommand, piddata);
				}
				readFloats(pidStatus.idOfCommand, piddata);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		addEvent(GetPIDVelocity.idOfCommand,  {
			try {
				if (veldata == null) {
					// veldata = new double[15];
					readFloats(GetPIDVelocity.idOfCommand, veldata);
				}
				readFloats(GetPIDVelocity.idOfCommand, veldata);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	 public double getNumPid() {
		return myNum.getMyNum();
	}

	 public double getPidSetpoint(int index) {

		return pidStatus.getUpstream()[1 + index * 2 + 0].doubleValue();
	}

	 public double getPidPosition(int index) {
		if(isVirtual())
			 return setSetpoint.getDownstream()[1 + index * 2 + 0].doubleValue();
		return pidStatus.getUpstream()[1 + index * 2 + 1].doubleValue();
	}

	/**
	 * Velocity domain values
	 *
	 * @param index
	 * @return
	 */
	 public double getHardwareOutput(int index) {
		return GetPIDVelocity.getUpstream()[1 + index * 3 + 2].doubleValue();
	}

	 public double getVelocity(int index) {
		return GetPIDVelocity.getUpstream()[1 + index * 3 + 1].doubleValue();
	}

	 public double getVelSetpoint(int index) {

		return GetPIDVelocity.getUpstream()[1 + index * 3 + 0].doubleValue();
	}

	 public void updatConfig() {
		getConfig.oneShotMode();
		GetPDVelocityConstants.oneShotMode();
	}

	 public void setPidGains(int index, double kp, double ki, double kd) {
		pidConfigData[3 * index + 0] = kp;
		pidConfigData[3 * index + 1] = ki;
		pidConfigData[3 * index + 2] = kd;
		writeFloats(setConfig.idOfCommand, pidConfigData);
		setConfig.oneShotMode();

	}

	 public double getKp(int index) {
		readFloats(getConfig.idOfCommand, pidConfigData);
		return pidConfigData[(3 * index) + 0];
	}

	 public double getKi(int index) {
		readFloats(getConfig.idOfCommand, pidConfigData);
		return pidConfigData[(3 * index) + 1];
	}

	 public double getKd(int index) {
		readFloats(getConfig.idOfCommand, pidConfigData);
		return pidConfigData[(3 * index) + 2];
	}

	 public double getVKp(int index) {
		readFloats(GetPDVelocityConstants.idOfCommand, pidVelConfigData);
		return pidVelConfigData[(3 * index) + 0];
	}

	 public double getVKd(int index) {
		readFloats(GetPDVelocityConstants.idOfCommand, pidVelConfigData);
		return pidVelConfigData[(3 * index) + 2];
	}
	 public double getVKi(int index) {
		readFloats(GetPDVelocityConstants.idOfCommand, pidVelConfigData);
		return pidVelConfigData[(3 * index) + 1];
	}
	 public void setVelocityGains(int index, double kp, double ki,double kd) {
		pidVelConfigData[3 * index + 0] = kp;
		pidVelConfigData[3 * index + 1] = ki;
		pidVelConfigData[3 * index + 2] = kd;
		writeFloats(SetPDVelocityConstants.idOfCommand, pidVelConfigData);
		SetPDVelocityConstants.oneShotMode();
	}

	 public void setPidSetpoints(int msTransition, int mode, double[] data) {
		def down = new double[2 + getMyNumPid()];
		down[0] = msTransition;
		down[1] = mode;
		for (int i = 0; i < getMyNumPid(); i++) {
			down[2 + i] = data[i];
		}
		writeFloats(setSetpoint.idOfCommand, down);
		setSetpoint.oneShotMode();

	}

	 public void setPidSetpoint(int msTransition, int mode, int index, double data) {
		double[] cur = new double[getMyNumPid()];
		for (int i = 0; i < getMyNumPid(); i++) {
			if (i == index)
				cur[index] = data;
			else
				cur[i] = getPidSetpoint(i);
		}
		cur[index] = data;
		setPidSetpoints(msTransition, mode, cur);

	}

	 public void setVelocity(int index, double data) {
		double[] cur = new double[getMyNumPid()];
		for (int i = 0; i < getMyNumPid(); i++) {
			if (i == index)
				cur[index] = data;
			else
				cur[i] = getVelSetpoint(i);
		}
		cur[index] = data;
		setVelocity(cur);

	}

	 public void setVelocity(double[] data) {
		writeFloats(SetPIDVelocity.idOfCommand, data);
		SetPIDVelocity.oneShotMode();

	}

	 public int getMyNumPid() {
		return myNum.getMyNum();
	}

	 public void setMyNumPid(int myNumPid) {
		if (myNumPid > 0)
			myNum.setMyNum(myNumPid);
		throw new RuntimeException("Can not have 0 PID");
	}

	 public void stop(int currentIndex) {
		setPidSetpoint(0, 0, currentIndex, getPidPosition(currentIndex));
	}
	@Override
	public String toString() {
		return getName();
	}
}

public class HIDRotoryLink extends AbstractRotoryLink{
	RBE3001Robot device;
	int index =0;
	int lastPushedVal = 0;
	/**
	 * Instantiates a new HID rotory link.
	 *
	 * @param c the c
	 * @param conf the conf
	 */
	public HIDRotoryLink(RBE3001Robot c,LinkConfiguration conf) {
		super(conf);
		conf.setDeviceTheoreticalMax(180);
		conf.setDeviceTheoreticalMin(-180);
		
		index = conf.getHardwareIndex()
		device=c
		if(device ==null)
			throw new RuntimeException("Device can not be null")
		c.addEvent(1910,{
			int val= getCurrentPosition();
			if(lastPushedVal!=val){
				//println "Fire Link Listner "+index+" value "+getCurrentPosition()
				fireLinkListener(val);
			}
			lastPushedVal=val
		})
		
	}

	/* (non-Javadoc)
	 * @see com.neuronrobotics.sdk.addons.kinematics.AbstractLink#cacheTargetValueDevice()
	 */
	@Override
	public void cacheTargetValueDevice() {
		device.setPidSetpoint(0,0,index,(float)getTargetValue())
	}

	/* (non-Javadoc)
	 * @see com.neuronrobotics.sdk.addons.kinematics.AbstractLink#flush(double)
	 */
	@Override
	public void flushDevice(double time) {
		// auto flushing
	}
	
	/* (non-Javadoc)
	 * @see com.neuronrobotics.sdk.addons.kinematics.AbstractLink#flushAll(double)
	 */
	@Override
	public void flushAllDevice(double time) {
		// auto flushing
	}

	/* (non-Javadoc)
	 * @see com.neuronrobotics.sdk.addons.kinematics.AbstractLink#getCurrentPosition()
	 */
	@Override
	public double getCurrentPosition() {
		return device.getPidPosition(index);
	}

}


INewLinkProvider provider= new INewLinkProvider() {
	public AbstractLink generate(LinkConfiguration conf) {
		String searchName = conf.getDeviceScriptingName();
		int vid=0x3742
		int pid=0x0007
		if(searchName.size()>8){
			String deviceID = searchName.substring(searchName.size()-8,searchName.size())
			String VIDStr = deviceID.substring(0,4)
			String PIDStr = deviceID.substring(4,8)
			try{
				vid = Integer.parseInt(VIDStr,16); 
				pid = Integer.parseInt(PIDStr,16); 
				//println "Searching for Device at "+VIDStr+" "+PIDStr
			}catch(Throwable t){
				BowlerStudio.printStackTrace(t)
			}
			
		}
		def dev = DeviceManager.getSpecificDevice( searchName,{
			RBE3001Robot d = new RBE3001Robot(vid,pid)
			d.setName(searchName);
			d.connect(); // Connect to it.
			if(d.isVirtual()){
				println "\n\n\nDevice is in virtual mode!\n\n\n"
			}
			return d
		})
		
		return new HIDRotoryLink(dev,conf);
	}
	
}

if(args==null)
	args=["pidg-link"]
LinkFactory.addLinkProvider(args[0], provider)

return provider


