import com.neuronrobotics.sdk.common.BowlerAbstractDevice

import com.neuronrobotics.bowlerstudio.creature.ICadGenerator;
import com.neuronrobotics.bowlerstudio.physics.TransformFactory
import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine
import com.neuronrobotics.bowlerstudio.BowlerStudio
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
import com.neuronrobotics.sdk.common.IDeviceAddedListener
import com.neuronrobotics.sdk.common.IDeviceConnectionEventListener

import java.nio.file.Paths;

import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cube
import eu.mihosoft.vrl.v3d.Cylinder
import eu.mihosoft.vrl.v3d.Parabola
import eu.mihosoft.vrl.v3d.RoundedCube
import eu.mihosoft.vrl.v3d.RoundedCylinder
import eu.mihosoft.vrl.v3d.Sphere
import eu.mihosoft.vrl.v3d.Transform

import com.neuronrobotics.bowlerstudio.vitamins.*;
import javafx.scene.transform.Affine;
import  eu.mihosoft.vrl.v3d.ext.quickhull3d.*
import eu.mihosoft.vrl.v3d.Vector3d

return new ICadGenerator(){
	CSG moveDHValues(CSG incoming,DHLink dh ){
		TransformNR step = new TransformNR(dh.DhStep(0)).inverse()
		Transform move = com.neuronrobotics.bowlerstudio.physics.TransformFactory.nrToCSG(step)
		return incoming.transformed(move)
	}
	@Override
	public ArrayList<CSG> generateCad(DHParameterKinematics d, int linkIndex) {
		Affine manipulator = d.getListener(linkIndex);
		ArrayList<CSG> allCad=new ArrayList<>()
		if(d.getScriptingName().contentEquals("Hand")) {
			
		
			File dir= ScriptingEngine.getRepositoryCloneDirectory(d.getGitCadEngine()[0])
			File gripperFile = new File(dir.getAbsolutePath()+"/gripper.stl")

			CSG gripperMovingCup  = moveDHValues(Vitamins.get(gripperFile).rotz(90),d.getDhLink(linkIndex))
			
			gripperMovingCup.setColor(javafx.scene.paint.Color.LIGHTPINK)
			gripperMovingCup.setName("Gripper")
			gripperMovingCup.setManufacturing ({ mfg ->
				return mfg.rotx(180).toZMin()
			})
			
			
			gripperMovingCup.setManipulator(manipulator)
			allCad.add(gripperMovingCup)
		}
		if(d.getScriptingName().contentEquals("HandActuator")) {
			LinkConfiguration conf = d.getLinkConfiguration(linkIndex);
			CSG horn = moveDHValues(Vitamins.get(
			conf.getShaftType(),
			conf.getShaftSize()
			).rotz(90),d.getDhLink(linkIndex))
			horn.setManufacturing ({ mfg ->
				return null
			})
			horn.setManipulator(manipulator)
			allCad.add(horn)
		}
		
		return allCad;
	}
	
	@Override
	public ArrayList<CSG> generateBody(MobileBase b ) {
		ArrayList<CSG> allCad=new ArrayList<>();
		return allCad;
	}
};

