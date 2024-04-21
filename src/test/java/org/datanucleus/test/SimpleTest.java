package org.datanucleus.test;

import java.util.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.*;
import javax.jdo.*;

import static org.junit.Assert.*;
import mydomain.model.*;
import org.datanucleus.util.NucleusLogger;

public class SimpleTest
{
    private static final int NR_OF_INSTANCES = 5000;

    private static final String SSJDOQL =
            "select max (this.x) from mydomain.model.Point "
          + "where this.x >= 0 && p2.x >= 0 "
          + "variables mydomain.model.Point p2 "  ;

    @Test
    public void testDerby()
    {
        NucleusLogger.GENERAL.info("");
        NucleusLogger.GENERAL.info(">> test derby START");
        PersistenceManagerFactory pmf = getDerbyPMF();
        insertData(pmf);
        for (int i = 0; i < 3; i++) {
            runTest(pmf, true);
        }
        runTest(pmf, false);
        pmf.close();
        NucleusLogger.GENERAL.info(">> test derby END");
    }

    @Test
    public void testH2()
    {
        NucleusLogger.GENERAL.info("");
        NucleusLogger.GENERAL.info(">> test h2 START");
        PersistenceManagerFactory pmf = getH2PMF();
        insertData(pmf);
        for (int i = 0; i < 3; i++) {
            runTest(pmf, true);
        }
        runTest(pmf, false);
        pmf.close();
        NucleusLogger.GENERAL.info(">> test h2 END");
    }

    private void runTest(PersistenceManagerFactory pmf, boolean warmup) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            Query<Point> query = pm.newQuery(SSJDOQL);
            if (!warmup)
            {
                Configurator.setLevel("DataNucleus.Datastore.Retrieve", Level.DEBUG);
                Configurator.setLevel("DataNucleus.Datastore.Native", Level.DEBUG);
                Configurator.setLevel("DataNucleus.Query", Level.DEBUG);
            }
            query.compile();
            Object result = query.execute();
            if (!warmup) {
                System.out.println("Query result: " + result);
                Configurator.setLevel("DataNucleus.Datastore.Retrieve", Level.INFO);
                Configurator.setLevel("DataNucleus.Datastore.Native", Level.INFO);
                Configurator.setLevel("DataNucleus.Query", Level.INFO);
            }
            tx.commit();
        }
        catch (Throwable thr)
        {
            NucleusLogger.GENERAL.error(">> Exception in test", thr);
            fail("Failed test : " + thr.getMessage());
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
    }

    private PersistenceManagerFactory getH2PMF() {
        Map<String, String> overrides = new HashMap();
        overrides.put(Constants.PROPERTY_CONNECTION_URL, "jdbc:h2:mem:nucleus;INIT=CREATE SCHEMA IF NOT EXISTS nuclues");
        overrides.put(Constants.PROPERTY_CONNECTION_USER_NAME, "sa");
        overrides.put(Constants.PROPERTY_CONNECTION_PASSWORD, "");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(overrides, "MyTest");
        return pmf;
    }

    private PersistenceManagerFactory getDerbyPMF() {
        Map<String, String> overrides = new HashMap();
        overrides.put(Constants.PROPERTY_CONNECTION_DRIVER_NAME, "org.apache.derby.jdbc.EmbeddedDriver");
        overrides.put(Constants.PROPERTY_CONNECTION_URL, "jdbc:derby:target/nucleus;create=true");
        overrides.put(Constants.PROPERTY_CONNECTION_USER_NAME, "user");
        overrides.put(Constants.PROPERTY_CONNECTION_PASSWORD, "pwd");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(overrides, "MyTest");
        return pmf;
    }

    private void insertData(PersistenceManagerFactory pmf) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < NR_OF_INSTANCES; i++) {
                Point p = new Point(i, i, i);
                pm.makePersistent(p);
            }
            tx.commit();
        } catch (Throwable ex) {
            NucleusLogger.GENERAL.error(">> Exception in test", ex);
            fail("Failed test : " + ex.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }
}
