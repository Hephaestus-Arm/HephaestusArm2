import com.neuronrobotics.sdk.addons.kinematics.math.RotationNR
import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR
import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cube
import eu.mihosoft.vrl.v3d.Cylinder
import com.neuronrobotics.bowlerstudio.physics.TransformFactory
//Your code here

if (args==null) {
	args=[
		120+(25.4*1.5), //Camera Height
		5, //Camera Insert Length
		5, //Camera Insert Diameter
		25 //Grid
		
	]
}
double grid=args[3]
double cameraInsertDia=args[2]
double cameraInsertLength=args[1]
double cameraHeight=args[0]
double xOffset=0
double yOffset=0
def cameraNut = new TransformNR(xOffset+grid/2,yOffset+grid/2,0,new RotationNR(0,0,0))
CSG cameraBoltHole = new Cylinder(2.5,cameraInsertLength+cameraHeight+2).toCSG()
CSG cameraCone =  new Cylinder(grid/2, // Radius at the bottom
	cameraInsertDia/2+2, // Radius at the top
	cameraHeight, // Height
	(int)8 //resolution
	).toCSG()//convert to CSG to display
   .transformed(TransformFactory.nrToCSG(cameraNut))