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
