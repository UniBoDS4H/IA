# DS4H Image Alignment

> "Data Science for Health (DS4H) Image Alignment" is a user-friendly tool freely provided as an ImageJ/Fiji plugin.
With DS4H Image Alignment, 2D images can be easily aligned (i.e. registered) by defining with a few clicks some well visible reference marks, or by using automatic routines.



## Implementation

DS4H Image Alignment has been implemented in Java 8 as a plugin for ImageJ/Fiji. This plugin can run on MacOs, Windows and Linux.



## How to Align
In order to align the images first of all we have to *load the images* inside the Java Application. This operation can be done in two ways:
1. By using the *File -> Load Images*, with the use of a file chooser you will be able to load 1 or more images. Be carefull if there are no images inside the project and you try to load a single Image you will get an error, but if you do the same operation but there are other images inside the project you will be able to load the choosed image.
2. By using the *Project -> Import*


### Manual Alignment
The manual alignment algorithm used inside this plugin are three: 
   - **Translational Alignment**
   - **Perspective Alignment**
   - **Affine Alignment**


### Automatic Alignment
The automatic alignment use what is called a *Point Detector*. A *Point Detector* is a class used for the detection of points for the alignment of the images. The Point Detector used inside this project are:
* **ORB**
* **SIFT**
* **SURF** 



## Installation

DS4H Image Alignment can be installed easily, on Windows, Linux and Apple Silicon M1 and Intel Based Mac (Mac-ImageJ is the ARM version, Mac-Fiji is the x86 Intel version). In order to use our tool, you can just download the version that you need for your pc, copy the jar 
inside the "plugins" folder of ImageJ/Fiji. You will find in the ImageJ/Fiji _Plugin_ voice out tool, **DS4H Image Alignment**


## How to use It
TO BE DONE

## Acknowledgments
We thanks all the University Students that helped in this project. In particular:

   * Fabio Vincenzi, Bachelor's Degree Student in Engineering and Computer Sciences, Alma Mater Studiorum University of Bologna, Italy

      email : fabio.vincenzi2001@gmail.com, 

      Linkedin : https://www.linkedin.com/in/fabio-vincenzi-b53a5a1a1/
   * Matteo Iorio, Bachelor's Degree Student in Engineering and Computer Sciences, Alma Mater Studiorum University of Bologna, Italy

      email : matteo.iorio01@gmail.com, 

      Linkedin : https://www.linkedin.com/in/matteo-iorio-1257621b4/


## Contact Us
The Data Science for Health (DS4H) group:

   * Antonella Carbonaro, Department of Computer Science and Engineering (DISI), University of Bologna, Bologna, Italy, 
   
      email: antonella.carbonaro@unibo.it

   * Filippo Piccinini, Istituto Scientifico Romagnolo per lo Studio e la Cura dei Tumori (IRST) IRCCS, Meldola (FC), Italy, 
   
      email: filippo.piccinini@irst.emr.it















<!--

This is an example Maven project implementing an ImageJ 1.x plugin.

For an example Maven project implementing an **ImageJ2 command**, see:
    https://github.com/imagej/example-imagej2-command

It is intended as an ideal starting point to develop new ImageJ 1.x plugins
in an IDE of your choice. You can even collaborate with developers using a
different IDE than you.

* In [Eclipse](http://eclipse.org), for example, it is as simple as
  _File &#8250; Import... &#8250; Existing Maven Project_.

* In [NetBeans](http://netbeans.org), it is even simpler:
  _File &#8250; Open Project_.

* The same works in [IntelliJ](http://jetbrains.net).

* If [jEdit](http://jedit.org) is your preferred IDE, you will need the
  [Maven Plugin](http://plugins.jedit.org/plugins/?MavenPlugin).

Die-hard command-line developers can use Maven directly by calling `mvn`
in the project root.

However you build the project, in the end you will have the `.jar` file
(called *artifact* in Maven speak) in the `target/` subdirectory.

To copy the artifact into the correct place, you can call
`mvn -Dscijava.app.directory=/path/to/ImageJ.app/`.
This will not only copy your artifact, but also all the dependencies. Restart
your ImageJ or call *Help &#8250; Refresh Menus* to see your plugin in the menus.

Developing plugins in an IDE is convenient, especially for debugging. To
that end, the plugin contains a `main` method which sets the `plugins.dir`
system property (so that the plugin is added to the Plugins menu), starts
ImageJ, loads an image and runs the plugin. See also
[this page](https://imagej.net/Debugging#Debugging_plugins_in_an_IDE_.28Netbeans.2C_IntelliJ.2C_Eclipse.2C_etc.29)
for information how ImageJ makes it easier to debug in IDEs.

Since this project is intended as a starting point for your own
developments, it is in the public domain.

How to use this project as a starting point
===========================================

1. Visit [this link](https://github.com/imagej/example-legacy-plugin/generate)
   to create a new repository in your space using this one as a template.

2. [Clone your new repository](https://help.github.com/en/articles/cloning-a-repository).

3. Edit the `pom.xml` file. Every entry should be pretty self-explanatory.
   In particular, change
    1. the *artifactId* (**NOTE**: should contain a '_' character)
    2. the *groupId*, ideally to a reverse domain name your organization owns
    3. the *version* (note that you typically want to use a version number
       ending in *-SNAPSHOT* to mark it as a work in progress rather than a
       final version)
    4. the *dependencies* (read how to specify the correct
       *groupId/artifactId/version* triplet
       [here](https://imagej.net/Maven#How_to_find_a_dependency.27s_groupId.2FartifactId.2Fversion_.28GAV.29.3F))
    5. the *developer* information
    6. the *scm* information

4. Remove the `Process_Pixels.java` file and add your own `.java` files
   to `src/main/java/<package>/` (if you need supporting files -- like icons
   -- in the resulting `.jar` file, put them into `src/main/resources/`)

5. Edit `src/main/resources/plugins.config`

6. Replace the contents of `README.md` with information about your project.

7. Make your initial
   [commit](https://help.github.com/en/desktop/contributing-to-projects/committing-and-reviewing-changes-to-your-project) and
   [push the results](https://help.github.com/en/articles/pushing-commits-to-a-remote-repository)!

### Eclipse: To ensure that Maven copies the plugin to your ImageJ folder

1. Go to _Run Configurations..._
2. Choose _Maven Build_
3. Add the following parameter:
    - name: `scijava.app.directory`
    - value: `/path/to/ImageJ.app/`

This ensures that the final `.jar` file will also be copied to
your ImageJ plugins folder everytime you run the Maven build.
-->
