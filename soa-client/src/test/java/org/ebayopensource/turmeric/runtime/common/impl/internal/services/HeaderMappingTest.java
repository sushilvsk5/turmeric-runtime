package org.ebayopensource.turmeric.runtime.common.impl.internal.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceCreationException;
import org.ebayopensource.turmeric.runtime.common.impl.internal.config.NameValue;
import org.ebayopensource.turmeric.runtime.common.impl.internal.config.OptionList;
import org.ebayopensource.turmeric.runtime.common.impl.utils.HTTPCommonUtils;
import org.ebayopensource.turmeric.runtime.common.service.HeaderMappingsDesc;
import org.ebayopensource.turmeric.runtime.common.types.SOAHeaders;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests jira 1563, which describes a problem with loading a header
 * mapping.
 * 
 * @author dcarver
 * 
 */
public class HeaderMappingTest {

	@Test
	public void testSOAPActionHeaderMappingTest() throws Exception {
		DummyBaseDescFactory factory = new DummyBaseDescFactory("testFactory",
				false, true);
		OptionList options = new OptionList();
		List<NameValue> mappings = options.getOption();
		NameValue nv = new NameValue();
		nv.setName(SOAHeaders.SERVICE_OPERATION_NAME);
		nv.setValue("header[SOAPAction]");
		mappings.add(nv);
		HeaderMappingsDesc hmd = factory.loadHeaderMappings("ExampleService",
				options, true);
		assertTrue(hmd.getHeaderMap().containsKey("SOAPAction"));
	}

	@Test
	public void testRequestHeaderMapping() throws Exception {
		
		final String HEADER_VALUE = "dummyOperation";
		final String ORIGINAL_HEADER = SOAHeaders.SERVICE_OPERATION_NAME;
		final String NEW_HEADER = "SOAPAction";
		
		Map<String, String> headerMap = new HashMap<String, String>();
		Map<String, String> resultMap = new HashMap<String, String>();
		
		headerMap.put(NEW_HEADER, ORIGINAL_HEADER);
		resultMap.put(ORIGINAL_HEADER, HEADER_VALUE);

		HTTPCommonUtils.applyHeaderMap(headerMap, resultMap);
		
		assertTrue(!resultMap.containsKey(ORIGINAL_HEADER) && resultMap.get(NEW_HEADER).equals(HEADER_VALUE));
	}

	@Test
	public void invalidMappingValue() throws Exception {
		DummyBaseDescFactory factory = new DummyBaseDescFactory("testFactory",
				false, true);
		OptionList options = new OptionList();
		List<NameValue> mappings = options.getOption();
		NameValue nv = new NameValue();
		nv.setName(SOAHeaders.SERVICE_OPERATION_NAME);
		nv.setValue("SOAPAction");
		mappings.add(nv);
		try {
			factory.loadHeaderMappings("ExampleService", options, true);
		} catch (ServiceCreationException e) {
			return;
		}
		fail("Invalid Header mapping succeeded.");
	}
}
