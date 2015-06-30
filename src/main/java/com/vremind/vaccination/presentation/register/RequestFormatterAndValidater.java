/**
 * 
 */
package com.vremind.vaccination.presentation.register;

import java.util.Date;
import java.util.StringTokenizer;

import org.springframework.ui.Model;

import com.vremind.vaccination.common.util.WebUtil;
import com.vremind.vaccination.domain.RequestVaccinationTO;
import com.vremind.vaccination.domain.RequestVaccinationTO.RequestVaccinationType;

/**
 * @author sdoddi
 * This is a helper class for Controller
 */
public class RequestFormatterAndValidater {
	
	public static final String IGNORE_ERROR_MESSAGE_KEY = "ignorerrmsg";
	public static final String ACTIONABLE_ERROR_MESSAGE_KEY = "actionerrmsg";
	private final static String SMS_MESSAGE_DELIMETER = " ";
	
	/**
	 * Breaks the sms messages to String array
	 * @param message
	 * @return String[]
	 */
	private static String[] getMessage(String message)
	{
		StringTokenizer stk = new StringTokenizer(message, SMS_MESSAGE_DELIMETER);
		String str[] = new String[stk.countTokens()];
		int count = 0;
		while (stk.hasMoreTokens())
		{
			str[count] = stk.nextToken();
			count++;
		}
		return str;
	}
	
	/**
	 * This method will validate the vremind SMS received message from provider. If any validation errors found, this will set the messages to Model
	 * @param mobileno
	 * @param message
	 * @param model
	 */
	public static void validate(String mobileno, String message, Model model)
	{
		if (WebUtil.isEmpty(mobileno))
		{
			model.addAttribute(IGNORE_ERROR_MESSAGE_KEY,"vremind.mobile.empty");
			return;
		}
		
		if (WebUtil.isEmpty(message))
		{
			model.addAttribute(IGNORE_ERROR_MESSAGE_KEY,"vremind.message.empty");
			return;
		}
		
		String values[] = getMessage(message);
		if (values == null)
		{
			model.addAttribute(ACTIONABLE_ERROR_MESSAGE_KEY,"vremind.message.invalid.format");
			return;
		}
		
		if (values.length < 3)
		{
			try {
				if (values[1].equalsIgnoreCase("stop"))
					model.addAttribute(ACTIONABLE_ERROR_MESSAGE_KEY,"vremind.unregister.message.invalid.format");
				else
					model.addAttribute(ACTIONABLE_ERROR_MESSAGE_KEY,"vremind.message.invalid.format");
			}
			catch (Exception ex)
			{
				model.addAttribute(ACTIONABLE_ERROR_MESSAGE_KEY,"vremind.message.invalid.format");
			}
			return;
		}
		if (values[1].equalsIgnoreCase("stop"))
		{
			if (values.length < 4)
			{
				model.addAttribute(ACTIONABLE_ERROR_MESSAGE_KEY,"vremind.unregister.message.invalid.format");
				return;
			}
		}

		if (!"remind".equalsIgnoreCase(values[0]))
		{
			model.addAttribute(IGNORE_ERROR_MESSAGE_KEY,"vremind.invalid.shortcode");
			return;
		}

		String dobStr = null, babyName = null;
		boolean unRegister = false;
		if (values[1].equalsIgnoreCase("stop"))
		{
			unRegister = true;
			dobStr = values[2];
			babyName = values[3];
		}
		else
		{
			dobStr = values[1];
			babyName = values[2];
		}
		
		if (WebUtil.isEmpty(dobStr))
		{
			model.addAttribute(ACTIONABLE_ERROR_MESSAGE_KEY, (unRegister ? "vremind.unregister.dob.invalid.format" : "vremind.dob.invalid.format"));
			return;
		}
		
		Date dateDOB = WebUtil.getDateFromString(dobStr, WebUtil.DOB_FORMAT);
		if (dateDOB == null)
		{
			model.addAttribute(ACTIONABLE_ERROR_MESSAGE_KEY, (unRegister ? "vremind.unregister.dob.invalid.format" : "vremind.dob.invalid.format"));
			return;
		}

		int daysDifference = WebUtil.daysBetween(dateDOB, new Date());
		if (daysDifference < 0)
		{
			model.addAttribute(ACTIONABLE_ERROR_MESSAGE_KEY, (unRegister ? "vremind.unregister.dob.futuredate" : "vremind.dob.futuredate"));
			return;
		} 

		if (WebUtil.isEmpty(babyName))
		{
			model.addAttribute(ACTIONABLE_ERROR_MESSAGE_KEY, (unRegister ? "vremind.unregister.message.invalid.format" : "vremind.message.invalid.format"));
			return;
		}
		
	}
	
	/**
	 * This method converts the vRemind vaccination request to TO object
	 * @param mobileNo
	 * @param circle
	 * @param state
	 * @param message
	 * @return RequestVaccinationTO
	 */
	public static RequestVaccinationTO formatMessageToTO(String mobileNo, String circle, String operatorName, String message, String dateTime)
	{
		
		RequestVaccinationTO requestVaccinationTO = new RequestVaccinationTO();
		
		String values[] = getMessage(message);
		
		String dobStr = null, babyName = null;
		boolean unRegister = false;
		if (values[1].equalsIgnoreCase("stop"))
		{
			unRegister = true;
			dobStr = values[2];
			babyName = values[3];
		}
		else
		{
			dobStr = values[1];
			babyName = values[2];
		}
		
		Date dateDOB = WebUtil.getDateFromString(dobStr, WebUtil.DOB_FORMAT);
		
		int age = WebUtil.getAgeInYears(dateDOB);
		
		boolean createBabyVacSchedule = (age >= 5 ? false : true);
		
		requestVaccinationTO.setMobileNo(mobileNo);
		requestVaccinationTO.setDob(dateDOB);
		requestVaccinationTO.setBabyName(babyName);
		requestVaccinationTO.setCircle(circle != null ? circle : "other");
		requestVaccinationTO.setCreateBabyVacSchedule(createBabyVacSchedule);
		requestVaccinationTO.setRequestedDateTime(new Date());
		requestVaccinationTO.setOperatorName(operatorName != null ? operatorName : "other");
		if (unRegister)
			requestVaccinationTO.setType(RequestVaccinationType.UNREGISTER);
		else
			requestVaccinationTO.setType(RequestVaccinationType.REGISTER);
		
		return requestVaccinationTO;
	}

}
