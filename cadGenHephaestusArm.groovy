import com.neuronrobotics.bowlerstudio.creature.ICadGenerator;
import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine
import com.neuronrobotics.bowlerstudio.creature.CreatureLab;
import org.apache.commons.io.IOUtils;
import com.neuronrobotics.bowlerstudio.vitamins.*;
import com.neuronrobotics.sdk.addons.kinematics.AbstractLink
import com.neuronrobotics.sdk.addons.kinematics.DHLink
import com.neuronrobotics.sdk.addons.kinematics.DHParameterKinematics
import com.neuronrobotics.sdk.addons.kinematics.LinkConfiguration
import com.neuronrobotics.sdk.addons.kinematics.MobileBase
import com.neuronrobotics.sdk.addons.kinematics.math.RotationNR
import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR

import java.nio.file.Paths;

import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cube
import eu.mihosoft.vrl.v3d.FileUtil;
import eu.mihosoft.vrl.v3d.Transform

import com.neuronrobotics.bowlerstudio.vitamins.*;
import javafx.scene.transform.Affine;
println "Loading STL file"
// Load an STL file from a git repo
// Loading a local file also works here
CSG reverseDHValues(CSG incoming,DHLink dh ){
	println "Reversing "+dh
	TransformNR step = new TransformNR(dh.DhStep(0))
	Transform move = com.neuronrobotics.bowlerstudio.physics.TransformFactory.nrToCSG(step)
	return incoming.transformed(move)
}

CSG moveDHValues(CSG incoming,DHLink dh ){
	TransformNR step = new TransformNR(dh.DhStep(0)).inverse()
	Transform move = com.neuronrobotics.bowlerstudio.physics.TransformFactory.nrToCSG(step)
	return incoming.transformed(move)
	
}

return new ICadGenerator(){
	@Override 
	public ArrayList<CSG> generateCad(DHParameterKinematics d, int linkIndex) {
		
		def vitaminLocations = new HashMap<TransformNR,ArrayList<String>>()
				
		ArrayList<DHLink> dhLinks = d.getChain().getLinks()
		ArrayList<CSG> allCad=new ArrayList<>()
		int i=linkIndex;
		DHLink dh = dhLinks.get(linkIndex)
		// Hardware to engineering units configuration
		LinkConfiguration conf = d.getLinkConfiguration(i);
		// Engineering units to kinematics link (limits and hardware type abstraction)
		AbstractLink abstractLink = d.getAbstractLink(i);
		// Transform used by the UI to render the location of the object
		Affine manipulator = dh.getListener();
		// loading the vitamins referenced in the configuration
		//CSG servo=   Vitamins.get(conf.getElectroMechanicalType(),conf.getElectroMechanicalSize())
		TransformNR locationOfMotorMount = new TransformNR(dh.DhStep(0)).inverse()
		
		vitaminLocations.put(locationOfMotorMount, [conf.getShaftType(),conf.getShaftSize()])

		if(linkIndex!=d.getNumberOfLinks()-1 ){
			LinkConfiguration confPrior = d.getLinkConfiguration(i+1);
			def vitaminType = confPrior.getElectroMechanicalType()
			def vitaminSize = confPrior.getElectroMechanicalSize()
			println "Adding Motor "+vitaminType
			vitaminLocations.put(new TransformNR(), [
				vitaminType,
				vitaminSize
			])
		}else {
			println "\r\nNOT adding "+linkIndex
		}
		
		//CSG tmpSrv = moveDHValues(servo,dh)

		//Compute the location of the base of this limb to place objects at the root of the limb
		//TransformNR step = d.getRobotToFiducialTransform()
		//Transform locationOfBaseOfLimb = com.neuronrobotics.bowlerstudio.physics.TransformFactory.nrToCSG(step)
		
		double totalMass = 0;
		TransformNR centerOfMassFromCentroid=new TransformNR();
		
		for(TransformNR tr: vitaminLocations.keySet()) {
			def vitaminType = vitaminLocations.get(tr)[0]
			def vitaminSize = vitaminLocations.get(tr)[1]
			
			HashMap<String, Object>  measurments = Vitamins.getConfiguration( vitaminType,vitaminSize)
			
			CSG vitaminCad=   Vitamins.get(vitaminType,vitaminSize)
			Transform move = com.neuronrobotics.bowlerstudio.physics.TransformFactory.nrToCSG(tr)
			def part = vitaminCad.transformed(move)
			part.setManipulator(manipulator)
			allCad.add(part)
			
			def massCentroidYValue = measurments.massCentroidY
			def massCentroidXValue = measurments.massCentroidX
			def massCentroidZValue = measurments.massCentroidZ
			def massKgValue = measurments.massKg
			println vitaminType+" "+vitaminSize
			TransformNR COMCentroid = tr.times(
				new TransformNR(massCentroidXValue,massCentroidYValue,massCentroidZValue,new RotationNR())
				)
			totalMass+=massKgValue			
			//do com calculation here for centerOfMassFromCentroid and totalMass
		}
		
		//Do additional CAD and add to the running CoM
		conf.setMassKg(totalMass)
		conf.setCenterOfMassFromCentroid(centerOfMassFromCentroid)
		
		//tmpSrv.setManipulator(manipulator)
		//allCad.add(tmpSrv)
		println "Generating link: "+linkIndex
		
		return allCad;
	}
	@Override 
	public ArrayList<CSG> generateBody(MobileBase b ) {
		ArrayList<CSG> allCad=new ArrayList<>();
		double size =40;
		def body= new Cube(10).toCSG()
		body.setManipulator(b.getRootListener());
		

		return [body];
	}
};