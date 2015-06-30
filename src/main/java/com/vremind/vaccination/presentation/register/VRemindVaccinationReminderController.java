/**
 * 
 */
package com.vremind.vaccination.presentation.register;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.vremind.vaccination.common.util.SMSMessageTemplates;
import com.vremind.vaccination.domain.RequestVaccinationTO;
import com.vremind.vaccination.domain.ResponseVaccinationTO;
import com.vremind.vaccination.services.register.VaccinationReminderBabyService;

/**
 * @author sdoddi
 *
 */
@Controller
@RequestMapping("/vRemindSMSService.do")
public class VRemindVaccinationReminderController {
	
	private final static Logger logger = LoggerFactory.getLogger(VRemindVaccinationReminderController.class);
	
	private VaccinationReminderBabyService registerBabyService;
	
	/**
	 * 
	 * @param registerBabyService
	 */
	@Autowired
	public void setRegisterBabyService(VaccinationReminderBabyService registerBabyService) {
		this.registerBabyService = registerBabyService;
	}
	
	
	/**
	 * This is a landing request from SMS provider to register the kid for vaccination
	 * @param mobileno
	 * @param dob
	 * @param babyname
	 * @param model
	 * @return String
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String smsService(@RequestParam("msisdn") String mobileNo, @RequestParam("circle") String circle, 
			@RequestParam("opnm") String operatorName, @RequestParam("sms") String message, @RequestParam("datetime") String datetime, Model model){
		MDC.put("mobileNo", mobileNo);
		try {
			if (logger.isInfoEnabled())
			{
				logger.info("Received SMS from {} and message {} circle {} operatorName {} datetime {}  ", new Object[] {mobileNo, message, circle, operatorName, datetime});
			}
			message = message.trim();
			
			RequestFormatterAndValidater.validate(mobileNo, message, model);
			Map<String, Object> modelAsMap = model.asMap();
			if (logger.isDebugEnabled())
			{
				logger.debug("controller Model {} ", modelAsMap);
			}
			if (modelAsMap.containsKey(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY))
			{
				logger.error("Showstopper Error during validation of SMS message {} ", modelAsMap.get(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY));
				model.addAttribute("message", modelAsMap.get(RequestFormatterAndValidater.IGNORE_ERROR_MESSAGE_KEY));
				return "status";
			}
			
			if (modelAsMap.containsKey(RequestFormatterAndValidater.ACTIONABLE_ERROR_MESSAGE_KEY))
			{
				String code = (String) modelAsMap.get(RequestFormatterAndValidater.ACTIONABLE_ERROR_MESSAGE_KEY);
				
				String errorMessage = SMSMessageTemplates.getInstance().getSMSTemplate(code);
				
				logger.error("Actionable Error during validation of SMS error code{} message {} ", code, 
						errorMessage);
				
				model.addAttribute("message", errorMessage);
				return "status";
			}
			
			RequestVaccinationTO vaccinationTo = RequestFormatterAndValidater.formatMessageToTO(mobileNo, circle, operatorName, message, datetime);
			switch (vaccinationTo.getType())
			{
				case REGISTER:
					try {
						String remessage = SMSMessageTemplates.getInstance().getSMSTemplate("vremind.server.error");
						ResponseVaccinationTO response = registerBabyService.registerBaby(vaccinationTo);
						
						// 4. send SMS message to registered user 
						if (response != null && response.getMessage() != null) {
							remessage = response.getMessage();	
						}
						model.addAttribute("message", remessage);
						if (logger.isInfoEnabled())
						{
							logger.info("baby registration status mobile {} message {}", mobileNo, remessage);
						}
					}
					catch (Exception ex)
					{
						logger.error("Unable to register baby for vaccination reminder mobile {}, sms message{}, error {}",new Object[]{mobileNo, message, ex});
						model.addAttribute("message", SMSMessageTemplates.getInstance().getSMSTemplate("vremind.server.error"));
					}
				break;
				case UNREGISTER:
					try {
						String unmessage = registerBabyService.unRegisterBaby(vaccinationTo);
						model.addAttribute("message", unmessage);
						if (logger.isInfoEnabled())
						{
							logger.info("baby unregistration status mobile {} unmessage {}", mobileNo, unmessage);
						}
					}
					catch (Exception ex)
					{
						logger.error("Unable to un-register baby for vaccination reminder mobile {}, sms message{}, error {}",new Object[]{mobileNo, message, ex});
						model.addAttribute("message", SMSMessageTemplates.getInstance().getSMSTemplate("vremind.server.error"));
					}
				break;
			}
		}
		catch (Exception ex)
		{
			logger.error("Some major issue happened for {} sms message {} exception {} ",new Object[]{mobileNo, message, ex}, ex);
			model.addAttribute("message", SMSMessageTemplates.getInstance().getSMSTemplate("vremind.server.error"));
		}
		finally {
			MDC.remove("mobileNo");
		}
		return "status";
	}
	
}
