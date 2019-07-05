This folder is to overlay the main classes and allow downgrades to support JDK 1.6

This works in combination with profile jdk16 which excludes from the main source, 
java classes which don't compile in 1.6. 

This profile also adds the classifier jdk16 for its build

JDK1.6 drops jdk18 package and backports to older style for 1.6 
