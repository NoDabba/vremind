/**
 * 
 */
package com.vremind.vaccination.services.register;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.vremind.vaccination.common.RegistrationFailedException;
import com.vremind.vaccination.common.util.SMSMessageTemplates;
import com.vremind.vaccination.common.util.WebUtil;
import com.vremind.vaccination.dao.register.RegisterBabyDAO;
import com.vremind.vaccination.domain.AlertTO;
import com.vremind.vaccination.domain.RequestVaccinationTO;
import com.vremind.vaccination.domain.ResponseVaccinationTO;
import com.vremind.vaccination.domain.ResponseVaccinationTO.RESPONSE_VACCINATION_STATUS;
import com.vremind.vaccination.services.alert.AlertService;

/**
 * @author sdoddi
 * Implementation class to register the kid and create vaccination schedule asynchronously.
 */
public class VaccinationReminderBabyServiceImpl implements VaccinationReminderBabyService {

	private final static Logger logger = LoggerFactory.getLogger(VaccinationReminderBabyServiceImpl.class);

	private RegisterBabyDAO registerBabyDAO;
	private CreateBabyScheduleService createBabyScheduleService;
	private AlertService alertService;

	@Autowired
	public void setRegisterBabyDAO(RegisterBabyDAO registerBabyDAO) {
		this.registerBabyDAO = registerBabyDAO;
	}

	@Autowired
	public void setCreateBabyScheduleService(
			CreateBabyScheduleService createBabyScheduleService) {
		this.createBabyScheduleService = createBabyScheduleService;
	}

	@Autowired
	public void setAlertService(AlertService alertService) {
		this.alertService = alertService;
	}

	/**
	 * This method will register baby's vaccination schedule by inserting records in parents, baby's and vaccination schedule to database and send the success/failure message to requested mobile user. 
	 * It has annotated as "Transactional", if any of the table inserts fails all are rolled back.
	 * @param RequestVaccinationTO
	 * @return boolean
	 * 
	 */
	@Override
	@Transactional
	public ResponseVaccinationTO registerBaby(final RequestVaccinationTO babyTO) {

		ResponseVaccinationTO response = null;
		boolean isBabyNameExists = true;
		if (WebUtil.isEmpty(babyTO.getBabyName()) && !WebUtil.isEmpty(babyTO.getMotherName())) {
			isBabyNameExists = false;
		}
		try {
			final int sno = registerBabyDAO.registerBabyForVaccinationReminder(babyTO);
			if (logger.isDebugEnabled())
			{
				logger.debug("Registeration child sno {} ", sno);
			}
			if (sno > 0)
			{
				if (babyTO.isCreateBabyVacSchedule())
				{
					createBabyScheduleService.createBabySchedule(sno, babyTO);
					String sucessTemplate = null;
					String smsMessage = null;
					if (isBabyNameExists) {
						sucessTemplate = SMSMessageTemplates.getInstance().getSMSTemplate("vremind.register.success");
						String dateofBirthStr = WebUtil.getFormattedDate(babyTO.getDob(), "dd-MMM-yyyy");
						String babyname = WebUtil.truncateLeadingChars(babyTO.getBabyName(), WebUtil.BABY_NAME_MAX_LENGTH);
						smsMessage = String.format(sucessTemplate, babyname, dateofBirthStr);
					}
					else {
						sucessTemplate = SMSMessageTemplates.getInstance().getSMSTemplate("vremind.register.nobabyname.success");
						String motherName = WebUtil.truncateLeadingChars(babyTO.getMotherName(), WebUtil.BABY_NAME_MAX_LENGTH);
						smsMessage = String.format(sucessTemplate, motherName);
					}

					if (logger.isDebugEnabled())
					{
						logger.debug("Registration Sucess sms message - {} ", smsMessage);
					}
					response = new ResponseVaccinationTO(smsMessage, RESPONSE_VACCINATION_STATUS.SUCCESS);
					return response;
				}
				else
				{
					String sucessTemplate = SMSMessageTemplates.getInstance().getSMSTemplate("vremind.dob.morethan5years");
					response = new ResponseVaccinationTO(sucessTemplate, RESPONSE_VACCINATION_STATUS.IGNORED);
					return response;
				}
			}
		}
		catch (RegistrationFailedException regEx)
		{
			String errorCode = regEx.getMessage();
			if (logger.isErrorEnabled())
			{
				logger.error("Error creating vaccination schedule RegistrationFailedException {} " +
						" {} ", babyTO, regEx);
			}
			if ("1001".equals(errorCode))
			{
				String message = SMSMessageTemplates.getInstance().getSMSTemplate("vremind.register.baby.alredyexists");
				response = new ResponseVaccinationTO(message, RESPONSE_VACCINATION_STATUS.IGNORED);
				return response;
			}
		}
		catch (Exception ex)
		{
			if (logger.isErrorEnabled())
			{
				logger.error("Error creating vaccination schedule" +
						" {} error {}", babyTO, ex);
			}
		}
		response = new ResponseVaccinationTO("Unable to register your baby's vaccination reminders now. Please try later.", RESPONSE_VACCINATION_STATUS.SYSTEM_ERROR);
		return response;
	}

