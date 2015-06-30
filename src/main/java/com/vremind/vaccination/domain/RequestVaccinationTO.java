/**
 * 
 */
package com.vremind.vaccination.domain;

import java.util.Date;

/**
 * @author sdoddi
 *
 */
public class RequestVaccinationTO {
	
	public enum RequestVaccinationType {
		REGISTER,
		UNREGISTER
	}
	
	private String mobileNo;
	private Date dob;
	private String babyName;
	private Date requestedDateTime;
	private String circle;
	private String operatorName;
	private RequestVaccinationType type;
	private boolean createBabyVacSchedule;
	private String email;
	private int hospitalId;
	private String motherName;
	private String fatherName;
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getBabyName() {
		return babyName;
	}
	public void setBabyName(String babyName) {
		this.babyName = babyName;
	}

	public Date getRequestedDateTime() {
		return requestedDateTime;
	}
	public void setRequestedDateTime(Date requestedDateTime) {
		this.requestedDateTime = requestedDateTime;
	}

	public String getCircle() {
		return circle;
	}
	public void setCircle(String circle) {
		this.circle = circle;
	}
	
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public RequestVaccinationType getType() {
		return type;
	}
	public void setType(RequestVaccinationType type) {
		this.type = type;
	}

	public boolean isCreateBabyVacSchedule() {
		return createBabyVacSchedule;
	}
	public void setCreateBabyVacSchedule(boolean createBabyVacSchedule) {
		this.createBabyVacSchedule = createBabyVacSchedule;
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("mobileNo : ").append(mobileNo).append(",");
		buffer.append("dob : ").append(dob).append(",");
		buffer.append("babyName : ").append(babyName).append(",");
		buffer.append("requestedDateTime : ").append(requestedDateTime).append(",");
		buffer.append("createBabyVacSchedule : ").append(createBabyVacSchedule).append(",");
		return buffer.toString();
	}
	/**
	 * @return the hospitalId
	 */
	public int getHospitalId() {
		return hospitalId;
	}
	/**
	 * @param hospitalId the hospitalId to set
	 */
	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}
	/**
	 * @return the motherName
	 */
	public String getMotherName() {
		return motherName;
	}
	/**
	 * @param motherName the motherName to set
	 */
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	/**
	 * @return the fatherName
	 */
	public String getFatherName() {
		return fatherName;
	}
	/**
	 * @param fatherName the fatherName to set
	 */
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
}
