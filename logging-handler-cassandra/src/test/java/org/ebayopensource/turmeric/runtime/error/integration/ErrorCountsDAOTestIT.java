/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.error.integration;

import static org.junit.Assert.*;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;

import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.runtime.error.cassandra.dao.ErrorCountsDAO;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ErrorCountsDAOTestIT extends CassandraTestHelper {

    private ErrorCountsDAO dao = null;
    private Keyspace kspace = null;
    private Long now = -1l;
    private ErrorById errorToSave = null;
    private ErrorValue errorValue = null;
    private int errorCountToStore;

    @Before
    public void setUp() {
        now = System.currentTimeMillis();
        errorToSave = new ErrorById();
        errorToSave.setCategory(ErrorCategory.REQUEST.toString());
        errorToSave.setSeverity(ErrorSeverity.ERROR.toString());
        errorToSave.setDomain("TestDomain");
        errorToSave.setErrorId(Long.valueOf(123));
        errorToSave.setName("TestError1");
        errorToSave.setOrganization("TestOrg1");
        errorToSave.setSubDomain("TestSubDomain");

        errorValue = new ErrorValue();
        errorValue.setErrorId(Long.valueOf(123));
        errorValue.setConsumerName("theTestConsumer");
        errorValue.setErrorMessage("The actual message");
        errorValue.setOperationName("Op1");
        errorValue.setServerName("TheServerName");
        errorValue.setServerSide(true);
        errorValue.setServiceAdminName("TheServiceAdminName");
        errorValue.setTimeStamp(now);
        errorCountToStore = 1;

        try {
            kspace = new HectorManager().getKeyspace("Test Cluster", IP_ADDRESS, "TurmericMonitoring", "Errors", false, null, String.class);
            dao = new ErrorCountsDAO("Test Cluster", IP_ADDRESS, "TurmericMonitoring");
        }
        catch (Exception e) {
            // TODO Auto|generated catch block
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        dao = null;
        kspace = null;
    }

    @Test
    public void testSaveErrorCounts() {

        String errorValueKey = "ErrorValueTestKey";
        dao.saveErrorCounts(errorToSave, errorValue, errorValueKey, now, errorCountToStore);

        // now, assert the count cf | first the category one
        ColumnSlice<Object, Object> categoryCountColumnSlice = getColumnValues(kspace, "ErrorCountsByCategory",
                        "TheServerName|TheServiceAdminName|theTestConsumer|Op1|REQUEST|true", LongSerializer.get(),
                        StringSerializer.get(), Long.valueOf(now));
        assertValues(categoryCountColumnSlice, now, "ErrorValueTestKey");

        // now, assert the count cf | all ops
        ColumnSlice<Object, Object> categoryCountAllOpsColumnSlice = getColumnValues(kspace, "ErrorCountsByCategory",
                        "TheServerName|TheServiceAdminName|theTestConsumer|All|REQUEST|true", LongSerializer.get(),
                        StringSerializer.get(), Long.valueOf(now));
        assertValues(categoryCountAllOpsColumnSlice, now, "ErrorValueTestKey");

        // now, assert the count cf | then the severity one
        ColumnSlice<Object, Object> severityCountColumnSlice = getColumnValues(kspace, "ErrorCountsBySeverity",
                        "TheServerName|TheServiceAdminName|theTestConsumer|Op1|ERROR|true", LongSerializer.get(),
                        StringSerializer.get(), Long.valueOf(now));
        assertValues(severityCountColumnSlice, now, "ErrorValueTestKey");

        // now, assert the count cf | all ops
        ColumnSlice<Object, Object> severityCountAllOpsColumnSlice = getColumnValues(kspace, "ErrorCountsBySeverity",
                        "TheServerName|TheServiceAdminName|theTestConsumer|All|ERROR|true", LongSerializer.get(),
                        StringSerializer.get(), Long.valueOf(now));
        assertValues(severityCountAllOpsColumnSlice, now, "ErrorValueTestKey");
    }

    @Test
    public void testCreateCategoryKeyByErrorValue() {
        String actual = dao.createCategoryKeyByErrorValue(errorValue, errorToSave);
        String expected = "TheServerName|TheServiceAdminName|theTestConsumer|Op1|REQUEST|true";
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateCategoryKeyByErrorValueForAllOps() {
        String actual = dao.createCategoryKeyByErrorValueForAllOps(errorValue, errorToSave);
        String expected = "TheServerName|TheServiceAdminName|theTestConsumer|All|REQUEST|true";
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateSeverityKeyByErrorValue() {
        String actual = dao.createSeverityKeyByErrorValue(errorValue, errorToSave);
        String expected = "TheServerName|TheServiceAdminName|theTestConsumer|Op1|ERROR|true";
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateSeverityKeyByErrorValueForAllOps() {
        String actual = dao.createSeverityKeyByErrorValueForAllOps(errorValue, errorToSave);
        String expected = "TheServerName|TheServiceAdminName|theTestConsumer|All|ERROR|true";
        assertEquals(expected, actual);
    }

}