	/**
	 * This method will un-register baby's vaccination schedule and updates the status as "0" in parents, baby's and vaccination schedule to database and 
	 * send success/failure message to requested mobile user. 
	 * It has annotated as "Transactional", if any of the table updates fails all records are rolled back.
	 * @param RequestVaccinationTO
	 * @return boolean
	 * 
	 */
	@Override
	@Transactional
	public String unRegisterBaby(RequestVaccinationTO babyTO) {
		try {
			int rows = registerBabyDAO.unRegisterBabyForVaccinationReminder(babyTO.getMobileNo(), babyTO.getBabyName(), babyTO.getDob());
			if (logger.isDebugEnabled())
			{
				logger.debug("Un Registeration Number of rows effected {} ", rows);
			}

			if (rows <= 0)
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Un Registeration for all baby's with given mobile {} ", babyTO.getMobileNo());
				}
				rows = registerBabyDAO.unRegisterBabyForVaccinationReminder(babyTO.getMobileNo());
				if (logger.isDebugEnabled())
				{
					logger.debug("Un Registeration Number of rows effected for childern {} ", rows);
				}
			}

			if (rows > 0)
			{
				String sucessTemplate = SMSMessageTemplates.getInstance().getSMSTemplate("vremind.unregister.success");
				String dateofBirthStr = WebUtil.getFormattedDate(babyTO.getDob(), "dd-MMM-yyyy");
				String babyname = WebUtil.truncateLeadingChars(babyTO.getBabyName(), WebUtil.BABY_NAME_MAX_LENGTH);
				String smsMessage = String.format(sucessTemplate, babyname, dateofBirthStr);
				if (logger.isDebugEnabled())
				{
					logger.debug("Un-Registration Sucess sms message - {} ", smsMessage);
				}

				return smsMessage;
			}
			if (logger.isDebugEnabled())
			{
				logger.debug("Un-Registration unable to update sallowing the mobile {} name {} dob {} ", 
						new Object[]{babyTO.getMobileNo(), babyTO.getBabyName(), babyTO.getDob()});
			}

			return "We are unable to unregister your baby vaccination reminders. Please try later.";
		}
		catch (Exception ex)
		{
			if (logger.isErrorEnabled())
			{
				logger.error("Error unregistering vaccination schedule" +
						" {} ", babyTO);
			}
			return "We are unable to unregister your baby vaccination reminders. Please try later.";
		}
	}

	/**
	 * Helper method to all the Alerts Service tp publish message
	 * @param alertTO
	 */
	private void sendSMSMessage(AlertTO alertTO)
	{
		try {
			List<AlertTO> alertsList = new ArrayList<AlertTO>();
			alertsList.add(alertTO);
			alertService.publishMessage(alertsList, null, null, true);
		}
		catch (Exception ex)
		{
			logger.error("Error while sending SMS (un) registration message to {} error {} ", new Object[]{alertTO.getMobileNo(), ex});
		}

	}

}
