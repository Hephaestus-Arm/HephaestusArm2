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
import com.neuronrobotics.sdk.addons.kinematics.imu.*;
import edu.wpi.SimplePacketComs.BytePacketType;
import edu.wpi.SimplePacketComs.FloatPacketType;
import edu.wpi.SimplePacketComs.*;
import edu.wpi.SimplePacketComs.phy.UDPSimplePacketComs;
import edu.wpi.SimplePacketComs.device.gameController.*;
import edu.wpi.SimplePacketComs.device.*

public class HephaestusArm extends HIDSimplePacketComs{
	PacketType pollingPacket = new FloatPacketType(1,64);
	PacketType pidPacket = new FloatPacketType(2,64);
	PacketType PDVelPacket = new FloatPacketType(48,64);
	PacketType SetVelocity = new FloatPacketType(42,64);
	PacketType gripperPacket = new FloatPacketType(3,64);
	String name=null
	String getName(){
		return name;
	}
	void setName(String n){
		name =n
	}
	public HephaestusArm(int vidIn, int pidIn, String n) {
		super(vidIn, pidIn);
		name =n
		pidPacket.oneShotMode();
		pidPacket.sendOk();
		PDVelPacket.oneShotMode();		
		PDVelPacket.sendOk();
		SetVelocity.oneShotMode();
		SetVelocity.sendOk();
		gripperPacket.oneShotMode();
		gripperPacket.sendOk();
		for (PacketType pt : Arrays.asList(pollingPacket, pidPacket, PDVelPacket, SetVelocity,gripperPacket)) {
			addPollingPacket(pt);
		}
	}
	public void addPollingPacketEvent(Runnable event) {
		addEvent(pollingPacket.idOfCommand, event);
	}
	public void setValues(int index,float position, float velocity, float force){
		pollingPacket.getDownstream()[(index*3)+0] = position;
		pollingPacket.getDownstream()[(index*3)+1] = velocity;
		pollingPacket.getDownstream()[(index*3)+2] = force;
		//println "Setting Downstream "+downstream
	}
	public void setGripperPosition(float position){
		gripperPacket.getDownstream()[0] = position;
		gripperPacket.oneShotMode();
	}
	public void setPIDGains(int index,float kp, float ki, float kd){
		
		pidPacket.getDownstream()[(index*3)+0] = kp;
		pidPacket.getDownstream()[(index*3)+1] = ki;
		pidPacket.getDownstream()[(index*3)+2] = kd;
		//println "Setting Downstream "+downstream
	}
	public void pushPIDGains(){
		pidPacket.oneShotMode();
	}
	public void setPDVelGains(int index,float kp, float kd){
		
		PDVelPacket.getDownstream()[(index*2)+0] = kp;
		PDVelPacket.getDownstream()[(index*2)+1] = kd;
		//println "Setting Downstream "+downstream
	}
	public void pushPDVelGains(){
		PDVelPacket.oneShotMode();
	}
	public void setVelocity(int index,float TPS){
		SetVelocity.getDownstream()[index] = TPS;
		//println "Setting Downstream "+downstream
	}
	public void pushVelocity(){
		SetVelocity.oneShotMode();
	}
	public List<Double> getValues(int index){
		List<Double> back= new ArrayList<>();
	
		back.add(pollingPacket.getUpstream()[(index*3)+0].doubleValue()) ;
		back.add( pollingPacket.getUpstream()[(index*3)+1].doubleValue());
		back.add(pollingPacket.getUpstream()[(index*3)+2].doubleValue());
		
		return back;
	}
	public double getPosition(int index) {
		return pollingPacket.getUpstream()[(index*3)+0].doubleValue();
	}
	
	public Number[] getRawValues(){
		return pollingPacket.getUpstream();
	}
	public void setRawValues(Number[] set){
		for(int i=0;i<set.length&&i<pollingPacket.getDownstream().length;i++) {
			pollingPacket.getDownstream()[i]=set[i];
		}
	}
	
}
public class HIDRotoryLink extends AbstractRotoryLink{
	def device;
	int index =0;
	int lastPushedVal = 0;
	/**
	 * Instantiates a new HID rotory link.
	 *
	 * @param c the c
	 * @param conf the conf
	 */
	public HIDRotoryLink(def c,LinkConfiguration conf) {
		super(conf);
		index = conf.getHardwareIndex()
		device=c
		if(device ==null)
			throw new RuntimeException("Device can not be null")
		c.addEvent(1,{
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
		device.setValues(index,(float)getTargetValue(),(float)0,(float)0)
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
		return device.getPosition(index);
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
			def d = new HephaestusArm(vid,pid,searchName)
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


