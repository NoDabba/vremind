package com.vremind.vaccination.datevalidation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.vremind.vaccination.common.util.WebUtil;

/**
 * 
 */

/**
 * @author sdoddi
 *
 */

public class DateValidateTest {

	@Test
	public void testDateFunctions()
	{
		String dob = "01/04/13";
		
		/*int age = Validate.getAgeInYears(Validate.getDateFromString(dob, Validate.DOB_FORMAT));
		
		System.out.println(age);
		
		
		boolean isValid =  Validate.isValidDateAndLessThanOrEqualToCurrentDate(dob,  Validate.DOB_FORMAT);
		
		System.out.println(isValid);*/
		
		
		dob = "12-13-14";
		
		Date date = WebUtil.getDateFromString(dob, "dd-MM-yy");
		
		System.out.println(date);
		
		
		dob = "02-06-2015";
		Date futureDate = WebUtil.getDateFromString(dob, "dd-MM-yyyy");
		
		dob = "02-06-2015";
		Date currentDate= new Date();//WebUtil.getDateFromString(dob, "dd-MM-yyyy");
		
		int dateDiff = WebUtil.daysBetween(currentDate, futureDate);
		System.out.println("date difference = "+ dateDiff);
		
		SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
		String inputString1 = "03-06-2015";
		String inputString2 = "27 04 1997";

		try {
		    Date date1 = myFormat.parse(inputString1);
		    Date date2 = new Date();//myFormat.parse(inputString2);
		    /*long diff = date2.getTime() - date1.getTime();
		    System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));*/
			int dateDiff1 = WebUtil.daysBetween(date2, date1);
			System.out.println("dateDiff1 => "+ dateDiff1);

		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
	}
	
}
