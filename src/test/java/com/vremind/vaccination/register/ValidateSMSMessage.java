/**
 * 
 */
package com.vremind.vaccination.register;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.vremind.vaccination.domain.RequestVaccinationTO;
import com.vremind.vaccination.presentation.register.RequestFormatterAndValidater;

/**
 * @author sdoddi
 *
 */
public class ValidateSMSMessage {

	
	@Test
	public void testSMSMessage()
	{
		Model model = new ExtendedModelMap();
		
		RequestFormatterAndValidater.validate("9000722000", "REMIND 04-08-12 NAVYA Shiva", model);
		
		Map<String, Object> data = model.asMap();
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.ACTIONABLE_ERROR_MESSAGE_KEY), false);
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY), false);
		
	}
	
	@Test
	public void testFormatSMSMessageToTO()
	{
		Model model = new ExtendedModelMap();
		RequestFormatterAndValidater.validate("9000722000", "REMIND   23-12-2013 MAHITH REDDY", model);
		Map<String, Object> data = model.asMap();
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.ACTIONABLE_ERROR_MESSAGE_KEY), false);
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY), false);
		
		RequestVaccinationTO reqeustTO = RequestFormatterAndValidater.formatMessageToTO("9000722000", "AP", "AIRTEL", "REMIND   23-12-2013 MAHITH REDDY", "04-06-2014 03:01:18 PM");
		
		Assert.assertNotNull(reqeustTO);
	}
	
	/*@Test
	public void testMissingBabyNameSMSMessage()
	{
		Model model = new ExtendedModelMap();
		
		RequestFormatterAndValidater.validate("9000722000", "REMIND 04-08-14", model);
		
		
		Map<String, Object> data = model.asMap();
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.ACTIONABLE_ERROR_MESSAGE_KEY), true);
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY), false);
		
	}
	
	@Test
	public void testWrongDateFormatSMSMessage()
	{
		Model model = new ExtendedModelMap();
		
		RequestFormatterAndValidater.validate("9000722000", "REMIND 04/08/14 NAVYA", model);
		
		
		Map<String, Object> data = model.asMap();
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.ACTIONABLE_ERROR_MESSAGE_KEY), true);
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY), false);
		
	}
	
	@Test
	public void testFutureDateFormatSMSMessage()
	{
		Model model = new ExtendedModelMap();
		
		RequestFormatterAndValidater.validate("9000722000", "REMIND 04/08/20 NAVYA", model);
		
		
		Map<String, Object> data = model.asMap();
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.ACTIONABLE_ERROR_MESSAGE_KEY), true);
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY), false);
		
	}
	
	@Test
	public void testStopSMSMessage()
	{
		Model model = new ExtendedModelMap();
		
		RequestFormatterAndValidater.validate("9000722000", "REMIND STOP 04-08-11 NAVYA", model);
		
		
		Map<String, Object> data = model.asMap();
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.ACTIONABLE_ERROR_MESSAGE_KEY), false);
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY), false);
		
	}
	
	@Test
	public void testStopDateAndMissingBabyNameSMSMessage()
	{
		Model model = new ExtendedModelMap();
		
		RequestFormatterAndValidater.validate("9000722000", "REMIND STOP", model);
		
		
		Map<String, Object> data = model.asMap();
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.ACTIONABLE_ERROR_MESSAGE_KEY), true);
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY), false);
	}
	
	
	@Test
	public void testStopMissingBabyNameSMSMessage()
	{
		Model model = new ExtendedModelMap();
		
		RequestFormatterAndValidater.validate("9000722000", "REMIND STOP 04-08-11", model);
		
		
		Map<String, Object> data = model.asMap();
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.ACTIONABLE_ERROR_MESSAGE_KEY), true);
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY), false);
	}
	
	@Test
	public void testStopFutureDateSMSMessage()
	{
		Model model = new ExtendedModelMap();
		
		RequestFormatterAndValidater.validate("9000722000", "REMIND STOP 04-08-20 NAVYA", model);
		
		
		Map<String, Object> data = model.asMap();
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.ACTIONABLE_ERROR_MESSAGE_KEY), true);
		Assert.assertEquals(data.containsKey(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY), false);
	}
	
	@Test
	public void testAckMessage()
	{
		String response = "Status=0,oa=REMIND,da=919000722060,report=D,sdate=2014-04-25 09:59:03,rdate=2014-04-25 09:59:10,tid=ins37_13984001438816,msg=Your+kid+navtej%27%27s+next+vaccine+%28DPT%2COPV%2CMMR%29+is+due+on+02-05-2014.+Please+consult+your+doctor+%26+administer+vaccine+timely.+www.vRemind.org-We+care%2C+We+remind";
		boolean status = false;
		if (response != null && response.indexOf(",") != -1)
		{
			String statusArray[] = response.split(",");
			if (statusArray != null && statusArray.length > 1)
			{
				if (statusArray[0].indexOf("=") != -1)
				{
					status = ("0".equals(statusArray[0].substring(statusArray[0].indexOf("=")+1)) ? true : false);
				}
			}
		}
		Assert.assertEquals(status, true);
	}*/
}
