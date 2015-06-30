/**
 * 
 */
package com.vremind.vaccination.presentation.partner.register;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.vremind.vaccination.domain.AlertTO;
import com.vremind.vaccination.domain.RequestVaccinationTO;
import com.vremind.vaccination.domain.ResponseVaccinationTO;
import com.vremind.vaccination.domain.ResponseVaccinationTO.RESPONSE_VACCINATION_STATUS;
import com.vremind.vaccination.domain.UserType;
import com.vremind.vaccination.presentation.admin.ManualRegistrationController;
import com.vremind.vaccination.presentation.registration.ManualRegistrationCommand;
import com.vremind.vaccination.presentation.registration.ManualRegistrationValidator;
import com.vremind.vaccination.presentation.registration.ManualRegistrationHelper;
import com.vremind.vaccination.services.connector.SMSConnector;
import com.vremind.vaccination.services.register.VaccinationReminderBabyService;

/**
 * @author sdoddi
 *
 */
@Controller
public class PartnerRegistrationController {

	private final static Logger logger = LoggerFactory.getLogger(ManualRegistrationController.class);
	private final String COMMAND_NAME="ManualRegistrationCommand";
			

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
	 * shows the manual registration form.
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/partner/register.do")
	public ModelAndView showManualRegistrationForm(HttpServletRequest request) {
		ModelAndView modelAndView = ManualRegistrationHelper.isSessionExists(UserType.PARTNER, request);
		if (modelAndView != null) {
			logger.debug("Invalid user tried to access Partner module");
			return modelAndView;
		}
		return new ModelAndView("/partner/partner_register", COMMAND_NAME, new ManualRegistrationCommand());
	}
	
	@RequestMapping(value = "/partner/submitregistration.do", method = RequestMethod.POST)
	public ModelAndView submitregistration(@ModelAttribute ManualRegistrationCommand command, HttpServletRequest request) {
		
		ModelAndView modelAndView = ManualRegistrationHelper.isSessionExists(UserType.PARTNER, request);
		if (modelAndView != null) {
			logger.debug("Invalid user tried to access partner module");
			return modelAndView;
		}
		
		List<String> errorMessagesList = new ArrayList<String>();
		String message = null;
		
		// 1. check if entered form is valid
		List<String> errors = ManualRegistrationValidator.validate(UserType.ADMIN, command);
		
		// 2. if errors or any validation errors, show them on UI
		if (errors.size() > 0) {
			for (String error : errors) {
				errorMessagesList.add(messageSource.getMessage(error, null, Locale.getDefault()));
			}
		}
		else
		{
			// 3. no errors. register the details to database
			try {
				MDC.put("mobileNo", command.getMobile());
				RequestVaccinationTO requestVaccinationTO = ManualRegistrationHelper.createRequestVaccinationTOfromCommand(UserType.PARTNER, command, request, -1);
				ResponseVaccinationTO response = registerBabyService.registerBaby(requestVaccinationTO);
				message = response.getMessage();
				// 4. send SMS message to only when the registration is success. Ignored we dont have to send any message
				if (RESPONSE_VACCINATION_STATUS.SUCCESS.equals(response.getStatus()) && message != null) {
					sendSMSMessage(command.getMobile(), message);
				}
				else if (RESPONSE_VACCINATION_STATUS.SYSTEM_ERROR.equals(response.getStatus()) && message != null) {
					errorMessagesList.add(messageSource.getMessage("manualregistration.error.generalerror", null, Locale.getDefault()));
					message = null;
				}
			}
			catch (Exception ex) {
				logger.error("Error occured while partner registration process", ex);
				errorMessagesList.add(messageSource.getMessage("manualregistration.error.generalerror", null, Locale.getDefault()));
			}
			finally {
				MDC.remove("mobileNo");
			}
		}
		// 5. set the view and model
		ModelAndView modelView = null;
		if (message != null) {
			modelView = new ModelAndView("/partner/partner_register", COMMAND_NAME, new PartnerRegistrationCommand());
			modelView.addObject("sucess", "Successfully registred "+command.getMobile()+" for vaccination reminders.");
		}
		else
		{
			modelView = new ModelAndView("/partner/partner_register", COMMAND_NAME, command);
			modelView.addObject("errors", errorMessagesList);
		}
		return modelView;

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
