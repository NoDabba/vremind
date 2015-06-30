/**
 * 
 */
package com.vremind.vaccination.services.connector;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vremind.vaccination.domain.AlertTO;
import com.vremind.vaccination.domain.SMSProviderDetails;

/**
 * @author sdoddi
 * This class is main entry point to connect to SMS provider over HTTP and get response from
 */
public class SMSConnectorImpl implements SMSConnector {

	private final static Logger logger = LoggerFactory.getLogger(SMSConnectorImpl.class);
	private final static int HTTP_SUCESS_STATUS_CODE = 200;
	private HttpClient httpClient;
	private SMSProviderDetails smsProviderDetails;

	@Autowired
	public void setSmsProviderDetails(SMSProviderDetails smsProviderDetails) {
		this.smsProviderDetails = smsProviderDetails;
	}
	
	@Autowired
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * This class will send sms message to SMS provider for given schedule date and time.
	 * @param alertTo
	 * @param scheduleDate
	 * @param scheduleTime
	 * @return Ackid
	 */
	@Override
	public String publishSMSScheduleMessage(AlertTO alertTo, String scheduleDate, String scheduleTime) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("receipientno",alertTo.getMobileNo());
			params.put("msgtxt",alertTo.getMessage());
			params.put("state","4");
			params.put("schedule_date", scheduleDate);
			params.put("schedule_time", scheduleTime);
			params.put("senderID", smsProviderDetails.getSenderId());
			return sendAndGetResponse(smsProviderDetails.getSendPath(), params);
		} catch (Exception e) {
			logger.error("Error send SMS Schedule to {} due to {}", alertTo.getMobileNo(), e);
		}
		return null;
	}

	/**
	 * This class will immediate send sms message to SMS provider
	 * @param alertTo
	 * @return Ackid
	 */
	@Override
	public String publishSMSMessageImmediate(AlertTO alertTo) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("receipientno",alertTo.getMobileNo());
			params.put("msgtxt",alertTo.getMessage());
			params.put("state","4");
			params.put("dcs", "0");
			params.put("senderID", smsProviderDetails.getSenderId());
			return sendAndGetResponse(smsProviderDetails.getSendPath(), params);
		} catch (Exception e) {
			logger.error("Error send SMS Immediate to {} due to {}", alertTo.getMobileNo(), e);
		}
		return null;
	}
	
	/**
	 * This message will get acknowledgment status from SMS PRovider for sent Message
	 * @param alertTo
	 * @param status
	 */
	@Override
	public String getAckStatus(AlertTO alertTo) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("tid",alertTo.getAckId());
			return sendAndGetResponse(smsProviderDetails.getAckPath(), params);
		} catch (Exception e) {
			logger.error("Error send SMS Schedule to {} due to {}", alertTo.getMobileNo(), e);
		}
		return null;
	}
	
	/**
	 * helper method to create the apache HTTP get objects with params and get response from provider
	 * @param params
	 * @return response
	 * @throws Exception
	 */
	private String sendAndGetResponse(String path, Map<String, String> params) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("sendAndGetResponse params {} ",params);
		}

		URIBuilder builder = new URIBuilder();
		builder.setScheme(smsProviderDetails.getSchema()).setHost(smsProviderDetails.getHost()).setPath(path)
		.setParameter("user", smsProviderDetails.getUserName());
		
		Iterator<String> it = params.keySet().iterator();
		while (it.hasNext())
		{
			String key = it.next();
			String value = params.get(key);
			builder.setParameter(key, value);
		}
		URI uri = builder.build();	
		if (logger.isDebugEnabled())
		{
			logger.debug("sendAndGetResponse uri {} ",uri);
		}
		HttpResponse response = httpClient.execute(new HttpGet(uri));
		if (response != null && response.getStatusLine().getStatusCode()==HTTP_SUCESS_STATUS_CODE)
		{
			String responseBody = EntityUtils.toString(response.getEntity());
			if (logger.isDebugEnabled())
			{
				logger.debug("response Body {} ",responseBody);
			}
			return responseBody;
		}
		throw new Exception("Unable to get response from provided url "+uri);
	}

}
