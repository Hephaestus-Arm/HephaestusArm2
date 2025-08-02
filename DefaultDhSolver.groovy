import com.neuronrobotics.bowlerstudio.physics.TransformFactory
import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine
import com.neuronrobotics.sdk.addons.kinematics.DHChain;
import com.neuronrobotics.sdk.addons.kinematics.DhInverseSolver;
import com.neuronrobotics.sdk.addons.kinematics.math.RotationNR;
import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR;
import java.util.ArrayList;

import com.neuronrobotics.sdk.addons.kinematics.DHChain;
import com.neuronrobotics.sdk.addons.kinematics.DHLink;
import com.neuronrobotics.sdk.addons.kinematics.DhInverseSolver;
import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR;
import com.neuronrobotics.sdk.common.Log;
import Jama.Matrix;
import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cube
import eu.mihosoft.vrl.v3d.Cylinder
import eu.mihosoft.vrl.v3d.Transform;
import groovy.transform.CompileStatic
import javafx.application.Platform
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
@CompileStatic
public class scriptJavaIKModel implements DhInverseSolver {
	boolean debug = true;
	CSG tipPointer =null;
	CSG tipPointer2 =null;
	CSG tipPointer3 =null;
	int limbIndex =0;
	DhInverseSolver sixDofSolver=null;
	public scriptJavaIKModel(int index){
		limbIndex=index;
	}
	
	public double[] inverseKinematics1dof(TransformNR target, double[] jointSpaceVector, DHChain chain) {
		double z = target.getZ();
		double y = target.getY();
		double x = target.getX();
		double a1 = Math.atan2(y , x);
		double a1d = Math.toDegrees(a1);
		
		jointSpaceVector[0]=a1d;
		
		return jointSpaceVector;
	}
	
	@Override 
	@SuppressWarnings("unchecked")
	public double[] inverseKinematics(TransformNR target, double[] jointSpaceVector, DHChain chain) {
		ArrayList<DHLink> links = chain.getLinks();
		if(links.size()==1) {
			return inverseKinematics1dof(target,jointSpaceVector,chain)
		}
		if(links.size()==3 || (links.size()==4 && (Math.abs(links.get(2).alpha)<0.001)))
			return inverseKinematics34dof(target,jointSpaceVector,chain);
		if(sixDofSolver==null)
			sixDofSolver=(DhInverseSolver)ScriptingEngine.gitScriptRun("https://github.com/madhephaestus/6dofServoArm.git","DefaultDhSolver.groovy")
		return sixDofSolver.inverseKinematics(target,jointSpaceVector,chain)
	}
	TransformNR linkOffset(DHLink link) {
		return new TransformNR(link.DhStep(0))
	}
	
