package org.ebayopensource.turmeric.runtime.error.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.common.v1.types.ErrorCategory;
import org.ebayopensource.turmeric.common.v1.types.ErrorSeverity;
import org.ebayopensource.turmeric.utils.cassandra.HectorManager;

public class BaseIntegration {

    public void assertValues(ColumnSlice<Object, Object> columnSlice, Object... columnPairs) {
    
        // the asserts are done in this way: assert(columnPairs[0], columnPairs[1]);, assert(columnPairs[2],
        // columnPairs[3]), ...;
        for (int i = 0; i < columnPairs.length / 2; i++) {
            HColumn<Object, Object> column = columnSlice.getColumnByName(columnPairs[2 * i]);
            assertNotNull("Null column name =" + columnPairs[2 * i], column);
            Object value = column.getValue();
            assertEquals("Expected = " + columnPairs[2 * i + 1] + ". Actual = " + value, columnPairs[2 * i + 1], value);
        }
    }

    public ColumnSlice<Object, Object> getColumnValues(String cfName, Object key, Serializer columnNameSerializer, Serializer valueSerializer, Object... columnNames) {
        Keyspace kspace = HectorManager.getKeyspace("Test Cluster", "192.168.2.41", "TurmericMonitoring");
        SliceQuery<Object, Object, Object> q = HFactory.createSliceQuery(kspace,
                        SerializerTypeInferer.getSerializer(key), columnNameSerializer, valueSerializer);
        q.setColumnFamily(cfName);
        q.setKey(key);
        q.setColumnNames(columnNames);
        QueryResult<ColumnSlice<Object, Object>> r = q.execute();
        ColumnSlice<Object, Object> columnSlice = r.get();
        return columnSlice;
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
            e.setErrorId(new Long(i));
            e.setMessage("Error Message " + i);
            e.setOrganization("TestOrganization");
            commonErrorDataList.add(e);
        }
        return commonErrorDataList;
    
    }

}
