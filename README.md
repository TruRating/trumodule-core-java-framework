```
============================================
  _____         __  __         _      _     
 |_   _| _ _  _|  \/  |___  __| |_  _| |___ 
   | || '_| || | |\/| / _ \/ _` | || | / -_)
   |_||_|  \_,_|_|  |_\___/\__,_|\_,_|_\___|
                                            
============================================
```
Introduction
------------
This project provides an example implementation of TruModule in Java.  

A requirements specification for TruModule can be found on the TruRating document gateway at: 
https://docs.trurating.com/get-started/integrated-systems/specification/requirements/ 

License
-------
This project is made available under an MIT Open source license. 

Build
-----
The project can be opened in a Java IDE such as eclipse, using the maven project file pom.xml. A full set of unit tests is included which should be run as part of a maven build.

Notes
------	

This project builds to a java library that exemplifies how TruModule should interact with TruService. The library includes two main interfaces, with attached implementations: 

* com.trurating.trumodule.ITruModuleStandalone - supports typical behaviour in an environment which is not receiving messages from the POS system.
* com.trurating.trumodule.ITruModuleIntegrated - supports typical behaviour in an environment which is receiving POSEvent messages.

The library uses a library of Data Transformation (DTO) classes generated from the TruRating XSD schema files using the JAXB XJC utility. This is supplied as a binary but will but be deilvered via Maven in future release.