	public double[] inverseKinematics34dof(TransformNR target, double[] jointSpaceVector, DHChain chain) {
		//System.out.println("My IK");
		//		try {
		ArrayList<DHLink> links = chain.getLinks();
		int linkNum = jointSpaceVector.length;

		double z = target.getZ();
		double y = target.getY();
		double x = target.getX();
		//		  z = Math.round(z*100.0)/100.0;
		//		  y = Math.round(y*100.0)/100.0;
		//            x = Math.round(x*100.0)/100.0;
		//
		RotationNR q = target.getRotation();

		//System.out.println("elevation: " + elev);
		//System.out.println("z: " + z);
		//System.out.println("y: " + y);
		//System.out.println("x: " + x);

		//double Oang = Math.PI/2 + q.getRotationElevation();
		//            double Oang = Math.toRadians(45);
		//
		//            double Oanginv = (Math.PI/2) - Oang;

		double l1_d = links.get(0).getR();
		double l2_d = links.get(1).getR();
		double l3_d = links.get(2).getR();

		double l4_d=0;// in 3 dof, this is 0
		if(links.size()>3)
			l4_d   = links.get(3).getR();

		//System.out.println("L1: " + l1_d);
		//System.out.println("L2: " + l2_d);
		//System.out.println("L3: " + l3_d);
		//System.out.println("L4: " + l4_d);


		double[] inv = new double[linkNum];
		double a1 = Math.atan2(y , x);
		double a1d = Math.toDegrees(a1);

		def newTip = new Transform()
				.movex(x)
				.movey(y)
				.movez(z)
				.rotz(a1d)
		//println newTip

		x=newTip.getX()
		y=newTip.getY()
		z=newTip.getZ()
		//System.out.println(" Base Angle : " + a1d);
		//System.out.println(" Base Orented z: " + z);
		//System.out.println(" Base Orented y: " + y);
		//System.out.println(" Base Orented x: " + x);

		double a2 = Math.atan2(z,x); // Z angle using x axis and z axis
		double a2d = Math.toDegrees(a2);
		//println a2d
		double elev = Math.toDegrees(q.getRotationElevation() )
		//println "R Vector Angle "+a2d

		//		double r1 = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)); // X and Y plane Vector
		//		double r2 = Math.sqrt(Math.pow(x, 2) + Math.pow(y,2)+Math.pow(z, 2)); // Leg Vector
		//		double r3 = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)); // x and z vector
		/*
		 def rvector = new Cube(r2,1,1).toCSG()
		 .toXMin()
		 .roty(a2d)
		 def rvectorOrig = rvector.rotz(-a1d)
		 .setColor(javafx.scene.paint.Color.BLUE)
		 BowlerStudioController.addCsg(rvector)
		 BowlerStudioController.addCsg(rvectorOrig)
		 */
		def wristCenter = new Transform()
				.movex(-l4_d)
				.roty(-elev)
				.movey(y)
				.movez(z-links.get(0).getD())
				.movex(x-l1_d)
		/*
		 def foot = new Cube(l4_d>0?l4_d:1,1,1).toCSG()
		 .toXMin()
		 .transformed(wristCenter)
		 .setColor(javafx.scene.paint.Color.AQUA)
		 BowlerStudioController.addCsg(foot)
		 */
		x=wristCenter.getX()
		y=wristCenter.getY()
		z=wristCenter.getZ()
		double wristAngle = Math.atan2(z,x);
		double wristAngleDeg =Math.toDegrees(wristAngle)
		double wristVect =  Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)); // x and z vector
		if(wristVect>l2_d+l3_d)
			throw new ArithmeticException("Total reach longer than possible "+inv);
		//System.out.println(" Wrist Angle: " + wristAngleDeg);
		//System.out.println(" Wrist Vect: " + wristVect);
		//System.out.println(" Wrist z: " + z);
		//System.out.println(" Wrist y: " + y);
		//System.out.println(" Wrist x: " + x);
		/*
		 def wristVector = new Cube(wristVect,1,1).toCSG()
		 .toXMin()
		 .roty(wristAngleDeg)
		 .setColor(javafx.scene.paint.Color.WHITE)
		 BowlerStudioController.addCsg(wristVector)
		 */
		double shoulderTiltAngle = Math.toDegrees(Math.acos(
				(Math.pow(l2_d,2)+Math.pow(wristVect,2)-Math.pow(l3_d,2))/
				(2*l2_d*wristVect)
				))
		double elbowTiltAngle = Math.toDegrees(Math.acos(
				(Math.pow(l3_d,2)+Math.pow(l2_d,2)-Math.pow(wristVect,2))/
				(2*l3_d*l2_d)
				))
		/*
		 def shoulderVector = new Cube(l2_d,1,1).toCSG()
		 .toXMin()
		 .roty(wristAngleDeg+shoulderTiltAngle)
		 .setColor(javafx.scene.paint.Color.GRAY)
		 BowlerStudioController.addCsg(shoulderVector)
		 */
		inv[0]=a1d
		if(Math.toDegrees(links.get(2).getTheta())<0){
			inv[1]=wristAngleDeg+shoulderTiltAngle-Math.toDegrees(links.get(1).getTheta())
			inv[2]=elbowTiltAngle-180-Math.toDegrees(links.get(2).getTheta())
		}else{
			inv[1]=-(wristAngleDeg+shoulderTiltAngle+Math.toDegrees(links.get(1).getTheta()))
			inv[2]=(180-elbowTiltAngle-Math.toDegrees(links.get(2).getTheta()))
		}
		if(links.size()>3)
			inv[3]=-inv[1]-inv[2]-Math.toDegrees(links.get(3).getTheta())-elev-
					Math.toDegrees(links.get(1).getTheta())-
					Math.toDegrees(links.get(2).getTheta())
		//System.out.println(inv[0]);
		//System.out.println(inv[1]);
		//System.out.println(inv[2]);
		//System.out.println(inv[3]);
		if(links.size()>3)
			if(Double.isNaN(inv[0]) || Double.isNaN(inv[1]) || Double.isNaN(inv[2]) || Double.isNaN(inv[3]))
				throw new ArithmeticException("Can't move to that position "+inv);
			else if(Double.isNaN(inv[0]) || Double.isNaN(inv[1]) || Double.isNaN(inv[2]) )
				throw new ArithmeticException("Can't move to that position "+inv);

		//println "Success "+inv
		return inv;
		//		} catch (Throwable t) {
		//			BowlerStudio.printStackTrace(t);
		//			return jointSpaceVector;
		//		}
	}

}

if(args==null)
	args=[0]
return new scriptJavaIKModel (args[0])