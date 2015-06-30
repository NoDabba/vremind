/**
 * 
 */
package com.vremind.vaccination.presentation.admin;

import java.util.ArrayList;
import java.util.List;

import com.vremind.vaccination.common.util.WebUtil;

/**
 * @author sdoddi
 *
 */
public class AdminLoginValidator {

	/**
	 * validates the command and returns errors in the list
	 * @param command
	 * @return errors
	 */
	public static List<String> validate(AdminLoginCommand command) {
		
		List<String> errors = new ArrayList<String>();
		
		if (WebUtil.isEmpty(command.getUserId())) {
			errors.add("User ID cannot be null or empty");
		}
		
		if (WebUtil.isEmpty(command.getPassword())) {
			errors.add("Password cannot be null or empty");
		}
		
		if (!WebUtil.isEmpty(command.getUserId()) && !WebUtil.isEmpty(command.getPassword())) {
			
			String tmpUserId = "vremind";
			if (!command.getUserId().equals(tmpUserId)) {
				errors.add("Invalid userid or password");
			}
			else
			{
				String tempPass = "Mavericks@0314";
				if (!command.getPassword().equals(tempPass))
				{
					errors.add("Invalid userid or password");
				}
			}
		}
		return errors;
	}
	
}
