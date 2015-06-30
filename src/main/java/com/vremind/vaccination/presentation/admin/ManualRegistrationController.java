/**
 * 
 */
package com.vremind.vaccination.presentation.admin;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.vremind.vaccination.common.util.WebUtil;
import com.vremind.vaccination.domain.AlertTO;
import com.vremind.vaccination.domain.Hospital;
import com.vremind.vaccination.domain.RequestVaccinationTO;
import com.vremind.vaccination.domain.ResponseVaccinationTO;
import com.vremind.vaccination.domain.ResponseVaccinationTO.RESPONSE_VACCINATION_STATUS;
import com.vremind.vaccination.domain.UserType;
import com.vremind.vaccination.presentation.registration.ManualRegistrationCommand;
import com.vremind.vaccination.presentation.registration.ManualRegistrationValidator;
import com.vremind.vaccination.presentation.registration.ManualRegistrationHelper;
import com.vremind.vaccination.services.HospitalMasterService;
import com.vremind.vaccination.services.connector.SMSConnector;
import com.vremind.vaccination.services.register.VaccinationReminderBabyService;

/**
 * @author sdoddi
 * This controller will enable admin (in future this might be used by customers as well) to manually register the users who are unable or 
 * failed during registration process
 */
@Controller
public class ManualRegistrationController {

	private final static Logger logger = LoggerFactory.getLogger(ManualRegistrationController.class);
	private final String COMMAND_NAME="ManualRegistrationCommand";


	@Autowired
	private ReloadableResourceBundleMessageSource messageSource;
	private VaccinationReminderBabyService registerBabyService;
	private SMSConnector smsConnector;
	private HospitalMasterService hospitalMasterService;

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
	 * @param hospitalMasterService the hospitalMasterService to set
	 */
	@Autowired
	public void setHospitalMasterService(HospitalMasterService hospitalMasterService) {
		this.hospitalMasterService = hospitalMasterService;
	}
	
