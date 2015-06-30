/**
 * 
 */
package com.vremind.vaccination.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import com.vremind.vaccination.domain.User;

/**
 * @author sdoddi
 *
 */
public class WebUtil {

	public static final String DOB_FORMAT = "dd-MM-yy";
	public static final String ONLINE_DOB_FORMAT = "yyyy-mm-dd";
	public static final String REGISTRATION_DATE_TIME_FORMAT="dd-MM-yyyy HH:mm:ss a";
	public static final int BABY_NAME_MAX_LENGTH = 10;
	
	public static boolean isEmpty(String str)
	{
		return StringUtils.isEmpty(str);
	}
	
	public static String truncateLeadingChars(String name, int size)
	{
		if (name != null && name.length() > size)
		{
			return name.substring(0, name.length());
		}
		return name;
	}
	
	public static boolean isValidDate(String date, String format)
	{
		boolean isValid = false;
		try {
			SimpleDateFormat df = new SimpleDateFormat(format);
			df.parse(date);
			isValid = true;
		}
		catch (Exception ex){
			//**ignore*/
		}
		return isValid;
	}
	
	public static boolean isValidDateAndLessThanOrEqualToCurrentDate(String date, String format) {
		
		Date formattedDate = getDateFromString(date, format);
		if (formattedDate == null)
		{
			return false;
		}
		
		boolean isValid = false;
		
		int noofDays = daysBetween(formattedDate, new Date());
		if (noofDays >= 0)
			isValid = true;
		return isValid;
	}
	
	public static int daysBetween(Date d1, Date d2){
		
		if (d1.after(d2))
			return -1;
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
     }
	
	public static Date getDateFromString(String source, String format) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(format);
			df.setLenient(false);
			return df.parse(source);
		}
		catch (Exception ex){
			//**ignore*/
		}
		return null;
	}
	
	
	public static int getAgeInYears (Date dob) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, noofyears;         

        y = cal.get(Calendar.YEAR);// current year ,
        m = cal.get(Calendar.MONTH);// current month 
        d = cal.get(Calendar.DAY_OF_MONTH);//current day
        
        
        GregorianCalendar cal1 = new GregorianCalendar();
        cal1.setTime(dob);
        
        
        noofyears = y - cal1.get(Calendar.YEAR);
        if ((m < cal1.get(Calendar.MONTH))
                        || ((m == cal1.get(Calendar.MONTH)) && (d < cal1
                                        .get(Calendar.DAY_OF_MONTH)))) {
                --noofyears;
        }
       
        return noofyears;
	}
	
	public static Calendar getCalendar(Date date) {
	    Calendar cal = Calendar.getInstance(Locale.US);
	    cal.setTime(date);
	    return cal;
	}
	
	public static String getCurrentDate(String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	public static String getCurrentTime(String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
	public static String getFormattedDate(Date date, String format)
	{
		try {
			SimpleDateFormat sdf  = new SimpleDateFormat(format);
			return sdf.format(date);
		}
		catch (Exception ex)
		{
			//IGNORE
		}
		return null;
	}
	
	public static void setUserToSession(HttpServletRequest request, User user) {
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
	}
	
	public static User getUserFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session != null) {
			return (User) session.getAttribute("user");
		}
		return null;
	}
}
