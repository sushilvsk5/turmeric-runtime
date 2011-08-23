package org.ebayopensource.turmeric.runtime.error.integration;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.TypeInferringSerializer;
import me.prettyprint.hector.api.beans.ColumnSlice;

import org.ebayopensource.turmeric.common.v1.types.CommonErrorData;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;
import org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler;
import org.ebayopensource.turmeric.runtime.error.utils.MockInitContext;
import org.ebayopensource.turmeric.runtime.error.utils.MockMessageContextImpl;
import org.ebayopensource.turmeric.runtime.sif.impl.internal.pipeline.ReducedClientMessageContextImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CassandraErrorLoggingHandlerTestIT extends BaseIntegration{
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
    public void testInit() throws ServiceException {
        Map<String, String> options = new HashMap<String, String>();
        options.put("cluster-name", "Test Cluster");
        options.put("host-address", "localhost");
        options.put("keyspace-name", "TurmericMonitoring");
        InitContext ctx = new MockInitContext(options);
        logHandler.init(ctx);
        assertEquals("Test Cluster", logHandler.getClusterName());
        assertEquals("localhost", logHandler.getHostAddress());
        assertEquals("TurmericMonitoring", logHandler.getKeyspaceName());
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
        // init the logHandler with the params
        Map<String, String> options = new HashMap<String, String>();
        options.put("cluster-name", "Test Cluster");
        options.put("host-address", "localhost");
        options.put("keyspace-name", "TurmericMonitoring");
        InitContext ctx = new MockInitContext(options);
        logHandler.init(ctx);
        logHandler.persistErrors(errorsToStore, serverName, srvcAdminName, opName, serverSide, consumerName, now);

        // now I need to retrieve the values. I use Hector for this.
        ColumnSlice<Object, Object> errorColumnSlice = getColumnValues("Errors", new Long(0), StringSerializer.get(),
                        StringSerializer.get(), "name", "category", "severity", "domain", "subDomain", "organization");
        assertValues(errorColumnSlice, "name", "TestErrorName", "organization", "TestOrganization", "domain",
                        "TestDomain", "subDomain", "TestSubdomain", "severity", "ERROR", "category", "APPLICATION");

        ColumnSlice<Object, Object> longColumnSlice = getColumnValues("Errors", new Long(0), StringSerializer.get(),
                        LongSerializer.get(), "errorId");
        assertValues(longColumnSlice, "errorId", new Long(0));

        // now, assert the count cf - first the category one
        ColumnSlice<Object, Object> categoryCountColumnSlice = getColumnValues("ErrorCountsByCategory",
                        "localhost-ServiceAdminName1-Operation1-APPLICATION", LongSerializer.get(),
                        StringSerializer.get(), new Long(now));
        assertValues(categoryCountColumnSlice, now, "0-localhost-ServiceAdminName1-Operation1");

        // now, assert the count cf - all ops
        ColumnSlice<Object, Object> categoryCountAllOpsColumnSlice = getColumnValues("ErrorCountsByCategory",
                        "localhost-ServiceAdminName1-All-APPLICATION", LongSerializer.get(), StringSerializer.get(),
                        new Long(now));
        assertValues(categoryCountAllOpsColumnSlice, now, "0-localhost-ServiceAdminName1-Operation1");

        // now, assert the count cf - then the severity one
        ColumnSlice<Object, Object> severityCountColumnSlice = getColumnValues("ErrorCountsBySeverity",
                        "localhost-ServiceAdminName1-Operation1-ERROR", LongSerializer.get(), StringSerializer.get(),
                        new Long(now));
        assertValues(severityCountColumnSlice, now, "0-localhost-ServiceAdminName1-Operation1");

        // now, assert the count cf - all ops
        ColumnSlice<Object, Object> severityCountAllOpsColumnSlice = getColumnValues("ErrorCountsBySeverity",
                        "localhost-ServiceAdminName1-All-ERROR", LongSerializer.get(), StringSerializer.get(),
                        new Long(now));
        assertValues(severityCountAllOpsColumnSlice, now, "0-localhost-ServiceAdminName1-Operation1");

    }

}
