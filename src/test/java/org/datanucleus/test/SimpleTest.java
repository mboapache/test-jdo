package org.datanucleus.test;

import org.junit.*;
import javax.jdo.*;

import static org.junit.Assert.*;
import mydomain.model.*;
import org.datanucleus.util.NucleusLogger;

public class SimpleTest
{
    @Test
    public void testSimple()
    {
        NucleusLogger.GENERAL.info(">> test START");
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("MyTest");

        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            Object oid;
            int nrOfDbCalls = PointToStringConverter.getNrOfConvertToDatastoreCalls();
            int nrOfAttrCalls = PointToStringConverter.getNrOfConvertToAttributeCalls();

            // create a PCReczt instance and store it
            tx.begin();
            PCRect rect = new PCRect();
            rect.setLowerRight(new Point(1,1));
            rect.setUpperLeft(new Point(2,2));
            pm.makePersistent(rect);
            oid = JDOHelper.getObjectId(rect);
            tx.commit();

            // convertToDatastore should be called twice
            assertEquals(2, PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
            // convertToAttribute should not be called
            assertEquals(0, PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);
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

        pmf.close();
        NucleusLogger.GENERAL.info(">> test END");
    }
}
