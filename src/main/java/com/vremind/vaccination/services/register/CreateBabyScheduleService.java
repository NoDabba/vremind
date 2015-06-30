/**
 * 
 */
package com.vremind.vaccination.services.register;

import com.vremind.vaccination.domain.RequestVaccinationTO;

/**
 * @author sdoddi
 * This class will asynchronously create the schedule for baby's vaccination
 */
public interface CreateBabyScheduleService {

	public void createBabySchedule(int tmpSno, RequestVaccinationTO babyTO);
	
}
