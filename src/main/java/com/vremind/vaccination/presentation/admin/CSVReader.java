/**
 * 
 */
package com.vremind.vaccination.presentation.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vremind.vaccination.common.util.WebUtil;
import com.vremind.vaccination.presentation.registration.ManualRegistrationCommand;

/**
 * @author sdoddi
 *
 */
public class CSVReader {
	
	private final static Logger logger = LoggerFactory.getLogger(CSVReader.class);

	
	/**
	 * reads the CSV file and returns the data in List<RequestVaccinationTO>
	 * @param csvData
	 * @return List<RequestVaccinationTO>
	 */
	public static List<ManualRegistrationCommand> parseCSVFile(String hospitalKey, String csvData) {
		List<ManualRegistrationCommand> list = new ArrayList<ManualRegistrationCommand>();
		
		StringTokenizer mainStk = new StringTokenizer(csvData, "\n");
		while (mainStk.hasMoreElements()) {
			String newLineStr = mainStk.nextToken();
			
			ManualRegistrationCommand request = parseNewLine(hospitalKey, newLineStr);
			if (request == null){
				logger.error("Unable to parse mobile record for {}", newLineStr);
				continue;
			}
			
			list.add(request);
		}
		return list;
	}
	
	/**
	 * helper method to parse and populate the command object with every new CSV line. If the parsing format is not correct, this will return null object 
	 * back to calling method.
	 * @param hospitalKey
	 * @param newLineStr
	 * @return ManualRegistrationCommand 
	 */
	private static ManualRegistrationCommand parseNewLine(String hospitalKey, String newLineStr)
	{
		if (WebUtil.isEmpty(newLineStr)) {
			return null;
		}
		
		StringTokenizer mainStk = new StringTokenizer(newLineStr, ",");
		if (mainStk.countTokens() > 0) {
			
			ManualRegistrationCommand request = new ManualRegistrationCommand();
			String dateStr = mainStk.nextToken();
			String mobileNumer = mainStk.nextToken();
			String motherName = mainStk.nextToken();
			String fatherName = mainStk.nextToken();
			
			request.setDob(dateStr);
			request.setMobile(mobileNumer);
			request.setMotherName(motherName);
			request.setFatherName(fatherName);
			request.setCircle(hospitalKey);
			request.setOperatorName(hospitalKey);
			return request;
		}
		
		return null;
	}
}
