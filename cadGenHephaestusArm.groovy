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
import eu.mihosoft.vrl.v3d.Transform

import com.neuronrobotics.bowlerstudio.vitamins.*;
import javafx.scene.transform.Affine;

double grid =25

CSG reverseDHValues(CSG incoming,DHLink dh ){
	//println "Reversing "+dh
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
	//			private HashMap<String, GearManager>  map= new HashMap<>()
	//			public  GearManager get(DHParameterKinematics b) {
	//				if(map.get(b.getXml())==null) {
	//					map.put(b.getXml(), new GearManager(b))
	//				}
	//				return map.get(b.getXml())
	//			}
	double motorGearPlateThickness = 10
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
		if(linkIndex!=0) {
			vitaminLocations.put(locationOfMotorMount, [
				conf.getShaftType(),
				conf.getShaftSize()
			])

		}


		if(linkIndex!=d.getNumberOfLinks()-1 ){
			LinkConfiguration confPrior = d.getLinkConfiguration(i+1);
			def vitaminType = confPrior.getElectroMechanicalType()
			def vitaminSize = confPrior.getElectroMechanicalSize()
			//println "Adding Motor "+vitaminType
			vitaminLocations.put(new TransformNR(0,0,0,new RotationNR()), [
				vitaminType,
				vitaminSize
			])

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
			Transform move = TransformFactory.nrToCSG(tr)
			def part = vitaminCad.transformed(move)
			part.setManipulator(manipulator)
			allCad.add(part)

			def massCentroidYValue = measurments.massCentroidY
			def massCentroidXValue = measurments.massCentroidX
			def massCentroidZValue = measurments.massCentroidZ
			def massKgValue = measurments.massKg
			//println vitaminType+" "+vitaminSize
			TransformNR COMCentroid = tr.times(
					new TransformNR(massCentroidXValue,massCentroidYValue,massCentroidZValue,new RotationNR())
					)
			totalMass+=massKgValue
			//do com calculation here for centerOfMassFromCentroid and totalMass
		}

		//Do additional CAD and add to the running CoM
		conf.setMassKg(totalMass)
		conf.setCenterOfMassFromCentroid(centerOfMassFromCentroid)
		CSG sparR = new Cube(d.getDH_R(linkIndex),5,5).toCSG()
				.toXMax()
		sparR.setManipulator(manipulator)
		allCad.add(sparR)
		//				CSG sparD = new Cube(gears.thickness,d.getDH_D(linkIndex),gears.thickness).toCSG()
		//						.toYMin()
		//						.toZMin()
		//				sparD.setManipulator(manipulator)
		//				allCad.add(sparD)
		d.addConnectionEventListener(new IDeviceConnectionEventListener (){

					/**
	 * Called on the event of a connection object disconnect.
	 *
	 * @param source the source
	 */
					public void onDisconnect(BowlerAbstractDevice source) {
						gears.clear()
						allCad.clear()
					}
					public void onConnect(BowlerAbstractDevice source) {}
				})
		return allCad;
	}
	@Override
	public ArrayList<CSG> generateBody(MobileBase b ) {

		def vitaminLocations = new HashMap<TransformNR,ArrayList<String>>()
		ArrayList<CSG> allCad=new ArrayList<>();
		double baseGrid = grid;
		double baseBoltThickness=15;
		double baseCoreheight = 1;
		String boltsize = "M5x25"
		def thrustBearingSize = "Thrust_1andAHalfinch"

		for(DHParameterKinematics d:b.getAllDHChains()) {
			// Hardware to engineering units configuration
			LinkConfiguration conf = d.getLinkConfiguration(0);

			b.addConnectionEventListener(new IDeviceConnectionEventListener (){

						/**
		 * Called on the event of a connection object disconnect.
		 *
		 * @param source the source
		 */
						public void onDisconnect(BowlerAbstractDevice source) {
							//gears.clear()
							allCad.clear()
						}
						public void onConnect(BowlerAbstractDevice source) {}
					})

			CSG motorModel=   Vitamins.get(conf.getElectroMechanicalType(),conf.getElectroMechanicalSize())
			double zOffset = motorModel.getMaxZ()
			TransformNR locationOfMotorMount = d.getRobotToFiducialTransform().copy()
			TransformNR locationOfBearing = locationOfMotorMount.copy()
			//move for gearing
			
			// move the motor down to allign with the shaft
			if(locationOfBearing.getZ()>baseCoreheight)
				baseCoreheight=locationOfBearing.getZ()
			locationOfMotorMount.translateZ(-zOffset)
			TransformNR pinionRoot = locationOfMotorMount.copy()
			def extractionLocationOfMotor =locationOfMotorMount.copy().translateZ(-20)

			vitaminLocations.put(locationOfBearing.copy().translateZ(-1), [
				"ballBearing",
				thrustBearingSize
			])
			vitaminLocations.put(locationOfMotorMount, [
				conf.getElectroMechanicalType(),
				conf.getElectroMechanicalSize()
			])
			vitaminLocations.put(extractionLocationOfMotor, [
				conf.getElectroMechanicalType(),
				conf.getElectroMechanicalSize()
			])
			vitaminLocations.put(pinionRoot, [
				conf.getShaftType(),
				conf.getShaftSize()
			])
		}
		def insert=["heatedThreadedInsert", "M5"]
		def insertMeasurments= Vitamins.getConfiguration(insert[0],
				insert[1])
		def mountLoacions = [
			new TransformNR(baseGrid,-baseGrid,0,new RotationNR(180,0,0)),
			new TransformNR(-baseGrid,baseGrid,0,new RotationNR(180,0,0)),
			new TransformNR(-baseGrid,-baseGrid,0,new RotationNR(180,0,0))
		]

		mountLoacions.forEach{
			vitaminLocations.put(it,
					["capScrew", boltsize])
			vitaminLocations.put(it.copy().translateZ(insertMeasurments.installLength),
					insert)

		}

		double totalMass = 0;
		TransformNR centerOfMassFromCentroid=new TransformNR();

		for(TransformNR tr: vitaminLocations.keySet()) {
			def vitaminType = vitaminLocations.get(tr)[0]
			def vitaminSize = vitaminLocations.get(tr)[1]

			HashMap<String, Object>  measurments = Vitamins.getConfiguration( vitaminType,vitaminSize)

			CSG vitaminCad=   Vitamins.get(vitaminType,vitaminSize)
			Transform move = TransformFactory.nrToCSG(tr)
			CSG part = vitaminCad.transformed(move)
			part.setManipulator(b.getRootListener())
			allCad.add(part)

			def massCentroidYValue = measurments.massCentroidY
			def massCentroidXValue = measurments.massCentroidX
			def massCentroidZValue = measurments.massCentroidZ
			def massKgValue = measurments.massKg
			//println "Base Vitamin "+vitaminType+" "+vitaminSize
			try {
				TransformNR COMCentroid = tr.times(
						new TransformNR(massCentroidXValue,massCentroidYValue,massCentroidZValue,new RotationNR())
						)
				totalMass+=massKgValue
			}catch(Exception ex) {
				BowlerStudio.printStackTrace(ex)
			}

			//do com calculation here for centerOfMassFromCentroid and totalMass
		}
		//Do additional CAD and add to the running CoM
		def thrustMeasurments= Vitamins.getConfiguration("ballBearing",
				thrustBearingSize)
		def baseCorRad = thrustMeasurments.outerDiameter/2+5
		CSG baseCore = new Cylinder(baseCorRad,baseCorRad,baseCoreheight,36).toCSG()
		CSG baseCoreshort = new Cylinder(baseCorRad,baseCorRad,baseCoreheight*3.0/4.0,36).toCSG()
		CSG mountLug = new Cylinder(15,15,baseBoltThickness,36).toCSG().toZMax()
		CSG mountCap = Parabola.coneByHeight(15, 20)
				.rotx(-90)
				.toZMax()
				.movez(-baseBoltThickness)
		def coreParts=[baseCore]
		mountLoacions.forEach{
			def place =com.neuronrobotics.bowlerstudio.physics.TransformFactory.nrToCSG(it)
			coreParts.add(
					CSG.hullAll(mountLug
					.transformed(place)
					,baseCoreshort)
					)
			coreParts.add(mountCap
					.transformed(place)
					)
		}

		// assemble the base

		def Base = CSG.unionAll(coreParts)
				//.difference(vitamin_roundMotor_WPI_gb37y3530_50en)
				.difference(allCad)
		// add it to the return list
		Base.setManipulator(b.getRootListener())
		allCad.add(Base)

		//allCad.add(vitamin_roundMotor_WPI_gb37y3530_50en)
		b.setMassKg(totalMass)
		b.setCenterOfMassFromCentroid(centerOfMassFromCentroid)

		return allCad;
	}
};

