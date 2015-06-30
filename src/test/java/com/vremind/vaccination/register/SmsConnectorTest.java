/**
 * 
 */
package com.vremind.vaccination.register;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vremind.vaccination.domain.AlertTO;
import com.vremind.vaccination.services.connector.SMSConnector;

/**
 * @author sdoddi
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationHttpConnectors.xml"})
public class SmsConnectorTest {
	


	@Autowired
	@Qualifier("smsConnector")
	private SMSConnector smsConnector;
	
	@BeforeClass
    public static void setUpBeforeClass() throws Exception {   
		
		System.setProperty("vremind.http.socketTimeoutInMillis", "45000");
		System.setProperty("vremind.http.connectionTimeoutInMillis", "45000");
		
		System.setProperty("vremind.sms.vVaayoo.url.schema", "http");
		System.setProperty("vremind.sms.vVaayoo.url.host", "api.mVaayoo.com");
		System.setProperty("vremind.sms.vVaayoo.username", "vremind.india@gmail.com:012987");
		System.setProperty("vremind.sms.vVaayoo.senderid", "REMIND");
		System.setProperty("vremind.sms.vVaayoo.send.url.path", "/mvaayooapi/MessageCompose");
		System.setProperty("vremind.sms.vVaayoo.ack.url.path", "/apidlvr/APIDlvReport");
	}
	
	@Test
	public void testSendSMS()
	{
	
		String response = smsConnector.publishSMSMessageImmediate(new AlertTO(-1,"919000722060","Baby of Saraswathi, 01-Mar-2014 is registered for vaccination reminders @ www.vRemind.org. We care, We remind."));
		System.out.println("Send SMS Status : " +response);
		Assert.assertNotNull(response);
	}
	
	@Test
	public void testGetAckStatus()
	{
		/*AlertTO alertTO = new AlertTO();
		alertTO.setAckId("ins37_14012979219233");
		String response = smsConnector.getAckStatus(alertTO);
		System.out.println("Ack Status response :"+response);
		Assert.assertNotNull(response);*/
	}

}
