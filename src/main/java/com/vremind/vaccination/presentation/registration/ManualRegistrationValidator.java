/**
 * 
 */
package com.vremind.vaccination.presentation.registration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vremind.vaccination.common.util.WebUtil;
import com.vremind.vaccination.domain.UserType;


/**
 * @author sdoddi
 *
 */
public final class ManualRegistrationValidator  {


	/**
	 * email regex pattern
	 */
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	/**
	 * validates the <code>ManualRegistrationCommand</code> for valid data, if invalid data, this will return the list of errors back
	 * @param form
	 * @return List<String>
	 */
	public static List<String> validate(UserType type, ManualRegistrationCommand form) {
		List<String> errors = new ArrayList<String>();

		if (form == null) {
			errors.add("manualregistration.error.invalidform");
			return errors;
		}

		String mobileNumber = form.getMobile();
		if (WebUtil.isEmpty(mobileNumber)) {
			errors.add("manualregistration.error.invalidmobilenumber");
		}

		if (!WebUtil.isEmpty(mobileNumber)) {
			int len = mobileNumber.trim().length();
			if (len < 10 || len > 10) {
				errors.add("manualregistration.error.lengthmobilenumber");
			}
			else
			{
				boolean matches = Pattern.matches("[0-9]+", mobileNumber);
				if (!matches) {
					errors.add("manualregistration.error.digitsmobilenumber");
				}
			}
		}

		String babyName = form.getBabyName();
		if (!WebUtil.isEmpty(babyName)) {
			boolean matches = Pattern.matches("[a-zA-Z0-9]+", babyName);
			if (!matches) {
				errors.add("manualregistration.error.invalidcharactersbabyname");
			}
			else
			{
				int len = babyName.trim().length();
				if (len > 30) {
					errors.add("manualregistration.error.lengthbabyname");
				}
			}
		}

		String motherName = form.getMotherName();
		if (!WebUtil.isEmpty(motherName)) {
			boolean matches = Pattern.matches("^[a-zA-Z\\s]*$", motherName);
			if (!matches) {
				errors.add("manualregistration.error.invalidcharactersmothername");
			}
			else
			{
				int len = motherName.trim().length();
				if (len > 30) {
					errors.add("manualregistration.error.lengthmothername");
				}
			}
		}

		//baby name and mother name anyone is mandatory in case of babyNameValidationRequired is false (this will happen only during file upload process)
		if (WebUtil.isEmpty(motherName) && WebUtil.isEmpty(babyName)) {
			errors.add("manualregistration.error.invalidbabyandmothername");
		}

		String fatherName = form.getFatherName();
		if (!WebUtil.isEmpty(fatherName)) {
			boolean matches = Pattern.matches("^[a-zA-Z\\s]*$", fatherName);
			if (!matches) {
				errors.add("manualregistration.error.invalidcharactersfathername");
			}
			else
			{
				int len = fatherName.trim().length();
				if (len > 30) {
					errors.add("manualregistration.error.lengthfathername");
				}
			}
		}

		String dob = form.getDob();
		if (WebUtil.isEmpty(dob)) {
			errors.add("manualregistration.error.invaliddateofbirth");
		}

		if (!WebUtil.isEmpty(dob)){
			Date dateDOB = null;
			if (UserType.ONLINE.equals(type)) {
				dateDOB = WebUtil.getDateFromString(dob, WebUtil.ONLINE_DOB_FORMAT);
			}
			else {
				dateDOB = WebUtil.getDateFromString(dob, WebUtil.DOB_FORMAT);
			}
			if (dateDOB == null) {
				errors.add("manualregistration.error.invaliddateofbirthformat");
			}
			if (dateDOB != null) {
				int daysDifference = WebUtil.daysBetween(dateDOB, new Date());
				if (daysDifference < 0)
				{
					errors.add("manualregistration.error.invalidfuturedateofbirth");
				}
			}
		}

		String circle = form.getCircle();
		if (!WebUtil.isEmpty(circle)) {
			int len = circle.trim().length();
			if (len > 40) {
				errors.add("manualregistration.error.lengthcircle");
			}
		}
		/*if (WebUtil.isEmpty(circle)) {
			errors.add("manualregistration.error.invalidcircle");
		}*/

		String operatorName = form.getOperatorName();
		if (!WebUtil.isEmpty(operatorName)) {
			int len = operatorName.trim().length();
			if (len > 30) {
				errors.add("manualregistration.error.lengthoperatorname");
			}
		}
		/*if (WebUtil.isEmpty(operatorName)) {
			errors.add("manualregistration.error.invalidoperatorname");
		}*/

		String dateTime = form.getDateTime();
		/*if (WebUtil.isEmpty(dateTime)) {
			errors.add("manualregistration.error.invaliddatetime");
		}*/

		if (!WebUtil.isEmpty(dateTime)){
			//23-01-2015 09:33:28 AM
			boolean isValidDate = WebUtil.isValidDate(dateTime, WebUtil.REGISTRATION_DATE_TIME_FORMAT);
			if (!isValidDate) {
				errors.add("manualregistration.error.invaliddatetimeformat");
			}
		}

		String email = form.getEmail();
		if (!WebUtil.isEmpty(email)){
			Matcher matcher = pattern.matcher(email);
			if (!matcher.matches()) {
				errors.add("manualregistration.error.invalidemail");
			}
			else
			{
				int len = email.trim().length();
				if (len > 50) {
					errors.add("manualregistration.error.lengthemail");
				}
			}
		}

		return errors;
	}



}
