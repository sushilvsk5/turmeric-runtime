package org.ebayopensource.turmeric.runtime.error;

import static org.ebayopensource.turmeric.runtime.error.cassandra.handler.CassandraErrorLoggingHandler.KEY_SEPARATOR;
import static org.junit.Assert.*;

import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;
import org.junit.Before;
import org.junit.Test;

public class MetricValueTest {
    long now;
    ErrorValue value = null;
    @Before
    public void init() {
        now = System.currentTimeMillis();
        value = new ErrorValue(1234L, "theserver", "The error Message", "ServiceAdminName1", "TheOperation",
                        "Consumer1", now, true, 0);
    }

    @Test
    public void testGetKey() {
        String actual = value.getKey();
        String expected =  "1234|theserver|ServiceAdminName1|TheOperation|true";
        assertEquals(expected, actual);
    }
}
