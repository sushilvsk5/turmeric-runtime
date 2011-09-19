/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.error;

import static org.junit.Assert.assertEquals;

import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorById;
import org.ebayopensource.turmeric.runtime.error.cassandra.model.ErrorValue;
import org.junit.Before;
import org.junit.Test;

public class MetricValueTest {
    long now;
    ErrorValue value = null;

    @Before
    public void init() {
        now = System.currentTimeMillis();
        ErrorById error = new ErrorById();
        value = new ErrorValue(error, "theserver", "The error Message", "ServiceAdminName1", "TheOperation",
                        "Consumer1", now, true, 0, 17);
    }

    @Test
    public void testGetKey() {
        String actual = value.getKey();
        String expected = now + "|17";
        assertEquals(expected, actual);
    }
}
