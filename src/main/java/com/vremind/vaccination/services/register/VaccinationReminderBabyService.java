package com.vremind.vaccination.services.register;

import com.vremind.vaccination.common.RegistrationFailedException;
import com.vremind.vaccination.domain.RequestVaccinationTO;
import com.vremind.vaccination.domain.ResponseVaccinationTO;

/**
 * Registration service to register the kid asynchronously.   
 * @author sdoddi
 *
 */
public interface VaccinationReminderBabyService {
	
	/**
	 * This will call the repo to load the data to database and asynchronously delegate the request to threadpool.
	 * @param RequestVaccinationTO
	 * @return boolean
	 * @throws RegistrationFailedException
	 */
	public ResponseVaccinationTO registerBaby(RequestVaccinationTO babyTO) ;
	
	public String unRegisterBaby(RequestVaccinationTO babyTO);

}
