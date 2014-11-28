StormCloud
==========

StormCloud is a standalone server for [Risk of Rain](http://riskofraingame.com/) designed from the ground up, to be customisable and stable.

Developing
----------

### Getting the code

Clone this repository with ```git``` first of all - the clone URL should be on the right hand side of this page.

### Setting up your environment

StormCloud is written using [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - so you'll need JDK 8 to work on it.
We recommend using [IntelliJ IDEA](https://www.jetbrains.com/idea/) to work on StormCloud (though of course Eclipse or NetBeans should work too)
Using IntelliJ IDEA, import the project from Maven and allow it to synchronize with the ```pom.xml``` automatically.
Create a Run Configuration, set the main class to ```StormCloud```, the classpath to your ```stormcloud``` module, and the rest should be set by default. You should now be set to run the server from within IntelliJ.
If you want to make IntelliJ build with Maven instead of the default compiler, you can switch the Make task for a Maven goal with the command line directive ```clean install```

Building
--------

StormCloud is currently build with [Maven](https://maven.apache.org/)
To build the StormCloud jar, use ```mvn clean install``` - this will create ```stormcloud-x.y.z.jar``` in the ```target``` directory.

Running
-------

As with any other command-line Java application, run StormCloud with:

```
java -Xms512M -Xmx1024M -jar stormcloud-x.y.z.jar
```

(Of course, you can change max/min stack size according to the amount of resources you have available and desired performance)

Contributing
------------

### Programmers

If you wish to help, first see if there is an issue you wish to solve. If so, fork the project and make a pull request including your changes. If not, raise a ticket with your issue or feature request.

### Technical writers

Open a pull request with any changes you wish to add to JavaDocs - these are much appreciated and help people both developing on the project itself, and third-party extensions.

### Server administrators

Open an issue with your feature request or bug report and we'll look at fixing/adding it