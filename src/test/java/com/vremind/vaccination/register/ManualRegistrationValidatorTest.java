/**
 * 
 */
package com.vremind.vaccination.register;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.vremind.vaccination.common.util.WebUtil;
import com.vremind.vaccination.domain.UserType;
import com.vremind.vaccination.presentation.registration.ManualRegistrationCommand;
import com.vremind.vaccination.presentation.registration.ManualRegistrationValidator;

/**
 * @author sdoddi
 *
 */
public class ManualRegistrationValidatorTest {

	@Test
	public void testFormNull() {

		ManualRegistrationCommand form = null;
		List<String> errors = ManualRegistrationValidator.validate(UserType.ADMIN, form);

		Assert.assertNotNull(errors);
	}
	
	@Test
	public void testFieldsNull() {

		ManualRegistrationCommand form = new ManualRegistrationCommand();
		List<String> errors = ManualRegistrationValidator.validate(UserType.ADMIN, form);

		Assert.assertNotNull(errors);
		System.out.println(errors);
		
		form = new ManualRegistrationCommand();
		form.setMobile("9000722060");
		form.setCircle("circle");
		form.setOperatorName("operatorName");
		form.setBabyName("babyName1babyNamebabyNa1234567");
		form.setDateTime("23-01-2015 09:33:28 AM");
		form.setDob("23-01-2015");
		form.setEmail("shiva@yahoo.com");
		form.setMotherName("Leena Shiva Kumar");
		errors = ManualRegistrationValidator.validate(UserType.ADMIN, form);

		Assert.assertNotNull(errors);
		System.out.println(errors);
		
		
		Date date = WebUtil.getDateFromString("12-MAR-2015", "dd-MMM-yyyy");
		System.out.println(date);
	}

}
