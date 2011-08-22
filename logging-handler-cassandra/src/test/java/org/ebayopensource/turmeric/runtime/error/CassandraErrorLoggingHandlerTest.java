package org.ebayopensource.turmeric.runtime.error;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.SerializerTypeInferer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.TypeInferringSerializer;
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
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.ebayopensource.turmeric.utils.cassandra.HectorManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CassandraErrorLoggingHandlerTest {
    CassandraErrorLoggingHandler logHandler = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        logHandler = new CassandraErrorLoggingHandler();
    }

    @After
    public void tearDown() throws Exception {
        logHandler = null;
    }

    @Test
    public void testInit() {
        fail("Not yet implemented");
    }

    @Test
    public void testLogProcessingStage() {
        fail("Not yet implemented");
    }

    @Test
    public void testLogResponseResidentError() {
        fail("Not yet implemented");
    }

    @Test
    public void testPersistErrors() throws ServiceException {

        List<CommonErrorData> errorsToStore = createTestCommonErrorDataList(1);
        String serverName = "localhost";
        String srvcAdminName = "ServiceAdminName1";
        String opName = "Operation1";
        boolean serverSide = true;
        String consumerName = "ConsumerName1";
        long now = System.currentTimeMillis();
        logHandler.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName, now);
        
        //now I need to retrieve the values. I use Hector for this.
        ColumnSlice<Object, Object> errorColumnSlice = getColumnValues("Errors", new Long(0), StringSerializer.get(), StringSerializer.get(), "name", "category",
                        "severity", "domain", "subDomain", "organization");
        assertValues(errorColumnSlice, "name", "TestErrorName", "organization", "TestOrganization", "domain", "TestDomain", "subDomain", "TestSubdomain", "severity", "ERROR", "category", "APPLICATION");
        
        ColumnSlice<Object, Object> longColumnSlice = getColumnValues("Errors", new Long(0), StringSerializer.get(), LongSerializer.get(), "errorId");
        assertValues(longColumnSlice, "errorId", new Long(0));
        
        //now, assert the count cf - first the category one
        ColumnSlice<Object, Object> categoryCountColumnSlice = getColumnValues("ErrorCountsByCategory", "localhost-ServiceAdminName1-Operation1-APPLICATION", LongSerializer.get(), StringSerializer.get(), new Long(now));
        assertValues(categoryCountColumnSlice, now, "0-localhost-ServiceAdminName1-Operation1");
        
        
      //now, assert the count cf - then the severity one
        ColumnSlice<Object, Object> severityCountColumnSlice = getColumnValues("ErrorCountsBySeverity", "localhost-ServiceAdminName1-Operation1-ERROR", LongSerializer.get(), StringSerializer.get(), new Long(now));
        assertValues(severityCountColumnSlice, now, "0-localhost-ServiceAdminName1-Operation1");
        
    }

    public void assertValues(ColumnSlice<Object, Object> columnSlice, Object... columnPairs) {
        
        //the asserts are done in this way: assert(columnPairs[0], columnPairs[1]);, assert(columnPairs[2], columnPairs[3]), ...;
        for (int i = 0; i < columnPairs.length/2; i++) {
            HColumn<Object, Object> column = columnSlice.getColumnByName(columnPairs[2*i]);
            assertNotNull("Null column name ="+columnPairs[2*i], column);
            Object value = column.getValue();
            assertEquals("Expected = "+columnPairs[2*i +1]+". Actual = "+value,columnPairs[2*i +1], value);
        }
    }

    public ColumnSlice<Object, Object> getColumnValues(String cfName, Object key, Serializer columnNameSerializer, Serializer valueSerializer, Object... columnNames) {
        Keyspace kspace = HectorManager.getKeyspace("Test Cluster", "192.168.2.41", "TurmericMonitoring");
        SliceQuery<Object, Object, Object> q = HFactory.createSliceQuery(kspace, SerializerTypeInferer.getSerializer(key),
                        columnNameSerializer, valueSerializer);
        q.setColumnFamily(cfName);
        q.setKey(key);
        q.setColumnNames(columnNames);
        QueryResult<ColumnSlice<Object, Object>> r = q.execute();
        ColumnSlice<Object, Object> columnSlice = r.get();
        return columnSlice;
    }

    private List<CommonErrorData> createTestCommonErrorDataList(int errorQuantity) {
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

    @Test
    public void testLogWarning() {
        fail("Not yet implemented");
    }

}