	/**
	 * shows the manual registration form.
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/admin/register.do")
	public ModelAndView showManualRegistrationForm(HttpServletRequest request) {
		ModelAndView modelAndView = ManualRegistrationHelper.isSessionExists(UserType.ADMIN, request);
		if (modelAndView != null) {
			logger.debug("Invalid user tried to access admin module");
			return modelAndView;
		}
		return new ModelAndView("/admin/register", COMMAND_NAME, new ManualRegistrationCommand());
	}

	@RequestMapping(value = "/admin/submitregistration.do", method = RequestMethod.POST)
	public ModelAndView submitregistration(@ModelAttribute ManualRegistrationCommand command, HttpServletRequest request) {

		ModelAndView modelAndView = ManualRegistrationHelper.isSessionExists(UserType.ADMIN, request);
		if (modelAndView != null) {
			logger.debug("Invalid user tried to access admin module");
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
				RequestVaccinationTO requestVaccinationTO = ManualRegistrationHelper.createRequestVaccinationTOfromCommand(UserType.ADMIN, command, request, -1);
				ResponseVaccinationTO response = registerBabyService.registerBaby(requestVaccinationTO);
				
				// 4. send SMS message to registered user 
				if ((RESPONSE_VACCINATION_STATUS.SUCCESS.equals(response.getStatus()) || RESPONSE_VACCINATION_STATUS.IGNORED.equals(response.getStatus())) 
						&& response.getMessage() != null)
				{
					message = response.getMessage();
					sendSMSMessage(command.getMobile(), message);
				}
				else {
					errorMessagesList.add(response.getMessage());
				}
			}
			catch (Exception ex) {
				logger.error("Error occured while manual registration process", ex);
				errorMessagesList.add(messageSource.getMessage("manualregistration.error.generalerror", null, Locale.getDefault()));
			}
			finally {
				MDC.remove("mobileNo");
			}
		}
		// 5. set the view and model
		ModelAndView modelView = null;
		if (message != null) {
			modelView = new ModelAndView("/admin/register", COMMAND_NAME, new ManualRegistrationCommand());
			modelView.addObject("sucess", message);
		}
		else
		{
			modelView = new ModelAndView("/admin/register", COMMAND_NAME, command);
			modelView.addObject("errors", errorMessagesList);
		}
		return modelView;
	}

	/**
	 * this will show the vaccination file upload screen to admin
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/admin/register/bulkupload.do")
	public ModelAndView showManualRegistrationBulkUploadForm(HttpServletRequest request) {
		ModelAndView modelAndView = ManualRegistrationHelper.isSessionExists(UserType.ADMIN, request);
		if (modelAndView != null) {
			logger.debug("Invalid user tried to access admin module");
			return modelAndView;
		}
		return new ModelAndView("/admin/register_fileupload");
	}


	/**
	 * This will parse the CSV file and register each request.
	 * @param hospitalKey
	 * @param file
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/admin/register/bulkupload.do", method = RequestMethod.POST)
	public ModelAndView submitManualRegistrationBulkUploadForm(@RequestParam("hospitalKey") String hospitalKey, 
			@RequestParam("file") MultipartFile file, HttpServletRequest request) {

		// 1. validate the session
		List<String> errorMessagesList = new ArrayList<String>();
		ModelAndView modelAndView = ManualRegistrationHelper.isSessionExists(UserType.ADMIN, request);
		if (modelAndView != null) {
			logger.debug("Invalid user tried to access admin module");
			return modelAndView;
		}
		//2. check the file size. allow the condition only when csv is greater than 0 size 
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				String csv = new String(bytes);
				
				//3. if the hospital key is null or empty, assign the parent register to default hospital master
				if (WebUtil.isEmpty(hospitalKey)) {
					hospitalKey = "DEFAULT";
				}
				
				//4. get the Hospital Master data for given hospital key
				int hospitalId = -1;
				try {
					Hospital hospital = findHospitalByKey(hospitalKey);
					if (hospital != null) {
						hospitalId = hospital.getId();
					}
				}
				catch (Exception ex) {
					logger.error("<File Upload> Error while getting the Hospital master data. please try again", ex);
					errorMessagesList.add("Error while getting the Hospital master data. please try again");
					modelAndView = new ModelAndView("/admin/register_fileupload");
					modelAndView.addObject("errors", errorMessagesList);
					return modelAndView;
				}
				
				//5. if hospital master data not found, show error and return to bulk load form
				if (hospitalId == -1) {
					logger.error("<File Upload> Unable to find the hospital master data for given hospital key : {} . please try again", hospitalKey);
					errorMessagesList.add("Unable to find the hospital master data for given hospital key : "+hospitalKey+" . please try again");
					modelAndView = new ModelAndView("/admin/register_fileupload");
					modelAndView.addObject("errors", errorMessagesList);
					return modelAndView;
				}
				
				//6. parse the CSV. we are reusing the same Command
				List<ManualRegistrationCommand> requestList = CSVReader.parseCSVFile(hospitalKey, csv);
				if (requestList != null && requestList.size() > 0) {
					int systemErrorsCount = 0;
					int ignoredCount = 0;
					int sucessCount = 0;
					int validationCount = 0;
					for (ManualRegistrationCommand command : requestList) {
						try {
							//7. validate the each entry. if error occurs continue to next record but show the error count and 
							List<String> errors = ManualRegistrationValidator.validate(UserType.ADMIN, command);
							if (errors.size() > 0) {
								validationCount = validationCount +1;
								logger.error("<File Upload> Vaccination record is not in correct format for : {} and errors are : {}", command.getMobile(), errors);
								continue;
							}
							MDC.put("mobileNo", command.getMobile());
							//8. transfer the data from command to request TO
							RequestVaccinationTO requestVaccinationTO = ManualRegistrationHelper.createRequestVaccinationTOfromCommand(UserType.ADMIN, command, request, hospitalId);
							ResponseVaccinationTO response = registerBabyService.registerBaby(requestVaccinationTO);
							String message = response.getMessage();
							// 9. send SMS message to only when the registration is success. Ignored we dont have to send any message
							if (RESPONSE_VACCINATION_STATUS.SUCCESS.equals(response.getStatus()) && message != null) {
								sendSMSMessage(command.getMobile(), message);
								sucessCount = sucessCount+1;
							}
							else if (RESPONSE_VACCINATION_STATUS.IGNORED.equals(response.getStatus()))
							{
								ignoredCount = ignoredCount + 1;
							}
							else {
								logger.error("<File Upload> Error during registering vaccination for mobileNo : {} and message : {}", command.getMobile(), message);
								systemErrorsCount = systemErrorsCount + 1;
							}
						}
						catch (Exception ex) {
							logger.error("<File Upload> Error during registering vaccination for mobileNo : {} ", command.getMobile(), ex);
							systemErrorsCount = systemErrorsCount + 1;
						}
						finally {
							MDC.remove("mobileNo");
						}
					}
					String status  = "File upload status : Request received = "+ requestList.size()+", Success = "+sucessCount+", " +
							"Validation errors count= "+validationCount +", Ignored count = "+ignoredCount+", System Errors = "+systemErrorsCount;
					modelAndView = new ModelAndView("/admin/register_fileupload");
					modelAndView.addObject("sucess",status);
					return modelAndView;
				}
			} catch (Exception e) {
				logger.error("<File Upload> You failed to upload " + hospitalKey + " => " + e.getMessage(), e);
			}
		} else {
			logger.error("<File Upload> You failed to upload " + hospitalKey
					+ " because the file was empty.");
		}
		errorMessagesList.add("Error occured during vaccination file upload. Please check logs for more information.");
		modelAndView = new ModelAndView("/admin/register_fileupload");
		modelAndView.addObject("errors", errorMessagesList);
		return modelAndView;
	}

	/**
	 * This method gets the hospital master details for given Hospital key. 
	 * @param hospitalKey
	 * @return Hospital
	 * @throws Exception
	 */
	private Hospital findHospitalByKey(String hospitalKey) throws Exception {
		List<Hospital> hospitalMasterList = null;
		try {
			hospitalMasterList = hospitalMasterService.getHospitalMaster();
		}
		catch (Exception ex) {
			logger.error("Error while getting the Hospital Master data from Service", ex);
			throw new Exception("Unable to get Hospital Master Details. Please try later.", ex);
		}
		
		//validate if partner details configured in your system
		if (hospitalMasterList != null) {
			for (Hospital hospital : hospitalMasterList) {
				if (hospital.getKey().equalsIgnoreCase(hospitalKey)) {
					return hospital;
				}
			}
		}
		return null;
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
