/**
 * 
 */
package com.vremind.vaccination.services.register;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vremind.vaccination.dao.register.RegisterBabyDAO;
import com.vremind.vaccination.domain.BabyVaccinationScheduleTO;
import com.vremind.vaccination.domain.MasterVaccinationTO;
import com.vremind.vaccination.domain.RequestVaccinationTO;
import com.vremind.vaccination.services.VaccinationMasterDetailsService;

/**
 * @author sdoddi
 * This service will calculate the baby's vaccination schedule dates for given date of birth and inserts into 
 * vaccination schedule table
 */
public class CreateBabyScheduleServiceImpl implements CreateBabyScheduleService {

	private final static Logger logger = LoggerFactory.getLogger(CreateBabyScheduleServiceImpl.class);

	private RegisterBabyDAO registerBabyDAO;
	private VaccinationMasterDetailsService vaccinationMasterDetailsService;
	
	@Autowired
	public void setVaccinationMasterDetailsService(
			VaccinationMasterDetailsService vaccinationMasterDetailsService) {
		this.vaccinationMasterDetailsService = vaccinationMasterDetailsService;
	}

	@Autowired
	public void setRegisterBabyDAO(RegisterBabyDAO registerBabyDAO) {
		this.registerBabyDAO = registerBabyDAO;
	}
	
	/**
	 * This will calculate the baby's vaccination schedule using data of birth and master vaccination schedule
	 * @param registered child id
	 * @param requested registration baby details
	 */
	@Override
	public void createBabySchedule(int babyId, RequestVaccinationTO babyTO) {
		if (logger.isDebugEnabled())
			logger.debug("Sno {}, Baby Info {} ", new Object[]{babyId, babyTO});
		
		Date currentDate = new Date();
		
		List<MasterVaccinationTO> vaccinationMasterTOList = vaccinationMasterDetailsService.getVaccinationDetails();
		List<BabyVaccinationScheduleTO> babyReminderTOList = new ArrayList<BabyVaccinationScheduleTO>();
		for (MasterVaccinationTO vaccinationTO: vaccinationMasterTOList)
		{
			int vaccinationId = vaccinationTO.getSno();
			int vaccinationDays = vaccinationTO.getDays();
			String vaccinationDaysType = vaccinationTO.getType();
			Calendar dobCal = Calendar.getInstance();
			dobCal.setTime(babyTO.getDob());
			if ("D".equals(vaccinationDaysType))
			{
				dobCal.add(Calendar.DAY_OF_MONTH, vaccinationDays);
			}
			else if ("W".equals(vaccinationDaysType))
			{
				dobCal.add(Calendar.DAY_OF_MONTH, vaccinationDays*7);
			}
			else if ("M".equals(vaccinationDaysType))
			{
				dobCal.add(Calendar.MONTH, vaccinationDays);
			}
			else if ("Y".equals(vaccinationDaysType))
			{
				dobCal.add(Calendar.YEAR, vaccinationDays);
			}
			
			int status = currentDate.after(dobCal.getTime()) ? 0 : 1;
			
			BabyVaccinationScheduleTO babyReminderTO = new BabyVaccinationScheduleTO(babyId, vaccinationId, dobCal.getTime(), babyTO.getRequestedDateTime(), status);
			babyReminderTOList.add(babyReminderTO);
			if (logger.isDebugEnabled())
				logger.debug("babyReminderTO {} ", babyReminderTO);
		}
		int status[] = registerBabyDAO.createBabyVaccinationSchedule(babyReminderTOList);
		if (logger.isDebugEnabled())
			logger.debug("insert status {} ", status);
		 
	}

}
