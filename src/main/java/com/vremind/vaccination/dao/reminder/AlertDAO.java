/**
 * 
 */
package com.vremind.vaccination.dao.reminder;

import java.util.List;

import com.vremind.vaccination.domain.AlertTO;


/**
 * @author sdoddi
 * this is main interface to create, get all alerts, updates the statues with acks etc.. in vaccination_Alerts table
 */
public interface AlertDAO {

	/**
	 * This will create all eligible vaccination alerts in vacconation_alerts table and also sets the status in vaccination_schedule table to 2
	 * @return number of alerts created
	 */
	public int createVaccinationScheduleAlert();
	
	/**
	 * helper method to create the alert in vaccination_alert table, this is mostly called during the registration flow in error/(un-)registration success flows.
	 * @param alertTO
	 * @return status of inserted record
	 */
	public int createSingleAlert(final AlertTO alertTO);
	
	/**
	 * Gets the all alerts which are eliable to send to SMS provider
	 * @return List<AlertTO>
	 */
	public List<AlertTO> findAllAlerts();
	
	/**
	 * this will get all alerts which are sent but not acknowledged
	 * @return List<AlertTO>
	 */
	public List<AlertTO> findAllAlertsToCheckAcks();
	
	/**
	 * This method will update the ack id, ack date time, status, retry count ect.. in vaccination_alerts table
	 * @param alertTOList
	 * @param ackDateUpdate
	 * @return int[]
	 */
	public int[] updateAllAlertAckIds(List<AlertTO>  alertTOList, boolean ackDateUpdate);
	
	/**
	 * This method will move all failed (status as -1) or failed (status = 1 and ack date != null) to history table
	 * @return number of records updated
	 */
	public int moveFailedSucessAlertsToHistory();
	
}
