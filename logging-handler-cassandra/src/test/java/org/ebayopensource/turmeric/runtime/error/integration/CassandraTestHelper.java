/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.error.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.utils.cassandra.service.CassandraManager;

public class CassandraTestHelper {

    protected static final String IP_ADDRESS = "127.0.0.1:9170";

    private static void cleanUpCassandraDirs() {
        if (CassandraManager.getEmbeddedService() == null) {
            System.out.println("Cleaning cassandra dirs ? = " + deleteDir(new File("target/cassandra")));
        }
    }

    // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }

        }
        // The directory is now empty so delete it
        return dir.delete();

    }

    public static void initialize() throws TTransportException, IOException, InterruptedException,
                    ConfigurationException {
        cleanUpCassandraDirs();
        loadConfig();
        CassandraManager.initialize();
    }

    /**
     * Load config.
     */
    private static void loadConfig() {
        // use particular test properties, maybe with copy method
        System.setProperty("log4j.configuration", "log4j.properties");
        System.setProperty("cassandra.config", "cassandra.yaml");
    }

    public void assertValues(ColumnSlice<Object, Object> columnSlice, Object... columnPairs) {

        // the asserts are done in this way: assert(columnPairs[0], columnPairs[1]);, assert(columnPairs[2],
        // columnPairs[3]), ...;
        for (int i = 0; i < (columnPairs.length / 2); i++) {
            HColumn<Object, Object> column = columnSlice.getColumnByName(columnPairs[2 * i]);
            assertNotNull("Null column name =" + columnPairs[2 * i], column);
            Object value = column.getValue();
            assertEquals("Expected [" + columnPairs[2 * i] + "]= " + columnPairs[(2 * i) + 1] + ". Actual = " + value,
                            columnPairs[(2 * i) + 1], value);
        }
    }

    public List<CommonErrorData> createTestCommonErrorDataList(int errorQuantity) {
        List<CommonErrorData> commonErrorDataList = new ArrayList<CommonErrorData>();
        for (int i = 0; i < errorQuantity; i++) {
            CommonErrorData e = new CommonErrorData();
            e.setCategory(ErrorCategory.APPLICATION);
            e.setSeverity(ErrorSeverity.ERROR);
            e.setCause("TestCause");
            e.setDomain("TestDomain");
            e.setSubdomain("TestSubdomain");
            e.setErrorName("TestErrorName");
            e.setErrorId(Long.valueOf(i));
            e.setMessage("Error Message " + i);
            e.setOrganization("TestOrganization");
            commonErrorDataList.add(e);
        }
        return commonErrorDataList;

    }

    public ColumnSlice<Object, Object> getColumnValues(Keyspace kspace, String cfName, Object key,
                    Serializer columnNameSerializer, Serializer valueSerializer, Object... columnNames) {

        SliceQuery<Object, Object, Object> q = HFactory.createSliceQuery(kspace,
                        SerializerTypeInferer.getSerializer(key), columnNameSerializer, valueSerializer);
        q.setColumnFamily(cfName);
        q.setKey(key);
        q.setColumnNames(columnNames);
        QueryResult<ColumnSlice<Object, Object>> r = q.execute();
        ColumnSlice<Object, Object> columnSlice = r.get();
        return columnSlice;
    }

}