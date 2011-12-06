package org.ebayopensource.turmeric.runtime.common.impl.internal.services;

import static org.junit.Assert.assertTrue;

import org.ebayopensource.turmeric.runtime.common.impl.utils.HTTPCommonUtils;
import org.ebayopensource.turmeric.runtime.common.types.Cookie;
import org.junit.Test;

/**
 * This class tests jira 1643, which describes a problem with encoding request cookie values.
 * 
 * @author sushil_vsk5
 * 
 */
public class CookieEncodingTest {	

	@Test
	public void testVersion0CookieEncoding() throws Exception {
		
		Cookie firstCookie = new Cookie("FIRST_COOKIE", "foo");
		Cookie secondCookie = new Cookie("SECOND_COOKIE", "bar");
		
		StringBuffer encodedCookieString = new StringBuffer();		

		HTTPCommonUtils.encodeCookieValue(encodedCookieString, new Cookie[]{firstCookie, secondCookie});
				
		assertTrue(encodedCookieString.toString().equals("$Version=0; FIRST_COOKIE=foo; SECOND_COOKIE=bar"));
	}
	
}
