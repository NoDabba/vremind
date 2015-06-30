/**
 * 
 */
package com.vremind.vaccination.presentation.register;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vremind.vaccination.domain.AlertTO;
import com.vremind.vaccination.domain.RequestVaccinationTO;
import com.vremind.vaccination.domain.ResponseVaccinationTO;
import com.vremind.vaccination.domain.ResponseVaccinationTO.RESPONSE_VACCINATION_STATUS;
import com.vremind.vaccination.domain.UserType;
import com.vremind.vaccination.presentation.registration.ManualRegistrationCommand;
import com.vremind.vaccination.presentation.registration.ManualRegistrationHelper;
import com.vremind.vaccination.presentation.registration.ManualRegistrationValidator;
import com.vremind.vaccination.services.connector.SMSConnector;
import com.vremind.vaccination.services.register.VaccinationReminderBabyService;

/**
 * @author sdoddi
 *
 */
@Controller
public class OnlineRegistrationController {

	private final static Logger logger = LoggerFactory.getLogger(OnlineRegistrationController.class);

	@Autowired
	private ReloadableResourceBundleMessageSource messageSource;

	private VaccinationReminderBabyService registerBabyService;

	private SMSConnector smsConnector;

	@Autowired
	@Qualifier("smsConnector")
	public void setSMSConnector(SMSConnector smsConnector) {
		this.smsConnector = smsConnector;
	}

	/**
	 * 
	 * @param registerBabyService
	 */
	@Autowired
	public void setRegisterBabyService(VaccinationReminderBabyService registerBabyService) {
		this.registerBabyService = registerBabyService;
	}

	/**
	 * this method takes Registration JSON request and sends the response in json format
	 * @param command
	 * @param request
	 * @return RegisterStatus
	 */
	@RequestMapping(value = "/onlineregister.do", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody RegisterStatus submitRegistration(@ModelAttribute ManualRegistrationCommand command, HttpServletRequest request) {

		RegisterStatus status = new RegisterStatus();
		List<String> errorMessagesList = new ArrayList<String>();
		String message = null;

		// 1. check if entered form is valid
		List<String> errors = ManualRegistrationValidator.validate(UserType.ONLINE, command);

		// 2. if errors or any validation errors, show them on UI
		if (errors.size() > 0) {
			for (String error : errors) {
				errorMessagesList.add(messageSource.getMessage(error, null, Locale.getDefault()));
			}
			status.setFailureList(errorMessagesList);
			return status;
		}
		// 3. no errors. register the details to database
		try {
			MDC.put("mobileNo", command.getMobile());
			RequestVaccinationTO requestVaccinationTO = ManualRegistrationHelper.createRequestVaccinationTOfromCommand(UserType.ONLINE, command, request, -1);
			ResponseVaccinationTO response = registerBabyService.registerBaby(requestVaccinationTO);
			if (response != null) {
				message = response.getMessage();
			}
			
			if (RESPONSE_VACCINATION_STATUS.SUCCESS.equals(response.getStatus()) && message != null)
			{
				sendSMSMessage(command.getMobile(), message);
			}
			else if (RESPONSE_VACCINATION_STATUS.SYSTEM_ERROR.equals(response.getStatus()) && message != null) 
			{
				errorMessagesList.add(message);
				message = null;
			}
		}
		catch (Exception ex) {
			logger.error("Error occured while manual registration process", ex);
			errorMessagesList.add(messageSource.getMessage("manualregistration.error.generalerror", null, Locale.getDefault()));
		}
		finally {
			MDC.remove("mobileNo");
		}
		//set the message only when the response is success or ignored
		if (message != null) {
			status.setSuccess(message);
		}
		else
		{
			if (errorMessagesList.size() <= 0) {
				errorMessagesList.add("Unable to do registration. Please try later");
			}
			status.setFailureList(errorMessagesList);
		}
		return status;
	}

	/**
	 * helper method to send immediate SMS message to provided mobile number
	 * @param mobileNo
	 * @param message
	 */
	private void sendSMSMessage(String mobileNo, String message) {
		AlertTO alertTO = new AlertTO();
		alertTO.setMobileNo(mobileNo);
		alertTO.setMessage(message);
		String status = smsConnector.publishSMSMessageImmediate(alertTO);
		logger.debug("SMS publish message status {} : ", status);
	}

}
