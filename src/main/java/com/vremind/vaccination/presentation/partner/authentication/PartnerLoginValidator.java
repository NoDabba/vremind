/**
 * 
 */
package com.vremind.vaccination.presentation.partner.authentication;

import java.util.ArrayList;
import java.util.List;

import com.vremind.vaccination.common.util.WebUtil;

/**
 * @author sdoddi
 *
 */
public class PartnerLoginValidator {
	
	/**
	 * validates the command and returns errors in the list
	 * @param command
	 * @return errors
	 */
	public static List<String> validate(PartnerLoginCommand command) {
		
		List<String> errors = new ArrayList<String>();
		
		if (WebUtil.isEmpty(command.getUserId())) {
			errors.add("User ID cannot be null or empty");
		}
		
		if (WebUtil.isEmpty(command.getPassword())) {
			errors.add("Password cannot be null or empty");
		}
		return errors;
	}
	


}
