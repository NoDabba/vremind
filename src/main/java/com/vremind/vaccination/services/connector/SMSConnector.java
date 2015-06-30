/**
 * 
 */
package com.vremind.vaccination.services.connector;

import com.vremind.vaccination.domain.AlertTO;

/**
 * @author sdoddi
 * Core interface for publish the SMS message to publisher
 */
public interface SMSConnector {
	
	/**
	 * This class will send sms message to SMS provider for given schedule date and time.
	 * @param alertTo
	 * @param scheduleDate
	 * @param scheduleTime
	 * @return
	 */
	public String publishSMSScheduleMessage(AlertTO alertTo, String scheduleDate, String scheduleTime);
	
	/**
	 * This class will immediate send sms message to SMS provider
	 * @param alertTo
	 * @return
	 */
	public String publishSMSMessageImmediate(AlertTO alertTo);

	/**
	 * This message will get acknowledgment status from SMS PRovider for sent Message
	 * @param alertTo
	 * @return
	 */
	public String getAckStatus(AlertTO alertTo);
	
}
