# About

This originally has been a Java applet that implements a visualization of [Fortune][]’s plane-sweep algorithm for creating a [voronoi diagram][].
You can view a [live demo][applet] of this original applet.
The applet was created by Benny Kjær Nielsen and Allan Odgaard in spring of 2000 following a course in Computational Geometry taught by Pawel Winter at [DIKU][].

The source code has then been reworked, refactored and improved by Sebastian Kürten.
It now works as a standalone Swing application rather than an applet.
Also there is a port via GWT to Javascript.

# License

The original authors state the following:

> Permission to copy, use, modify, sell and distribute this software is granted. This software is provided “as is” without express or implied warranty, and with no claim as to its suitability for any purpose.

I release this modified version of the software as a whole under the terms of the **GNU General Public License Version 3** **(GNU GPLv3)**.

# Building and Running

To build the project, use ant:

> `ant compile`

To run the Swing application, run the following:

> `java -cp bin:lib/*:lib/batik/*:. fortune.sweep.gui.swing.SwingFortune`

Or alternatively:

> `ant dist`

> `java -cp dist/fortune.jar fortune.sweep.gui.swing.SwingFortune`

# Notes

The purpose of the source is to visualize the algorithm, it is not a good base for an efficient implementation of the algorithm (it does not run in O(n log n) time).

The original source was initially lost and recovered using a Java decompiler so most variable names are nonsensical.
After extensive rework, for most of the code this is not true anymore since the code has been heavily refactored, modified
and commented.

[Fortune]: http://ect.bell-labs.com/who/sjf/ "Steven Fortune"
[voronoi diagram]: http://en.wikipedia.org/wiki/Voronoi_diagram "Wikipedia Entry: Voronoi diagram"
[applet]: http://www.diku.dk/hjemmesider/studerende/duff/Fortune/ "Visualization of plane-sweep algorithm for voronoi diagrams"
[DIKU]: http://www.diku.dk/ "Department of Computer Science, University of Copenhagen"
