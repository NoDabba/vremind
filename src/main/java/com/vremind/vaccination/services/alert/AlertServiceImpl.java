/**
 * 
 */
package com.vremind.vaccination.services.alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.vremind.vaccination.dao.reminder.AlertDAO;
import com.vremind.vaccination.domain.AlertTO;
import com.vremind.vaccination.services.connector.SMSConnector;

/**
 * @author sdoddi
 * Entry point to publish and receive acknowledgments from the SMS providers and update the status to Vaccination_Alerts table accordingly
 */
public class AlertServiceImpl implements AlertService {

	private final static Logger logger = LoggerFactory.getLogger(AlertServiceImpl.class);

	private AlertDAO alertDAO;

	private SMSConnector smsConnector;

	@Autowired
	public void setAlertDAO(
			AlertDAO alertDAO) {
		this.alertDAO = alertDAO;
	}

	@Autowired
	@Qualifier("smsConnector")
	public void setSMSConnector(SMSConnector smsConnector) {
		this.smsConnector = smsConnector;
	}

	/**
	 * This message publish the SMS message for given schedule date and time, if not provied, this will send he immediate message.
	 * the Response ACK ID from SMS provider will be updated Alerts Table
	 * @param alertTOList
	 * @param scheduledDate
	 * @param scheduleTime
	 * @param insert
	 */
	@Override
	public void publishMessage(List<AlertTO> alertTOList, String scheduledDate, String scheduleTime, boolean insert) {

		if (logger.isDebugEnabled())
		{
			logger.debug("alertTOList size {} ", (alertTOList != null ? alertTOList.size() : "NULL"));
		}

		if (insert)
		{
			for (AlertTO alertTO : alertTOList)
			{
				int sno = alertDAO.createSingleAlert(alertTO);
				alertTO.setSno(sno);
			}
		}

		int countFailureWithNoAck = 0;
		List<AlertTO> alertTOsWithAckSucessList = new ArrayList<AlertTO>();

		for (AlertTO alertTo : alertTOList)
		{
			String response = null;
			if (scheduledDate != null && scheduleTime != null)
				response = smsConnector.publishSMSScheduleMessage(alertTo, scheduledDate, scheduleTime);
			else
				response = smsConnector.publishSMSMessageImmediate(alertTo);
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
					String ackId = status[1].trim();
					alertTo.setSentTimeStamp(new Date());
					alertTo.setAckId(ackId);
					alertTOsWithAckSucessList.add(alertTo);
				}
				else
				{
					countFailureWithNoAck = countFailureWithNoAck+1;
				}
			}
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("No. of SMS Alerts messages not sent {} ", countFailureWithNoAck);
		}

		this.alertDAO.updateAllAlertAckIds(alertTOsWithAckSucessList, false);

		if (logger.isDebugEnabled())
		{
			logger.debug("Endeded Send SMS Triggering at {} ", new Date());
		}

	}

	/**
	 * This message will get the acknowledge message from the SMS Provider. if the given status is not sucessfull, this will
	 * send Immediate SMS message for given Alert and update the returned ACK ID.
	 * If the Alert retry is 2, the status will be set has -1 to that next time that particular message is not picked up.
	 * @param alertAckTOList
	 */
	@Override
	public void ackMessage(List<AlertTO> alertAckTOList) {
		for (AlertTO alertTO : alertAckTOList)
		{
			boolean status = false;
			String response = smsConnector.getAckStatus(alertTO);
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
						
			if (logger.isDebugEnabled())
			{
				logger.debug("Status for ackid {} status {}  ", alertTO.getAckId(), status);
			}
			
			if (status)
			{
				alertTO.setStatus(1);
				alertTO.setAckTimeStamp(new Date());
			}
			else
			{
				response = smsConnector.publishSMSMessageImmediate(alertTO);
				if (response != null)
				{
					String ackId = null;
					boolean sucessStatus = false;
					if (response != null && response.indexOf(",") != -1)
					{
						String statusArray[] = response.split(",");
						if (statusArray != null && statusArray.length > 1)
						{
							if (statusArray[0].indexOf("=") != -1)
							{
								sucessStatus = ("0".equals(statusArray[0].substring(statusArray[0].indexOf("=")+1)) ? true : false);
							}
						}
						if (sucessStatus)
						{
							ackId = statusArray[1].trim();
						}
					}
					
					alertTO.setAckId(ackId);
				}
				int retryCount = alertTO.getRetry();
				alertTO.setStatus(retryCount == 2 ? -1 : 1);
				alertTO.setRetry(retryCount++);
			}
		}

		this.alertDAO.updateAllAlertAckIds(alertAckTOList, true);
		if (logger.isDebugEnabled())
		{
			logger.debug("Endeded Send SMS Triggering at {} , status {}", new Date());
		}

	}
}
