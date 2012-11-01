+-----------------------------------------------------------------------------+
| CompGeom
+----------+
  ~ version      : Version: 0.3
  ~ release date : 27-04-2010
  ~ contact      : bart@big-o.nl

+-----------------------------------------------------------------------------+
| CompGeom
+----------+
  ~ A computational geometry library using arbitrary-precision arithmetic
    where possible, written in Java.

+-----------------------------------------------------------------------------+
| How to build CompGeom
+-----------------------+
  ~ If you have Ant installed, it's as easy as `ant compile` to compile all
    source files in the 'src/' directory. Or `ant jar` to compile all source
    files except the ones in the 'src/test/' directory and pack all compiled
    classes inside a JAR file.
  ~ If Ant is not installed, I recommend importing the project in an IDE like
    Eclipse, NetBeans or IntelliJ IDEA and let the IDE compile the files, or
    create a JAR file of the compiled classes.

+-----------------------------------------------------------------------------+
| System Requirements
+---------------------+
  ~ JDK/JRE 1.6 or higher
  ~ Ant (optional)

+-----------------------------------------------------------------------------+
| Usage
+-------+
  ~ See the 'src/main/compgeom/demos/Examples.java' class for some examples
    how to use the various algorithms.
  ~ Also see the API docs in the directory 'doc/api/'
  ~ When executing the CompGeom JAR file (java -jar CompGeom-x.y.z.jar), the
    DemosLauncher class is executed which prompts a graphical dialogue window
    that let's you choose a (graphical) demo to run.

+-----------------------------------------------------------------------------+
| Supported
+-----------+
  ~ Rational used for exact calculations
  ~ Geometric primitives (point, line segment, line, polygon, rectangle)
  ~ Graham scan (convex hulls)
  ~ Rotating calipers to find the minimum bounding box of a convex hull
  ~ Closest pair of points
  ~ Shamos-Hoey algorithm
  ~ Bentley-Ottmann
  
+-----------------------------------------------------------------------------+
| Working on...
+---------------+
  ~ Voronoi diagram
  ~ Delaunay triangulation
  ~ Polygon triangulation
  