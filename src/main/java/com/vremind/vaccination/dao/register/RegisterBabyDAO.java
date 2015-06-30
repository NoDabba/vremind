/**
 * 
 */
package com.vremind.vaccination.dao.register;

import java.util.Date;
import java.util.List;

import com.vremind.vaccination.domain.BabyVaccinationScheduleTO;
import com.vremind.vaccination.domain.RequestVaccinationTO;

/**
 * @author sdoddi
 * This DAO will do the following tasks
 * 1. Put request for register kid to database with status as processing
 * 2. Return the request serial number back to calling service
 * 3. Query the register kid request by serial number
 * 4. delete the record upon successful processing of register kid request
 * 5. change the status of the register baby request to 'X' in case if the data is not correct 
 */
public interface RegisterBabyDAO {
	
	public int registerBabyForVaccinationReminder(RequestVaccinationTO registerBabyTO);
	
	public int[] createBabyVaccinationSchedule(List<BabyVaccinationScheduleTO> vaccinationTOList);
	
	public int unRegisterBabyForVaccinationReminder(String mobileNo, String babyName, Date dob);
	
	public int unRegisterBabyForVaccinationReminder(String mobileNo);
}
