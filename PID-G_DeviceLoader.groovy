@GrabResolver(name='sonatype', root='https://oss.sonatype.org/content/repositories/releases/')
@Grab(group='com.neuronrobotics', module='SimplePacketComsJava', version='0.10.1')
@Grab(group='com.neuronrobotics', module='SimplePacketComsJava-HID', version='0.10.0')
@Grab(group='org.hid4java', module='hid4java', version='0.5.0')

import Jama.Matrix;
import edu.wpi.SimplePacketComs.*;
import edu.wpi.SimplePacketComs.phy.*;
import com.neuronrobotics.sdk.addons.kinematics.imu.*;
import edu.wpi.SimplePacketComs.BytePacketType;
import edu.wpi.SimplePacketComs.FloatPacketType;
import edu.wpi.SimplePacketComs.*;
import edu.wpi.SimplePacketComs.phy.UDPSimplePacketComs;
import edu.wpi.SimplePacketComs.device.gameController.*;
import edu.wpi.SimplePacketComs.device.*

if(args==null)
	args=["hidbowler"]

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


def dev = DeviceManager.getSpecificDevice( args[0],{
	//If the device does not exist, prompt for the connection
	
	def d = new HephaestusArm(0x3742,0x7,args[0])
	d.connect(); // Connect to it.
	if(d.isVirtual()){
		println "\n\n\nDevice is in virtual mode!\n\n\n"
	}
	LinkFactory.addLinkProvider("hidsimple",{LinkConfiguration conf->
				println "Loading link "
				return new HIDRotoryLink(d,conf)
		}
	)
	println "Connecting new device: "+d.getName()
	return d
})