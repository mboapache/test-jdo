#JDO Converter Test Case

##Summary
PM.makePersistent runs into an exception when using a PC class with Convert annotation instead of 
metadata in a .jdo file.

The setup in this branch shows the working test case using a JDO metadata file.


##Details

This test case shows a runtime issue with a persistence capable class having a persistent field with a converter 
annotation.

Model class PCRect is a persistence capable class with two fields of type Point. Point is a simple class 
with two int fields x and y. Both point fields are mapped to a string of the form "x:y" in the database 
using a converter class PointToStringConverter.

The test case SimpleTest creates a PCRect instance with two Point instances and commits the transaction. 
When using annotations in the field declaration of the PCRect class, then method PersistenceManager.makePersistent 
throws an  IllegalArgumentException (see stacktrace below). 

The exception does not occur when using JDO metadata in a .jdo file (package.jdo) instead of using annotations in 
the PCRect class.

The branches [converter-annotation](https://github.com/mboapache/test-jdo/tree/converter-annotation) and 
[converter-metadata](https://github.com/mboapache/test-jdo/tree/converter-metadata) contain the test case 
showing this behaviour. The branch converter-annotation shows the exception.

##Stacktrace

```  
java.lang.IllegalArgumentException: out of field index :1 
    at mydomain.model.PCRect.dnCopyField(PCRect.java)
    at mydomain.model.PCRect.dnCopyFields(PCRect.java)
    at org.datanucleus.state.StateManagerImpl.saveFields(StateManagerImpl.java:5862)
    at org.datanucleus.state.StateManagerImpl.initialiseForPersistentNew(StateManagerImpl.java:495)
    at org.datanucleus.state.StateManagerImpl.initialiseForPersistentNew(StateManagerImpl.java:126)
    at org.datanucleus.state.ObjectProviderFactoryImpl.newForPersistentNew(ObjectProviderFactoryImpl.java:205)
    at org.datanucleus.ExecutionContextImpl.persistObjectInternal(ExecutionContextImpl.java:2025)
    at org.datanucleus.ExecutionContextImpl.persistObjectWork(ExecutionContextImpl.java:1869)
    at org.datanucleus.ExecutionContextImpl.persistObject(ExecutionContextImpl.java:1724)
    at org.datanucleus.api.jdo.JDOPersistenceManager.jdoMakePersistent(JDOPersistenceManager.java:715)
    at org.datanucleus.api.jdo.JDOPersistenceManager.makePersistent(JDOPersistenceManager.java:740)
    at org.datanucleus.test.SimpleTest.testSimple(SimpleTest.java:32)
```

#test-jdo

Template project for any user testcase using JDO.
To create a DataNucleus test simply fork this project, and add/edit as 
necessary to add your model and persistence commands. The files that you'll likely need to edit are

* <a href="https://github.com/datanucleus/test-jdo/tree/master/src/main/java/mydomain/model">src/main/java/mydomain/model/</a>   **[Put your model classes here]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/main/resources/META-INF/persistence.xml">src/main/resources/META-INF/persistence.xml</a>   **[Put your datastore details in here]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/test/java/org/datanucleus/test/SimpleTest.java">src/test/java/org/datanucleus/test/SimpleTest.java</a>   **[Edit this if a single-thread test is required]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/test/java/org/datanucleus/test/MultithreadTest.java">src/test/java/org/datanucleus/test/MultithreadTest.java</a>   **[Edit this if a multi-thread test is required]**

To run this, simply type "mvn clean compile test"
