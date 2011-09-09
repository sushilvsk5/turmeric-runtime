package org.ebayopensource.turmeric.runtime.error.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.ebayopensource.turmeric.runtime.error.utils.MockInitContext;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CassandraErrorLoggingHandlerTestIT extends CassandraTestHelper {
    private static final Long ERROR_ID = Long.valueOf(0);
    private static final Serializer<String> STR_SERIALIZER = StringSerializer.get();
    CassandraErrorLoggingHandler logHandler = null;
    Keyspace kspace = null;
    List<CommonErrorData> errorsToStore = null;
    String serverName = "localhost";
    String srvcAdminName = "ServiceAdminName1";
    String opName = "Operation1";
    boolean serverSide = true;
    String consumerName = "ConsumerName1";
    long now = System.currentTimeMillis();
    Map<String, String> options = null;
    InitContext ctx = null;

    @Before
    public void setUp() {
        errorsToStore = createTestCommonErrorDataList(1);
        options = createOptionsMap();
        ctx = new MockInitContext(options);
        try {
            logHandler = new CassandraErrorLoggingHandler();
            kspace = new HectorManager().getKeyspace("Test Cluster", IP_ADDRESS, "TurmericMonitoring", "Errors");
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @After
    public void tearDown() {
        cleanUpTestData();
        logHandler = null;
        kspace = null;
    }

    private void cleanUpTestData() {
        String[] columnFamilies = { "ErrorCountsByCategory", "ErrorCountsBySeverity", "Errors", "ErrorValues" };

        for (String cf : columnFamilies) {
            RangeSlicesQuery<String, String, String> rq = HFactory.createRangeSlicesQuery(kspace, STR_SERIALIZER,
                            STR_SERIALIZER, STR_SERIALIZER);
            rq.setColumnFamily(cf);
            rq.setRange("", "", false, 1000);
            QueryResult<OrderedRows<String, String, String>> qr = rq.execute();
            OrderedRows<String, String, String> orderedRows = qr.get();
            Mutator<String> deleteMutator = HFactory.createMutator(kspace, STR_SERIALIZER);
            for (Row<String, String, String> row : orderedRows) {
                deleteMutator.delete(row.getKey(), cf, null, STR_SERIALIZER);
            }
        }

    }

    @Test
    public void testInit() throws ServiceException {
        Map<String, String> options = createOptionsMap();
        InitContext ctx = new MockInitContext(options);
        logHandler.init(ctx);
        assertEquals("Test Cluster", logHandler.getClusterName());
        assertEquals(IP_ADDRESS, logHandler.getHostAddress());
        assertEquals("TurmericMonitoring", logHandler.getKeyspaceName());
    }

    public Map<String, String> createOptionsMap() {
        Map<String, String> options = new HashMap<String, String>();
        options.put("cluster-name", "Test Cluster");
        options.put("host-address", IP_ADDRESS);
        options.put("keyspace-name", "TurmericMonitoring");
        return options;
    }

    @Test
    public void testPersistErrorsCF() throws ServiceException {

        logHandler.init(ctx);
        logHandler.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName, now);

        // now I need to retrieve the values. I use Hector for this.
        ColumnSlice<Object, Object> errorColumnSlice = getColumnValues(kspace, "Errors", ERROR_ID,
                        StringSerializer.get(), StringSerializer.get(), "name", "category", "severity", "domain",
                        "subDomain", "organization");
        assertValues(errorColumnSlice, "name", "TestErrorName", "organization", "TestOrganization", "domain",
                        "TestDomain", "subDomain", "TestSubdomain", "severity", "ERROR", "category", "APPLICATION");

        ColumnSlice<Object, Object> longColumnSlice = getColumnValues(kspace, "Errors", ERROR_ID,
                        StringSerializer.get(), LongSerializer.get(), "errorId");
        assertValues(longColumnSlice, "errorId", ERROR_ID);

    }

    @Test
    public void testPersistErrorCountsByCategoryCF() throws ServiceException {

        logHandler.init(ctx);
        logHandler.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName, now);

        ColumnSlice<Object, Object> categoryCountColumnSlice = getColumnValues(kspace, "ErrorCountsByCategory",
                        "localhost|ServiceAdminName1|ConsumerName1|Operation1|APPLICATION|true", LongSerializer.get(),
                        StringSerializer.get(), Long.valueOf(now));
        assertValues(categoryCountColumnSlice, now, "0|localhost|ServiceAdminName1|Operation1|true");

        // now, assert the count cf | all ops
        ColumnSlice<Object, Object> categoryCountAllOpsColumnSlice = getColumnValues(kspace, "ErrorCountsByCategory",
                        "localhost|ServiceAdminName1|ConsumerName1|All|APPLICATION|true", LongSerializer.get(),
                        StringSerializer.get(), Long.valueOf(now));
        assertValues(categoryCountAllOpsColumnSlice, now, "0|localhost|ServiceAdminName1|Operation1|true");

    }
    
    @Test
    public void testPersistErrorCountsBySeverityCF() throws ServiceException {

        long now = System.currentTimeMillis();
        Map<String, String> options = createOptionsMap();
        InitContext ctx = new MockInitContext(options);
        logHandler.init(ctx);
        logHandler.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName, now);

        
        // now, assert the count cf | then the severity one
        ColumnSlice<Object, Object> severityCountColumnSlice = getColumnValues(kspace, "ErrorCountsBySeverity",
                        "localhost|ServiceAdminName1|ConsumerName1|Operation1|ERROR|true", LongSerializer.get(),
                        StringSerializer.get(), Long.valueOf(now));
        assertValues(severityCountColumnSlice, now, "0|localhost|ServiceAdminName1|Operation1|true");

        // now, assert the count cf | all ops
        ColumnSlice<Object, Object> severityCountAllOpsColumnSlice = getColumnValues(kspace, "ErrorCountsBySeverity",
                        "localhost|ServiceAdminName1|ConsumerName1|All|ERROR|true", LongSerializer.get(),
                        StringSerializer.get(), Long.valueOf(now));
        assertValues(severityCountAllOpsColumnSlice, now, "0|localhost|ServiceAdminName1|Operation1|true");

    }

}
