/**
 * 
 */
package com.vremind.vaccination.datevalidation;

import org.junit.Test;

/**
 * @author sdoddi
 *
 */
public class StringFormatTest {

	private final static String REGISTRATION_SUCCESS = "Your kid %s, %s is registered for vaccination reminders @ www.vRemind.org. We care, We remind.";

	@Test
	public void testStringFormat()
	{
		String formatted = String.format(REGISTRATION_SUCCESS, "Navya", "04-08-12");
		System.out.println(formatted);
	}

	@Test
	public void testAckStatus()
	{
		String response ="Status=0,ins37_14002145412394";

		boolean sucessStatus = false;
		if (response != null && response.indexOf(",") != -1)
		{
			String status[] = response.split(",");
			if (status != null && status.length > 1)
			{
				if (status[0].indexOf("=") != -1)
				{
					sucessStatus = ("0".equals(status[0].substring(status[0].indexOf("=")+1)) ? true : false);
				}
			}
			if (sucessStatus)
			{
				System.out.println(status[1]);
			}

		}


	}
}
