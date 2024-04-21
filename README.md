test-jdo
========

Template project for any user testcase using JDO.
To create a DataNucleus test simply fork this project, and add/edit as 
necessary to add your model and persistence commands. The files that you'll likely need to edit are

* <a href="https://github.com/datanucleus/test-jdo/tree/master/src/main/java/mydomain/model">src/main/java/mydomain/model/</a>   **[Put your model classes here]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/main/resources/META-INF/persistence.xml">src/main/resources/META-INF/persistence.xml</a>   **[Put your datastore details in here]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/test/java/org/datanucleus/test/SimpleTest.java">src/test/java/org/datanucleus/test/SimpleTest.java</a>   **[Edit this if a single-thread test is required]**
* <a href="https://github.com/datanucleus/test-jdo/blob/master/src/test/java/org/datanucleus/test/MultithreadTest.java">src/test/java/org/datanucleus/test/MultithreadTest.java</a>   **[Edit this if a multi-thread test is required]**

To run this, simply type "mvn clean compile test"

========

The class SimpleTest runs an aggregate JDOQL query and tries to figure how much time is spend in the RDBMS versus in the 
JDOQL query layer. The query is executed on derby and on H2. The datanuclues logging output in datanuclues.log shows 
unexpected query execution times when comparing the two RDBMS. 

With derby the SQL execution time is a few Millis and most of the time in spent in the JDOQL query layer:
14:52:54,997 (main) DEBUG [DataNucleus.Datastore.Native] - SELECT MAX(A0.X) FROM POINT A0 CROSS JOIN POINT VAR_P2 WHERE A0.X >= 0 AND VAR_P2.X >= 0
14:52:54,997 (main) DEBUG [DataNucleus.Datastore.Retrieve] - SQL Execution Time = 1 ms
14:53:02,086 (main) DEBUG [DataNucleus.Query] - JDOQL Query : Execution Time = 7087 ms

With H2 most of the  time is spent in the RDBMS:
14:53:12,505 (main) DEBUG [DataNucleus.Datastore.Native] - SELECT MAX(A0.X) FROM POINT A0 CROSS JOIN POINT VAR_P2 WHERE A0.X >= 0 AND VAR_P2.X >= 0
14:53:15,263 (main) DEBUG [DataNucleus.Datastore.Retrieve] - SQL Execution Time = 2757 ms
14:53:15,263 (main) DEBUG [DataNucleus.Query] - JDOQL Query : Execution Time = 2758 ms

Since the query returns a single non-persistent value I would expect that most of the time is spent in the RDBMS, 
such as in the case of H2. The performance numbers with deryb are somewhat unexpected.
