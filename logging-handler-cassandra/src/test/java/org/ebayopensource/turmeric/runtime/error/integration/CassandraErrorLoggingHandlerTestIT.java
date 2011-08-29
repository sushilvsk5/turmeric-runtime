package org.ebayopensource.turmeric.runtime.error.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.ebayopensource.turmeric.runtime.error.utils.MockInitContext;
import org.ebayopensource.turmeric.utils.cassandra.hector.HectorManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CassandraErrorLoggingHandlerTestIT extends CassandraTestHelper{
    CassandraErrorLoggingHandler logHandler = null;
    Keyspace kspace = null;

    @Before
    public void setUp(){
        
        try {
            logHandler = new CassandraErrorLoggingHandler();
            kspace = new HectorManager().getKeyspace("Test Cluster", "127.0.0.1", "TurmericMonitoring", "Errors");
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @After
    public void tearDown(){
        logHandler = null;
        kspace = null;
    }

    @Test
    public void testInit() throws ServiceException {
        Map<String, String> options = createOptionsMap();
        InitContext ctx = new MockInitContext(options);
        logHandler.init(ctx);
        assertEquals("Test Cluster", logHandler.getClusterName());
        assertEquals("127.0.0.1", logHandler.getHostAddress());
        assertEquals("TurmericMonitoring", logHandler.getKeyspaceName());
    }

    public Map<String, String> createOptionsMap() {
        Map<String, String> options = new HashMap<String, String>();
        options.put("cluster-name", "Test Cluster");
        options.put("host-address", "127.0.0.1");
        options.put("keyspace-name", "TurmericMonitoring");
        return options;
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
        Map<String, String> options = createOptionsMap();
        InitContext ctx = new MockInitContext(options);
        logHandler.init(ctx);
        logHandler.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName, now);

        // now I need to retrieve the values. I use Hector for this.
        ColumnSlice<Object, Object> errorColumnSlice = getColumnValues(kspace, "Errors", new Long(0), StringSerializer.get(),
                        StringSerializer.get(), "name", "category", "severity", "domain", "subDomain", "organization");
        assertValues(errorColumnSlice, "name", "TestErrorName", "organization", "TestOrganization", "domain",
                        "TestDomain", "subDomain", "TestSubdomain", "severity", "ERROR", "category", "APPLICATION");

        ColumnSlice<Object, Object> longColumnSlice = getColumnValues(kspace, "Errors", new Long(0), StringSerializer.get(),
                        LongSerializer.get(), "errorId");
        assertValues(longColumnSlice, "errorId", new Long(0));

        // now, assert the count cf - first the category one
        ColumnSlice<Object, Object> categoryCountColumnSlice = getColumnValues(kspace, "ErrorCountsByCategory",
                        "localhost-ServiceAdminName1-Operation1-APPLICATION", LongSerializer.get(),
                        StringSerializer.get(), new Long(now));
        assertValues(categoryCountColumnSlice, now, "0-localhost-ServiceAdminName1-Operation1");

        // now, assert the count cf - all ops
        ColumnSlice<Object, Object> categoryCountAllOpsColumnSlice = getColumnValues(kspace, "ErrorCountsByCategory",
                        "localhost-ServiceAdminName1-All-APPLICATION", LongSerializer.get(), StringSerializer.get(),
                        new Long(now));
        assertValues(categoryCountAllOpsColumnSlice, now, "0-localhost-ServiceAdminName1-Operation1");

        // now, assert the count cf - then the severity one
        ColumnSlice<Object, Object> severityCountColumnSlice = getColumnValues(kspace, "ErrorCountsBySeverity",
                        "localhost-ServiceAdminName1-Operation1-ERROR", LongSerializer.get(), StringSerializer.get(),
                        new Long(now));
        assertValues(severityCountColumnSlice, now, "0-localhost-ServiceAdminName1-Operation1");

        // now, assert the count cf - all ops
        ColumnSlice<Object, Object> severityCountAllOpsColumnSlice = getColumnValues(kspace, "ErrorCountsBySeverity",
                        "localhost-ServiceAdminName1-All-ERROR", LongSerializer.get(), StringSerializer.get(),
                        new Long(now));
        assertValues(severityCountAllOpsColumnSlice, now, "0-localhost-ServiceAdminName1-Operation1");

    }

}
