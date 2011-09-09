/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.runtime.error.utils;

import java.util.Map;

import org.ebayopensource.turmeric.runtime.common.pipeline.LoggingHandler.InitContext;
import org.ebayopensource.turmeric.runtime.common.service.ServiceId;

public class MockInitContext implements InitContext{
    Map<String, String> options = null;
    
    public MockInitContext(Map<String, String> opts){
        options = opts;
    }

    @Override
    public ServiceId getServiceId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String> getOptions() {
        return this.options;
    }

    @Override
    public void setSupportsErrorLogging() {
        // TODO Auto-generated method stub
        
    }

}
