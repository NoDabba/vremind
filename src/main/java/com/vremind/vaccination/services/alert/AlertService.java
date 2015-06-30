/**
 * 
 */
package com.vremind.vaccination.services.alert;

import java.util.List;

import com.vremind.vaccination.domain.AlertTO;

/**
 * @author sdoddi
 *
 */
public interface AlertService {
	
	public void publishMessage(List<AlertTO> alertTOList, String scheduledDate, String scheduleTime, boolean insert);

	public void ackMessage(List<AlertTO> alertTOList);
}
