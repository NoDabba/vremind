/**
 * 
 */
package com.vremind.vaccination.domain;

/**
 * @author sdoddi
 *
 */
public class AlertScheduleTO {
	
	private int scheduleId;
	private int vacId;
	private String mobileNo;
	private String scheduleDate;
	private String babyName;
	private String vacShortCode;
	private String motherName;
	private String fatherName;
	
	public AlertScheduleTO(int scheduleId, int vacId, String mobileNo,
			String babyName, String motherName, String fatherName, String vacShortCode,  String scheduleDate) {
		super();
		this.scheduleId = scheduleId;
		this.vacId = vacId;
		this.mobileNo = mobileNo;
		this.scheduleDate = scheduleDate;
		this.babyName = babyName;
		this.vacShortCode = vacShortCode;
		this.motherName = motherName;
		this.fatherName = fatherName;
	}
	public int getScheduleId() {
		return scheduleId;
	}
	public int getVacId() {
		return vacId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public String getScheduleDate() {
		return scheduleDate;
	}
	public String getBabyName() {
		return babyName;
	}
	/**
	 * @return the motherName
	 */
	public String getMotherName() {
		return motherName;
	}
	/**
	 * @return the fatherName
	 */
	public String getFatherName() {
		return fatherName;
	}
	public String getVacShortCode() {
		return vacShortCode;
	}
}
