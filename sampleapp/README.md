```
============================================
  _____         __  __         _      _
 |_   _| _ _  _|  \/  |___  __| |_  _| |___
   | || '_| || | |\/| / _ \/ _` | || | / -_)
   |_||_|  \_,_|_|  |_\___/\__,_|\_,_|_\___|

			     Example
============================================
```
Introduction
------------
This project provides an example implementation of using TruModule in Java.

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

The library uses a library of Data Transformation (DTO) classes generated from the TruRating XSD schema files using the JAXB XJC utility and a precompiled JAR of a simploe TruModule implementation. These are supplied as binaries but will but be deilvered via Maven in future release
