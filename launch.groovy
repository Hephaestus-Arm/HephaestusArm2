

import Jama.Matrix;

ScriptingEngine.gitScriptRun(
	"https://github.com/madhephaestus/HephaestusArm2.git", 
	"PID-G_DeviceLoader.groovy", 
	["hidbowler"]
);

def base =DeviceManager.getSpecificDevice( "HephaestusArm",{
	//If the device does not exist, prompt for the connection
	
	MobileBase m = MobileBaseLoader.fromGit(
		"https://github.com/madhephaestus/HephaestusArm2.git",
		"hephaestus.xml"
		)
	return m
})

public class PhysicicsDevice extends NonBowlerDevice{

	def hidEventEngine;
	def physicsSource ;
	int count = 0;
	Runnable run={
	
	
			count ++
			if(count >10){
				count =0
				// Setup of variables done, next perfoem one compute cycle
				
				//get the current FK pose to update the data used by the jacobian computation
				TransformNR pose = physicsSource.getCurrentTaskSpaceTransform()
				// Convert the tip transform to Matrix form for math
				Matrix matrixForm= pose.getMatrixTransform()
				// get the position of all the joints in engineering units
				double[] jointSpaceVector = physicsSource.getCurrentJointSpaceVector()
				// compute the Jacobian using Jama matrix library
				Matrix jacobian = physicsSource.getJacobian();
				Matrix[] massMatrix =  new Matrix[jointSpaceVector.length]
				Matrix[] incrementalJacobian =  new Matrix[jointSpaceVector.length]
				double [] masses = new double [jointSpaceVector.length]
				//TODO LoadMasses and mass Matrix here
				
				for (int i=0;i<jointSpaceVector.length;i++){
					incrementalJacobian[i] = physicsSource.getJacobian(jointSpaceVector,i);
					
					//println "Increment "+i+" "+  TransformNR.getMatrixString(incrementalJacobian[i])
				}
				//println "Total "+  TransformNR.getMatrixString(jacobian)
								
			}
		}
	public PhysicicsDevice(def c,def  d){
		hidEventEngine=c;
		physicsSource=d;
		hidEventEngine.arm.addEvent(37,run)
		
	}
	@Override
	public  void disconnectDeviceImp(){		
		println "Physics Termination signel shutdown"
		hidEventEngine.arm.removeEvent(37,run)
	}
	
	@Override
	public  boolean connectDeviceImp(){
		println "Physics Startup signel "
		return true
	}
	public  ArrayList<String>  getNamespacesImp(){
		// no namespaces on dummy
		return [];
	}
	
}


return

def physics =DeviceManager.getSpecificDevice( "HephaestusPhysics",{
	PhysicicsDevice pd = new PhysicicsDevice(dev,base.getAllDHChains().get(0))
	
	return pd
})
