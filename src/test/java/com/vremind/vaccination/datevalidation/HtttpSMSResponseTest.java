/**
 * 
 */
package com.vremind.vaccination.datevalidation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sdoddi
 *
 */
public class HtttpSMSResponseTest {
	
	@Test
	public void testHttpSMSResponse()
	{
		String response = "status=0,ack1_12345678900";
		boolean sucessStatus = false;
		String status[] = response.split(",");
		if (status != null && status.length > 1)
		{
			if (status[0].indexOf("=") != -1)
			{
				sucessStatus = ("0".equals(status[0].substring(status[0].indexOf("=")+1)) ? true : false);
			}
		}
		Assert.assertEquals(true, sucessStatus);
		if (sucessStatus)
		{
			String ackId = status[1];
			Assert.assertEquals("ack1_12345678900", ackId);
		}
	}

	@Test
	public void testSMSMessageFormat() throws URISyntaxException
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("receipientno","9000722060");
		params.put("msgtxt","Your kid Navya, 02-12-14 is registered for vaccination reminders @ www.vRemind.org. We care, We remind.");
		params.put("state","4");
		params.put("dcs", "0");
		
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("api.mVaayoo.com").setPath("/mvaayooapi/MessageCompose")
		.setParameter("user", "vremind.india@gmail.com:012987")
		.setParameter("senderID", "TEST SMS");
		Iterator<String> it = params.keySet().iterator();
		while (it.hasNext())
		{
			String key = it.next();
			String value = params.get(key);
			builder.setParameter(key, value);
		}
		URI uri = builder.build();
		System.out.println(uri);
		
	}
	
}
