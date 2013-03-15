## CompGeom

A computational geometry library using arbitrary-precision arithmetic where 
possible, written in Java.


## How to build CompGeom

* if you have Ant installed, it's as easy as `ant compile` to compile all
  source files in the `src/` directory. Or `ant jar` to compile all source
  files except the ones in the `src/test/` directory and pack all compiled
  classes inside a JAR file.
* if Ant is not installed, I recommend importing the project in an IDE like
  Eclipse, NetBeans or IntelliJ IDEA and let the IDE compile the files, or
  create a JAR file of the compiled classes.


## System Requirements

 * JDK/JRE 1.6 or higher
 * Ant (optional)

## Usage

 * see the `src/main/compgeom/demos/Examples.java` class for some examples
   how to use the various algorithms
 * also see the API docs in the directory `doc/api/`
 * when executing the CompGeom JAR file (`java -jar CompGeom-x.y.z.jar`), the
   `DemosLauncher` class is executed which prompts a graphical dialogue window
   that lets you choose a demo to run

## Supported

 * rational used for exact calculations
 * geometric primitives (point, line segment, line, polygon, rectangle)
 * Graham scan (convex hulls)
 * rotating calipers to find the minimum bounding box of a convex hull
 * closest pair of points
 * Shamos-Hoey algorithm
 * Bentley-Ottmann
  
## Working on...

 * Voronoi diagram
 * Delaunay triangulation
 * polygon triangulation
