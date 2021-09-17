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
import eu.mihosoft.vrl.v3d.FileUtil
import eu.mihosoft.vrl.v3d.Parabola
import eu.mihosoft.vrl.v3d.RoundedCube
import eu.mihosoft.vrl.v3d.RoundedCylinder
import eu.mihosoft.vrl.v3d.Sphere
import eu.mihosoft.vrl.v3d.Transform

import javafx.scene.transform.Affine;
import  eu.mihosoft.vrl.v3d.ext.quickhull3d.*
import eu.mihosoft.vrl.v3d.parametrics.LengthParameter
import eu.mihosoft.vrl.v3d.Vector3d


double grid =25
double cornerOffset=grid*1.75
double boardx=8.5*25.4+cornerOffset
double boardy=11.0*25.4+cornerOffset
// Scoot arm over so the paper doesn't awkwardly hang out over edge
double cornerNudge = -10
// radius of rounded corners on base plate
double cornerRadius=5;

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

    int bracketOneKeepawayDistance = 50

	double motorGearPlateThickness = 10
	double boardThickness =10
	
	def thrustBearingSize = "Thrust_1andAHalfinch"
	double radiusOfGraspingObject=12.5;
	
	double thrustBearing_inset_Into_bottom = 1
	double topOfHornToBotomOfBaseLinkDistance = movingPartClearence-thrustBearing_inset_Into_bottom
	

	double GripperServoYOffset = 35
	

	
	def cornerRad=2
	String boltsize = "M5x25"
	def insert=["heatedThreadedInsert", "M5"]
	def insertCamera=["heatedThreadedInsert", "M5"]
	def insertMeasurments= Vitamins.getConfiguration(insert[0],
		insert[1])
	double cameraInsertLength = insertMeasurments.installLength
	
	HashMap<String,Object> measurmentsMotor = Vitamins.getConfiguration(  "LewanSoulMotor","lx_224")
	HashMap<String,Object> measurmentsHorn = Vitamins.getConfiguration(  measurmentsMotor.shaftType,measurmentsMotor.shaftSize)
	
	double motorz =  measurmentsMotor.body_z
	double motorPassiveLinkSideWasherTHickness=measurmentsMotor.shoulderHeight
	double hornKeepawayLen = measurmentsHorn.mountPlateToHornTop
	double hornDiameter = measurmentsHorn.hornDiameter
	double centerTheMotorsValue=motorz/2;
	double linkYDimention = measurmentsMotor.body_x;
	double movingPartClearence =motorPassiveLinkSideWasherTHickness
	double totalMotorAndHorn = motorz+hornKeepawayLen+movingPartClearence;
	
	
	
	double linkThickness = hornKeepawayLen
	double centerlineToOuterSurfacePositiveZ = centerTheMotorsValue+hornKeepawayLen
	double centerlineToOuterSurfaceNegativeZ = -(centerTheMotorsValue+movingPartClearence+linkThickness)
	CSG linkBuildingBlockRoundCyl = new Cylinder(linkYDimention/2,linkYDimention/2,linkThickness,30)
		.toCSG()
	CSG linkBuildingBlockRoundSqu = new RoundedCube(linkYDimention,linkYDimention,linkThickness)
		.cornerRadius(cornerRad)
		.toCSG()
		.toZMin()
	CSG linkBuildingBlockRound = new RoundedCylinder(linkYDimention/2,linkThickness)
		.cornerRadius(cornerRad)
		.toCSG()
    CSG cameraBuildingBlockRound = new RoundedCylinder(linkYDimention/2,cameraInsertLength+1)
		.cornerRadius(cornerRad)
		.toCSG()
	CSG linkBuildingBlock = CSG.hullAll([
	linkBuildingBlockRound.movey(linkYDimention),
	linkBuildingBlockRound
	])
	.toZMin()
	.movey(-5)
	LengthParameter offset		= new LengthParameter("printerOffset",0.5,[2,0])
	double offsetValue = 0.6
	@Override
	public ArrayList<CSG> generateCad(DHParameterKinematics d, int linkIndex) {
		System.out.println( "Total motor and horn length "+totalMotorAndHorn)
		offset.setMM(offsetValue)
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
		CSG motorModel=   Vitamins.get(conf.getElectroMechanicalType(),conf.getElectroMechanicalSize())
		
	
		
		double zOffset = motorModel.getMaxZ()
		TransformNR locationOfMotorMount = new TransformNR(dh.DhStep(0)).inverse()
		def shaftLocation = locationOfMotorMount.copy()
		def thrustMeasurments= Vitamins.getConfiguration("ballBearing",
			thrustBearingSize)
		def baseCorRad = thrustMeasurments.outerDiameter/2+5
		
		double servoAllignmentAngle=0
		CSG gripperMotor=null;
		TransformNR locationOfGripperHinge=null
		TransformNR locationOfServo=null 
		double servoZOffset = 0;
		def insert=["heatedThreadedInsert", "M5"]
		def insertMeasurments= Vitamins.getConfiguration(insert[0],
			insert[1])
		if(linkIndex==0)
			shaftLocation.translateY(zOffset-topOfHornToBotomOfBaseLinkDistance)
		else
			shaftLocation.translateZ(centerTheMotorsValue)
		vitaminLocations.put(shaftLocation, [
				conf.getShaftType(),
				conf.getShaftSize()
		])

		TransformNR locationOfBearing = locationOfMotorMount.copy().translateY(thrustBearing_inset_Into_bottom)
		if(linkIndex==0) {
			vitaminLocations.put(locationOfBearing, [
				"ballBearing",
				thrustBearingSize
			])
		}
		CSG vitamin_LewanSoulHorn_round_m3_bolts = Vitamins.get("LewanSoulHorn", "round_m3_bolts")

		if(linkIndex==1) {
			def mountBoltOne =locationOfMotorMount.copy()
							.times(new TransformNR().translateZ(centerlineToOuterSurfacePositiveZ+linkThickness)
								.translateY(-bracketOneKeepawayDistance))
			def mountBoltTwo=mountBoltOne.copy()
								.times(new TransformNR()
									.translateY(+20))
							
			vitaminLocations.put(mountBoltOne,["capScrew", boltsize])
			vitaminLocations.put(mountBoltOne.times(new TransformNR().translateZ(-linkThickness-insertMeasurments.installLength)),
				insert)
			vitaminLocations.put(mountBoltTwo,["capScrew", boltsize])
			vitaminLocations.put(mountBoltTwo.times(new TransformNR().translateZ(-linkThickness-insertMeasurments.installLength)),
				insert)
			
		}
		if(linkIndex==2) {
			double theta = Math.toDegrees(dh.getTheta())
			servoAllignmentAngle = Math.toDegrees(Math.atan2(GripperServoYOffset,dh.getR()))-(90-theta)
			println "Angle of servo offset = "+servoAllignmentAngle+" "+GripperServoYOffset+" "+dh.getR()+" theta "+theta
			double hypot = Math.sqrt(Math.pow(dh.getR(), 2)+Math.pow(GripperServoYOffset, 2))
			double hingeBackset = 40
			if(dh.getSlaveMobileBase()!=null) {
				MobileBase hand = dh.getSlaveMobileBase();
				DHParameterKinematics gripperLimb=hand.getAllDHChains().get(0);
				hingeBackset = gripperLimb.getDH_R(0);
				
			}
			locationOfServo=locationOfMotorMount
			.copy()
			.times(
				new TransformNR(0,0,0,new RotationNR(0,0,90))
				)
			//.translateY(-linkYDimention/2)
			.times(
					new TransformNR(0,0,0,new RotationNR(-servoAllignmentAngle,0,0))
					)
			.translateY(-GripperServoYOffset)
			.times(new TransformNR().translateZ(linkYDimention/2))
			
			locationOfGripperHinge=locationOfServo
			.copy()
			.times(new TransformNR().translateY(hypot-hingeBackset))
			
//			.translateY(-hingeBackset*Math.sin(Math.toRadians(servoAllignmentAngle))-8)
//			.translateX(hypot-hingeBackset)
			
			
			
		
			vitaminLocations.put(locationOfGripperHinge,
					["capScrew", boltsize])
			vitaminLocations.put(locationOfGripperHinge.times(new TransformNR().translateZ(-linkYDimention)),
					insert)
			def mountBoltOne = locationOfServo.copy()
							.times(new TransformNR(0,0,0,new RotationNR(0,0,-90)))
							.times(new TransformNR().translateZ(centerlineToOuterSurfacePositiveZ+linkThickness)
								.translateX(-linkYDimention/2)
								.translateY(-5)
								)
			def mountBoltTwo=mountBoltOne.times(new TransformNR().translateY(linkYDimention))
			vitaminLocations.put(mountBoltOne,["capScrew", boltsize])
			vitaminLocations.put(mountBoltOne.times(new TransformNR().translateZ(-linkThickness-insertMeasurments.installLength)),
				insert)
			vitaminLocations.put(mountBoltTwo,["capScrew", boltsize])
			vitaminLocations.put(mountBoltTwo.times(new TransformNR().translateZ(-linkThickness-insertMeasurments.installLength)),
				insert)
			vitaminLocations.put(locationOfServo.times(new TransformNR(0,0,0,new RotationNR(0,180,0))), [
				"hobbyServo",
				"mg92b"
			])
			CSG srv =Vitamins.get("hobbyServo","mg92b")
			servoZOffset=srv.getMaxZ()
		}

		if(linkIndex!=d.getNumberOfLinks()-1 ){
			LinkConfiguration confPrior = d.getLinkConfiguration(i+1);
			def vitaminType = confPrior.getElectroMechanicalType()
			def vitaminSize = confPrior.getElectroMechanicalSize()
			//println "Adding Motor "+vitaminType
			def motorLocation = new TransformNR(0,0,centerTheMotorsValue,new RotationNR())
			if(linkIndex==0)
				motorLocation=motorLocation.times(new TransformNR(0,0,0,new RotationNR(0,180,0)))
			if(linkIndex==1)
					motorLocation=motorLocation.times(new TransformNR(0,0,0,new RotationNR(0,-90,0)))
			vitaminLocations.put(motorLocation, [
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
		def vitamins =[]
		for(TransformNR tr: vitaminLocations.keySet()) {
			def vitaminType = vitaminLocations.get(tr)[0]
			def vitaminSize = vitaminLocations.get(tr)[1]

			HashMap<String, Object>  measurments = Vitamins.getConfiguration( vitaminType,vitaminSize)
			offset.setMM(offsetValue)
			CSG vitaminCad=   Vitamins.get(vitaminType,vitaminSize)
			Transform move = TransformFactory.nrToCSG(tr)
			def part = vitaminCad.transformed(move)
			part.setManipulator(manipulator)
			vitamins.add(part)

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
		Transform actuatorSpace = TransformFactory.nrToCSG(locationOfMotorMount)
		def tipCupCircle = linkBuildingBlockRound.movez(centerlineToOuterSurfacePositiveZ)
		def gripperLug = linkBuildingBlockRound.movez(centerlineToOuterSurfaceNegativeZ)
		def actuatorCircle = tipCupCircle.transformed(actuatorSpace)
		def actuatorCirclekw = linkBuildingBlockRoundCyl.movez(centerlineToOuterSurfacePositiveZ).transformed(actuatorSpace)
		def passivLinkLug = gripperLug.transformed(actuatorSpace)
		double offsetOfLinks=0.0
		double braceBackSetFromMotorLinkTop=1.0
		if(linkIndex==1) {
			double braceDistance=-hornDiameter/2;
			
			double linkClearence = totalMotorAndHorn/2
			def mountMotorSidekw = linkBuildingBlockRoundCyl
										.movez(centerTheMotorsValue)
										.movex(-linkClearence-movingPartClearence)
			def mountMotorSide = linkBuildingBlockRound
										.movez(centerTheMotorsValue)
										.movex(-linkClearence-movingPartClearence)
			def mountPassiveSide = linkBuildingBlockRoundSqu
										.movez(-centerTheMotorsValue-linkThickness)
										.movex(-linkClearence-movingPartClearence)
		   def mountPassiveSideAlligned = linkBuildingBlockRoundSqu
										.movez(centerlineToOuterSurfaceNegativeZ)
										.movex(-linkClearence-movingPartClearence)
			def clearencelugMotorSide = mountMotorSide.movex(-dh.getR()+bracketOneKeepawayDistance)
			def clearencelugMotorSidekw = mountMotorSidekw.movex(-dh.getR()+bracketOneKeepawayDistance)
			def clearencelugPassiveSide = mountPassiveSide.movex(-dh.getR()+bracketOneKeepawayDistance)
			CSG motorLink = actuatorCircle.movez(-offsetOfLinks).movex(bracketOneKeepawayDistance)
			CSG motorLinkkw = actuatorCirclekw.movez(-offsetOfLinks).movex(bracketOneKeepawayDistance)
			def bracemountPassiveSideAlligned = linkBuildingBlockRound
													.movez(centerlineToOuterSurfacePositiveZ)
													.movez(-offsetOfLinks)
													.movey(braceDistance)
													.movex(-linkClearence-movingPartClearence-dh.getR()+bracketOneKeepawayDistance)
			def bracemountMotorSide=actuatorCircle
									.movez(-braceBackSetFromMotorLinkTop)
									.movex(dh.getR()-hornDiameter*1.5)
									.movey(braceDistance)
										
			def brace = CSG.unionAll([
				bracemountMotorSide,
				bracemountPassiveSideAlligned.movez(-braceBackSetFromMotorLinkTop)
				]).hull()
			brace = brace
						.union([
							brace
							.movez(-centerlineToOuterSurfacePositiveZ+centerlineToOuterSurfaceNegativeZ+offsetOfLinks+braceBackSetFromMotorLinkTop),
							clearencelugMotorSide,clearencelugPassiveSide
							]
						).hull()
			def passiveSide = mountPassiveSideAlligned.union(passivLinkLug).hull()
			def motorSidePlate = CSG.hullAll([clearencelugMotorSide,mountMotorSide]);
			motorSidePlate=CSG.hullAll([motorSidePlate,motorSidePlate.toZMax().movez(centerlineToOuterSurfacePositiveZ-offsetOfLinks)])
			def motorSidePlatekw = CSG.hullAll([clearencelugMotorSidekw,mountMotorSidekw]);
			motorSidePlatekw=CSG.hullAll([motorSidePlatekw,motorSidePlatekw.toZMax().movez(centerlineToOuterSurfacePositiveZ-offsetOfLinks)])
			
			def center = CSG.unionAll([mountPassiveSideAlligned,mountMotorSide,clearencelugMotorSide,clearencelugPassiveSide])
							.hull()
		    CSG motorToCut = Vitamins.get(conf.getElectroMechanicalType(),conf.getElectroMechanicalSize())
							.rotz(180)
							.movez(centerTheMotorsValue)
							.transformed(actuatorSpace)
			CSG MotorMountBracketkw = actuatorCirclekw.movez(-offsetOfLinks)
							.union(motorLinkkw)
							.hull()
			CSG MotorMountBracket = actuatorCircle.movez(-offsetOfLinks)
							.union(motorLink)
							.hull()
							.difference(vitamins)
			def FullBracket =CSG.unionAll([center,passiveSide,brace])
							//.difference(motorSidePlatekw.getBoundingBox())
							.difference(MotorMountBracket)
							.union(motorSidePlate)
							.difference(vitamins)
							.difference(motorToCut)

								
			
			MotorMountBracket.setColor(javafx.scene.paint.Color.DARKCYAN)
			FullBracket.setColor(javafx.scene.paint.Color.YELLOW)
			MotorMountBracket.setManipulator(manipulator)
			FullBracket.setManipulator(manipulator)
			FullBracket.setName("MiddleLinkMainBracket")
			MotorMountBracket.setName("MiddleLinkActuatorBracket")
			allCad.addAll(FullBracket,MotorMountBracket)
		}
		if(linkIndex==2) {
			CSG objectToGrab = new Sphere(radiusOfGraspingObject,32,16).toCSG()
			CSG box = objectToGrab.getBoundingBox().movex(-2)
			CSG insertpart = Vitamins.get(insert[0],insert[1])
								.roty(90)
								.toXMax()
								.movex(box.getMaxX())
			
			
			
			
			def corners =[]
			Transform gripperSpace = TransformFactory.nrToCSG(locationOfServo)
			
			// Move the kinematic location of the gripper based on how the CAD needs the hinge to be
			if(dh.getSlaveMobileBase()!=null) {
				MobileBase hand = dh.getSlaveMobileBase();
				DHParameterKinematics gripperLimb=hand.getAllDHChains().get(0);
				DHParameterKinematics gripperMotorLimb=hand.getAllDHChains().get(1);
				gripperLimb.setRobotToFiducialTransform(locationOfGripperHinge
					.times(new TransformNR(0,0,0,new RotationNR(0,90,0)))// align
					);
				gripperMotorLimb.setRobotToFiducialTransform(locationOfServo
						.times(new TransformNR(0,0,0,new RotationNR(0,180,0)))// align
						.times(new TransformNR().translateZ(servoZOffset))
						
						);
				gripperLimb.refreshPose();	
				gripperMotorLimb.refreshPose();
			}
			
			Transform hinge = TransformFactory.nrToCSG(locationOfGripperHinge)			
			CSG motorToCut = Vitamins.get(conf.getElectroMechanicalType(),conf.getElectroMechanicalSize())
							.rotz(90)
							.movez(centerTheMotorsValue)
							.transformed(actuatorSpace)			
			
			
			
			def servoCube = linkBuildingBlock.toXMax().movez(centerlineToOuterSurfacePositiveZ-offsetOfLinks).roty(90).transformed(gripperSpace)	
			def rightServoCube = linkBuildingBlock.toZMax().toXMin().movez(-centerlineToOuterSurfaceNegativeZ).roty(-90).transformed(gripperSpace)
			
			def servoBracket = servoCube.union(rightServoCube).hull()
			def supportBracket = rightServoCube.union(passivLinkLug).hull()
			def linkToCup = rightServoCube.union(gripperLug).hull()
			def ActuatorBracket = servoCube.union(actuatorCircle.movez(-offsetOfLinks)).hull()
									.difference(vitamins)
			
			CSG pincherCup = new  Cylinder(radiusOfGraspingObject/2,5).toCSG()
	
			def pincherBracket = gripperLug.union(pincherCup).hull()
			
			
			double hingeDiameter = 10
			def hingeBarrel = new RoundedCylinder(hingeDiameter/2,linkYDimention)
									.cornerRadius(cornerRad)
									.toCSG()
									.toZMax()
			def hingeLinkHole = new Cylinder(1,linkYDimention).toCSG()
									.toZMax()
									.movex(centerlineToOuterSurfaceNegativeZ*2/3)
			def hingeHole = new Cylinder(2.75,linkYDimention).toCSG()
									.toZMax()
			def hingeSlotCutter = new Cube(hingeDiameter*3+1,hingeDiameter*3,linkThickness+movingPartClearence/2).toCSG()
									.toXMax()
									.movex(hingeDiameter+movingPartClearence)
									.movez(-linkYDimention/2)
									.transformed(hinge)
			def innerBarrel = hingeBarrel
											.toXMax()
											.movex(-centerlineToOuterSurfaceNegativeZ)
			def hingeBrace = innerBarrel
								.transformed(hinge)
								.union(servoCube).union(rightServoCube)
								.hull()
			
			def hingeBarrelMount = hingeBarrel
									.union(innerBarrel)
									.transformed(hinge)
									.union(rightServoCube)
									.hull()
									.union(hingeBrace)
									.difference(hingeSlotCutter)
			def movingCupHingeLug = tipCupCircle.roty(90)
									.movez(-linkYDimention/2)
									.transformed(hinge)
			def knotches = new Cube(centerlineToOuterSurfacePositiveZ-centerlineToOuterSurfaceNegativeZ+linkThickness*3,5,3).toCSG()
							.toXMin()
							.toZMax()
							.movex(centerlineToOuterSurfaceNegativeZ-linkThickness)
							.movey(21)
							.transformed(hinge)
			def movingHingeBarrel =  new Cylinder(hingeDiameter/2,linkThickness).toCSG()
											.movez(-linkYDimention/2-linkThickness/2)
			def movingPart = movingHingeBarrel
								.union(movingHingeBarrel
										.movex(-centerlineToOuterSurfacePositiveZ)
										)
								.hull()
								.transformed(hinge)
								.union(movingCupHingeLug.union(tipCupCircle).hull())
								.difference(hingeLinkHole.transformed(hinge))
								.difference(hingeHole.transformed(hinge))
			def gripperMovingCupstl = tipCupCircle.union(pincherCup).hull()
								.union(movingPart)
								.difference(objectToGrab)
								.difference(knotches)
								//.difference(vitamins)
								.transformed(hinge.inverse())// move the gripper to the tip
								
			def FullBracket =CSG.unionAll([servoBracket,supportBracket,linkToCup,pincherBracket,hingeBarrelMount])
									.difference(objectToGrab)
									.difference(vitamins)
									.difference(ActuatorBracket)
									.difference(motorToCut)
									.difference(knotches)
									
			objectToGrab=objectToGrab.intersect(box)
									.difference(insertpart)
			// Save the gripper cup to an STL to be loaded by the hand cad script and hung on the hand 
			File dir= ScriptingEngine.getRepositoryCloneDirectory(d.getGitCadEngine()[0])
			FileUtil.write(Paths.get(dir.getAbsolutePath()+"/gripper.stl"),
				gripperMovingCupstl
				
				.toStlString());
			FullBracket.setColor(javafx.scene.paint.Color.LIGHTBLUE)
			
			ActuatorBracket.setColor(javafx.scene.paint.Color.DARKCYAN)
			objectToGrab.setColor(javafx.scene.paint.Color.RED)
			ActuatorBracket.setManipulator(manipulator)
			FullBracket.setManipulator(manipulator)
			
			objectToGrab.setManipulator(manipulator)
			
			FullBracket.setName("LastLinkMainBracket")
			ActuatorBracket.setName("LastLinkActuatorBracket")
			
			objectToGrab.setName("GamePiece")
			objectToGrab.setManufacturing ({ mfg ->
				return mfg.roty(-90).toZMin()				
			})
			
			allCad.addAll(FullBracket,ActuatorBracket,objectToGrab)
		}
		
		if(linkIndex==0) {
			def z = dh.getD()-linkYDimention/2-movingPartClearence
			def supportBeam= new RoundedCube(linkYDimention+linkThickness*2.0,40+linkThickness*2,z)
								.cornerRadius(cornerRad)
								.toCSG()
								.toZMax()
			
			def	baseOfArm = Parabola.coneByHeight(baseCorRad, 25)
						.rotx(90)
						.toZMin()
						.movez(movingPartClearence)
						
			baseOfArm=baseOfArm
						.difference(
							baseOfArm
							.getBoundingBox()
							.movez(z)
							)
						.union(supportBeam.movez(z+movingPartClearence))
						.transformed( TransformFactory.nrToCSG(locationOfBearing))
						.difference(vitamins)
			baseOfArm.setColor(javafx.scene.paint.Color.WHITE)
			baseOfArm.setManipulator(manipulator)
			baseOfArm.setName("BaseCone")
			baseOfArm.setManufacturing ({ mfg ->
				return mfg.rotx(-90).toZMin()				
			})
			allCad.add(baseOfArm)
		}
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
							allCad.clear()
					}
					public void onConnect(BowlerAbstractDevice source) {}
				})
		for(CSG c:vitamins) {
			c.setManufacturing ({ mfg ->
			return null;
		})
		}
		allCad.addAll(vitamins)
		vitamins.clear()
		return allCad;
	}
	
	@Override
	public ArrayList<CSG> generateBody(MobileBase b ) {

		def vitaminLocations = new HashMap<TransformNR,ArrayList<String>>()
		ArrayList<CSG> allCad=new ArrayList<>();
		double baseGrid = grid;
		double baseBoltThickness=15;
		double baseCoreheight = 1;
		def insertMeasurments= Vitamins.getConfiguration(insert[0],
			insert[1])
		double xOffset = grid*7.5;
		double yOffset = -grid*0.5;
		def cameraHeight_parameterized
		def cameraHeight_default = (25.4*9.75)
		cameraHeight_parameterized = new LengthParameter("Camera Stand Height",cameraHeight_default,[cameraHeight_default+200,cameraHeight_default-200])
		def cameraHeight = cameraHeight_parameterized.getMM();
		def cameraNut = new TransformNR(xOffset+grid/2,yOffset+grid/2,0,new RotationNR(0,0,0))
		
		CSG cameraBoltHole = new Cylinder(2.5,cameraInsertLength+cameraHeight+2).toCSG()
		CSG cameraCone =  new Cylinder(grid/2, // Radius at the bottom
                      		insertMeasurments.diameter/2+2, // Radius at the top
                      		cameraHeight, // Height
                      		(int)8 //resolution
                      		).toCSG()//convert to CSG to display 
							 .transformed(TransformFactory.nrToCSG(cameraNut))
		def mountLoacionsCamera = [
			new TransformNR(xOffset,yOffset,0,new RotationNR(180,0,0)),
			new TransformNR(xOffset,yOffset+grid,0,new RotationNR(180,0,0)),
			new TransformNR(xOffset+grid,yOffset,0,new RotationNR(180,0,0)),
			new TransformNR(xOffset+grid,yOffset+grid,0,new RotationNR(180,0,0))
		]
		def corners =[cameraCone.movez(1)]
		for(TransformNR t:mountLoacionsCamera) {
			def tr = TransformFactory.nrToCSG(t)
			corners.add(cameraBuildingBlockRound.toZMax().transformed(tr).movez(1)
				)
				
		}
		vitaminLocations.put(cameraNut.copy().translateZ(cameraHeight-cameraInsertLength+1), [
			insertCamera[0],
			insertCamera[1]
		])
//		def cameraNutPart = Vitamins.get( insertCamera[0],insertCamera[1])
//								.transformed(TransformFactory.nrToCSG(cameraNut))
		def cameraBlock = CSG.unionAll(corners).hull()
							.difference(cameraBoltHole.transformed(TransformFactory.nrToCSG(cameraNut)))
							//.difference(cameraNutPart)
		//allCad.add(cameraBlock)
		//return allCad
		

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
			TransformNR pinionRoot = locationOfMotorMount.copy().translateZ(topOfHornToBotomOfBaseLinkDistance+1)
			def extractionLocationOfMotor =locationOfMotorMount.copy().translateZ(-20)

			vitaminLocations.put(locationOfBearing.copy().translateZ(-1), [
				"ballBearing",
				thrustBearingSize
			])
			vitaminLocations.put(locationOfMotorMount.copy().translateZ(topOfHornToBotomOfBaseLinkDistance+1), [
				conf.getElectroMechanicalType(),
				conf.getElectroMechanicalSize()
			])
			vitaminLocations.put(extractionLocationOfMotor, [
				conf.getElectroMechanicalType(),
				conf.getElectroMechanicalSize()
			])
			// cut the hole in the base for the shaft
			vitaminLocations.put(pinionRoot, [
				conf.getShaftType(),
				conf.getShaftSize()
			])
//			vitaminLocations.put(pinionRoot.copy().translateZ(3), [
//				conf.getShaftType(),
//				conf.getShaftSize()
//			])
		}
		
		double yOffsetFeducial = baseGrid*4
		def mountLoacionsFeducials = [
			new TransformNR(0,-yOffsetFeducial,0,new RotationNR(180,0,0)),// feducial
			new TransformNR(0,yOffsetFeducial,0,new RotationNR(180,0,0)),// feducial
			new TransformNR(baseGrid+yOffsetFeducial,yOffsetFeducial,0,new RotationNR(180,0,0)),// feducial
			new TransformNR(yOffsetFeducial-baseGrid,0,0,new RotationNR(180,0,0)),// feducial
			new TransformNR(baseGrid+yOffsetFeducial,-yOffsetFeducial,0,new RotationNR(180,0,0)),// feducial

		]

		
		def mountLoacions = [
			new TransformNR(baseGrid,0,0,new RotationNR(180,0,0)),//base
			new TransformNR(-baseGrid,baseGrid,0,new RotationNR(180,0,0)),//base
			new TransformNR(-baseGrid,-baseGrid,0,new RotationNR(180,0,0)),//base
			
		]
		mountLoacions.forEach{
			vitaminLocations.put(it.copy().translateZ(-boardThickness),
					["capScrew", boltsize])
			vitaminLocations.put(it.copy().translateZ(insertMeasurments.installLength),
					insert)

		}
		mountLoacionsFeducials.forEach{
			vitaminLocations.put(it.copy().translateZ(-boardThickness),
					["capScrew", boltsize])
			vitaminLocations.put(it.copy().translateZ(insertMeasurments.installLength),
					insert)

		}
		def mountLocationsCorners = [
			//new TransformNR(-(boardx/2 - cornerRadius), -(boardy/2 - cornerRadius),0,new RotationNR(180,0,0)),// corner mount
			//new TransformNR(-(boardx/2 - cornerRadius),  (boardy/2 - cornerRadius),0,new RotationNR(180,0,0)),// corner mount
			//new TransformNR( (boardx/2 - cornerRadius), -(boardy/2 - cornerRadius),0,new RotationNR(180,0,0)),// corner mount
			//new TransformNR( (boardx/2 - cornerRadius),  (boardy/2 - cornerRadius),0,new RotationNR(180,0,0))// corner mount
			]
		mountLocationsCorners.forEach{
			vitaminLocations.put(it.copy().translateZ(-boardThickness),
					["capScrew", boltsize])
			vitaminLocations.put(it.copy().translateZ(insertMeasurments.installLength),
					insert)

		}
		mountLoacionsCamera.forEach{
			vitaminLocations.put(it.copy().translateZ(-boardThickness),
					["capScrew", boltsize])
			vitaminLocations.put(it.copy().translateZ(cameraInsertLength+1),
					insert)

		}
		double totalMass = 0;
		TransformNR centerOfMassFromCentroid=new TransformNR();
		def vitamins=[]
		mountLoacionsCamera.forEach{
			vitamins.add(new Cylinder(5.75/2,cameraInsertLength)
				.toCSG()
				.toZMax()
				.transformed(TransformFactory.nrToCSG(it))
				)

		}
		for(TransformNR tr: vitaminLocations.keySet()) {
			def vitaminType = vitaminLocations.get(tr)[0]
			def vitaminSize = vitaminLocations.get(tr)[1]

			HashMap<String, Object>  measurments = Vitamins.getConfiguration( vitaminType,vitaminSize)
			offset.setMM(offsetValue)
			CSG vitaminCad=   Vitamins.get(vitaminType,vitaminSize)
			Transform move = TransformFactory.nrToCSG(tr)
			CSG part = vitaminCad.transformed(move)
			part.setManipulator(b.getRootListener())
			vitamins.add(part)

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
		
		cameraBlock=cameraBlock.difference(vitamins)
		cameraBlock.setParameter(cameraHeight_parameterized)
		cameraBlock.setColor(javafx.scene.paint.Color.BLUE)
		cameraBlock.setName("CameraStandMount")
		cameraBlock.setManufacturing ({ mfg ->
			return mfg.toZMin()
		})
		cameraBlock.setManipulator(b.getRootListener())
		
		//Do additional CAD and add to the running CoM
		def thrustMeasurments= Vitamins.getConfiguration("ballBearing",
				thrustBearingSize)
		def baseCorRad = thrustMeasurments.outerDiameter/2+5
		CSG baseCore = new Cylinder(baseCorRad,baseCorRad,baseCoreheight,36).toCSG()
		CSG baseCoreshort = new Cylinder(baseCorRad,baseCorRad,baseCoreheight*3.0/4.0,36).toCSG()
		CSG mountLug = new Cylinder(15,15,baseBoltThickness,36).toCSG().toZMax()
		CSG mountCap = Parabola.coneByHeight(15, 8)
				.rotx(-90)
				.toZMax()
				.movez(-baseBoltThickness)
		CSG mountUnit= mountLug.union(mountCap)
		def coreParts=[baseCore]
		def boltHolePattern = []
		def boltHoleKeepawayPattern = []
		def bolt = new Cylinder(2.6,20).toCSG()
					.movez(-10)
		def boltkeepaway = new Cylinder(5,20).toCSG()
					.movez(-10)
		mountLoacions.forEach{
			
			def place =com.neuronrobotics.bowlerstudio.physics.TransformFactory.nrToCSG(it)
			boltHolePattern.add(bolt.transformed(place))
			boltHoleKeepawayPattern.add(boltkeepaway.transformed(place))
			coreParts.add(
					CSG.hullAll(mountLug
					.transformed(place)
					,baseCoreshort)
					)
			coreParts.add(mountCap
					.transformed(place)
					)
		}
		
		def locationOfCalibration = new TransformNR(0,-50,15, new RotationNR())
		DHParameterKinematics dev = b.getAllDHChains().get(0)
		//dev.setDesiredTaskSpaceTransform(locationOfCalibration, 0);
		def jointSpaceVect = dev.inverseKinematics(dev.inverseOffset(locationOfCalibration));
		def poseInCal = dev.forwardOffset(dev.forwardKinematics(jointSpaceVect));
		println "\n\nCalibration Values "+jointSpaceVect+"\n at pose: "+poseInCal+"\n\n"
				
		def calibrationFrame = TransformFactory.nrToCSG(locationOfCalibration)
								.movex(centerlineToOuterSurfaceNegativeZ)
		def calibrationFramemountUnit=mountUnit
										.rotx(180)
										.toYMin()
										.transformed(calibrationFrame)
										.toZMin()
										
		// assemble the base
		def calibrationTipKeepaway =new RoundedCylinder(linkYDimention/2,centerlineToOuterSurfacePositiveZ-centerlineToOuterSurfaceNegativeZ)
											.cornerRadius(cornerRad)
											.toCSG()
											.roty(-90)
									.transformed(calibrationFrame)
		coreParts.add(calibrationTipKeepaway)			
		def cordCutter = new Cube(10,40,30).toCSG()
							.toYMin()
							.toZMax()
							.movez(baseCoreheight-37)
							.movez(topOfHornToBotomOfBaseLinkDistance+5)
		//pcbScrewXSpacing
		//pcbScrewYSpacing
	    //pcbScrewMountHeight
		

		
							
		def points = [	new Vector3d(10,0,0),
					new Vector3d(0, 0, 5),
					new Vector3d(0, -3, 0),
					new Vector3d(0, 3, 0)
		]
		CSG pointer = HullUtil.hull(points)
		
		def Base = CSG.unionAll(coreParts)
				.union(calibrationFramemountUnit)
				.union(calibrationFramemountUnit.mirrory())
				//.difference(vitamin_roundMotor_WPI_gb37y3530bracketOneKeepawayDistanceen)
				.difference(vitamins)
				.difference(calibrationTipKeepaway)
				.difference(cordCutter);
				
			

				
		Base = Base.intersect(Base.getBoundingBox().toXMin().movex(-baseCorRad))		
		Base = Base.union(pointer.movex(Base.getMaxX()-2))
						.union(pointer.rotz(90).movey(-baseCorRad+2))

		def pcbmount = ScriptingEngine.gitScriptRun(
				"https://github.com/Hephaestus-Arm/HephaestusArm2.git", // git location of the library
				"pcbmountpoints.groovy" , // file to load
				// Parameters passed to the funcetion
				[45.72, //Holes X Spacing
					0, //Holes Y Spacing
					5, //Pillar Dia
					1.8, //Hole Dia
					2, //Height
				])
		
		def pcbmounttop = ScriptingEngine.gitScriptRun(
			"https://github.com/Hephaestus-Arm/HephaestusArm2.git", // git location of the library
			"pcbmountpoints.groovy" , // file to load
			// Parameters passed to the funcetion
			[0, //Holes X Spacing
				0, //Holes Y Spacing
				5, //Pillar Dia
				1.8, //Hole Dia
				2, //Height
			])
		double extra = Math.abs(Base.getMinX())
		pcbmount = pcbmount.union(pcbmounttop.movey(40)).rotz(90).roty(90).movex(Base.getMinX())
		pcbmount = pcbmount.movez(-pcbmount.getMinZ()+2)


		Base.setColor(javafx.scene.paint.Color.PINK)
		// add it to the return list
		Base.setManipulator(b.getRootListener())
		for(def c:vitamins) {
			c.setManufacturing ({ mfg ->
			return null;
		})
		}


		Base = Base.union(pcbmount)


		def paper = new Cube(8.5*25.4,11.0*25.4,1).toCSG()
						.toZMax()
						.toXMin()
						.movez(1)
						.movex(-extra)
						.difference(boltHolePattern)

		
		allCad.add(cameraBlock)

		// Cyl for radius
		def cornerCyl = new Cylinder(cornerRadius,cornerRadius,boardThickness,80).toCSG();

		
		// Make 4 copies and hull them.
		def board = CSG.hullAll([
			cornerCyl.movex(-(boardx/2 - cornerRadius)).movey(-(boardy/2 - cornerRadius)),
			cornerCyl.movex(-(boardx/2 - cornerRadius)).movey((boardy/2 - cornerRadius)),
			cornerCyl.movex((boardx/2 - cornerRadius)).movey(-(boardy/2 - cornerRadius)),
			cornerCyl.movex((boardx/2 - cornerRadius)).movey((boardy/2 - cornerRadius))			
			])
		
		// hacky non vitamin hole solution
		
		def holeCyl = new Cylinder(5,5,boardThickness+1,80).toCSG().movez(-0.5)
		double holenudge = 10
		def hackyholes = CSG.unionAll([
			holeCyl.movex(-(boardx/2 - cornerRadius - holenudge)).movey(-(boardy/2 - cornerRadius - holenudge)),
			holeCyl.movex(-(boardx/2 - cornerRadius - holenudge)).movey((boardy/2 - cornerRadius - holenudge)),
			holeCyl.movex((boardx/2 - cornerRadius - holenudge)).movey(-(boardy/2 - cornerRadius - holenudge)),
			holeCyl.movex((boardx/2 - cornerRadius - holenudge)).movey((boardy/2 - cornerRadius - holenudge))
			])
		board = board.difference(hackyholes)

		board = board.movex(cornerNudge).movey(cornerNudge)
		
		board = board.toZMax()
						.toXMin()
						.movex(-extra)
						.movey(cornerOffset/2)
						//.difference(boltHolePattern)
						.difference(vitamins)
		

						
		
		board.setColor(javafx.scene.paint.Color.WHITESMOKE)
		def cardboard = new Cube(boardx,boardy,2).toCSG()
		.toZMax()
		.toXMin()
		.movex(-extra)
		.movey(cornerOffset/2)
		.movez(-boardThickness)
		.difference(boltHoleKeepawayPattern)
		.difference(vitamins)

		cardboard.setColor(javafx.scene.paint.Color.SADDLEBROWN)
		
		cardboard.addExportFormat("svg")
		board.addExportFormat("svg")
		paper.addExportFormat("svg")
		paper.setManufacturing ({ mfg ->
			return mfg.toZMin()
		})
		board.setManufacturing ({ mfg ->
			return mfg.toZMin()
		})
		cardboard.setManufacturing ({ mfg ->
			return mfg.toZMin()
		})
		paper.setColor(javafx.scene.paint.Color.WHITE)
		
		allCad.addAll(Base,paper,pcbmount,board)//cardboard,board,paper
		Base.addExportFormat("stl")
		Base.addExportFormat("svg")
		Base.setName("BaseMount")
		b.setMassKg(totalMass)
		b.setCenterOfMassFromCentroid(centerOfMassFromCentroid)
		
		allCad.addAll(vitamins)
		return allCad;
	}
};

