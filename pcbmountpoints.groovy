import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Cylinder
import eu.mihosoft.vrl.v3d.Transform

//Your code here

if (args==null) {
	args=[
		45.72,
		//Holes X Spacing
		20,//54.61,
		//Holes Y Spacing
		5,
		//Pillar Dia
		2,
		//Hole Dia
		5, //Height
	]
}

double pcbMountHeight = args[4]
double pcbScrewXSpacing = args[0]
double pcbScrewYSpacing = args[1]
double pcbPillarDia = args[2]
double pcbHoleDia = args[3]


def pcbmountstud = new Cylinder(pcbPillarDia,pcbPillarDia/2,pcbMountHeight).toCSG()
def pcbmountbase = new Cylinder(pcbPillarDia,pcbMountHeight).toCSG()
							.toZMax()
pcbmountstud = pcbmountstud.union(pcbmountbase)
def pcbmountstuds = CSG.unionAll([
	pcbmountstud.movex(pcbScrewXSpacing/2.0).movey(-pcbScrewYSpacing/2.0),
	pcbmountstud.movex(pcbScrewXSpacing/2.0).movey(pcbScrewYSpacing/2.0),
	pcbmountstud.movex(-pcbScrewXSpacing/2.0).movey(-pcbScrewYSpacing/2.0),
	pcbmountstud.movex(-pcbScrewXSpacing/2.0).movey(pcbScrewYSpacing/2.0)

])

def pcbscrewholecut = new Cylinder(pcbHoleDia/2,pcbMountHeight+0.2).toCSG().movez(-0.1)
def pcbscrewholes = CSG.unionAll([
	pcbscrewholecut.movex(pcbScrewXSpacing/2.0).movey(-pcbScrewYSpacing/2.0),
	pcbscrewholecut.movex(pcbScrewXSpacing/2.0).movey(pcbScrewYSpacing/2.0),
	pcbscrewholecut.movex(-pcbScrewXSpacing/2.0).movey(-pcbScrewYSpacing/2.0),
	pcbscrewholecut.movex(-pcbScrewXSpacing/2.0).movey(pcbScrewYSpacing/2.0)

])


return pcbmountstuds.difference(pcbscrewholes)