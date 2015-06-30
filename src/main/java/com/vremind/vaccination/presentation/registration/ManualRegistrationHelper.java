/**
 * 
 */
package com.vremind.vaccination.presentation.registration;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.vremind.vaccination.common.util.WebUtil;
import com.vremind.vaccination.domain.PartnerDetails;
import com.vremind.vaccination.domain.RequestVaccinationTO;
import com.vremind.vaccination.domain.RequestVaccinationTO.RequestVaccinationType;
import com.vremind.vaccination.domain.User;
import com.vremind.vaccination.domain.UserType;

/**
 * @author sdoddi
 *
 */
public class ManualRegistrationHelper {

	/**
	 * Helper method to transfer the data from command to TO
	 * @param command
	 * @return RequestVaccinationTO
	 */
	public static RequestVaccinationTO createRequestVaccinationTOfromCommand(UserType type, ManualRegistrationCommand command, 
			HttpServletRequest request, int hospitalId) {

		RequestVaccinationTO requestVaccinationTO = new RequestVaccinationTO();
		Date dateDOB = null;
		if (UserType.ONLINE.equals(type)) {
			dateDOB = WebUtil.getDateFromString(command.getDob(), WebUtil.ONLINE_DOB_FORMAT);
		}
		else
		{
			dateDOB = WebUtil.getDateFromString(command.getDob(), WebUtil.DOB_FORMAT);
		}
		Date requestedDateTime = WebUtil.getDateFromString(command.getDateTime(), WebUtil.REGISTRATION_DATE_TIME_FORMAT);
		int age = WebUtil.getAgeInYears(dateDOB);
		boolean createBabyVacSchedule = (age >= 5 ? false : true);

		requestVaccinationTO.setMobileNo("91"+command.getMobile());
		requestVaccinationTO.setDob(dateDOB);
		requestVaccinationTO.setBabyName(command.getBabyName());
		requestVaccinationTO.setCreateBabyVacSchedule(createBabyVacSchedule);
		requestVaccinationTO.setEmail(command.getEmail());
		requestVaccinationTO.setMotherName(command.getMotherName());
		requestVaccinationTO.setFatherName(command.getFatherName());
		requestVaccinationTO.setRequestedDateTime(requestedDateTime != null ? requestedDateTime : new Date());
		requestVaccinationTO.setType(RequestVaccinationType.REGISTER);

		switch (type) {
			case PARTNER:
				PartnerDetails partnerDetails = WebUtil.getUserFromSession(request).getPartnerDetails();
				requestVaccinationTO.setOperatorName(partnerDetails.getHospitalKey());
				requestVaccinationTO.setCircle(partnerDetails.getBranchName());
				requestVaccinationTO.setHospitalId(partnerDetails.getHospitalId());
			break;
			case ADMIN:
				requestVaccinationTO.setCircle(command.getCircle() != null ? command.getCircle() : "other");
				requestVaccinationTO.setOperatorName(command.getOperatorName() != null ? command.getOperatorName() : "other");
				requestVaccinationTO.setHospitalId(hospitalId);
			break;
			case ONLINE:
				requestVaccinationTO.setCircle("online");
				requestVaccinationTO.setOperatorName("online");
			break;
		}
		return requestVaccinationTO;
	}
	
	/**
	 * helper method to check if session exists or not. if not exists, this will return view for given user type
	 * @param type
	 * @param request
	 * @return ModelAndView
	 */
	public static ModelAndView isSessionExists(UserType type, HttpServletRequest request) {
		User user = WebUtil.getUserFromSession(request);
		if (user == null) {
			switch (type) {
				case PARTNER:
					return new ModelAndView("forward:/partner/signon.do");
				case ADMIN:
					return new ModelAndView("forward:/admin/signon.do");
			}
		}
		return null;
	}
}
