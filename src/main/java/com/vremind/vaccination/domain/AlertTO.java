/**
 * 
 */
package com.vremind.vaccination.domain;

import java.util.Date;

/**
 * @author sdoddi
 *
 */
public class AlertTO {
	
	private int sno;
	private int vaccinationScheduleId;
	private String mobileNo;
	private String message;
	
	private Date sentTimeStamp;
	private String ackId;
	private Date ackTimeStamp;
	private int status;
	private int retry;
	
	public AlertTO()
	{
		
	}
	public AlertTO(int sno, int vaccinationScheduleId, String mobileNo,
			String message) {
		super();
		this.sno = sno;
		this.vaccinationScheduleId = vaccinationScheduleId;
		this.mobileNo = mobileNo;
		this.message = message;
	}
	
	public AlertTO(int vaccinationScheduleId, String mobileNo,
			String message) {
		super();
		this.vaccinationScheduleId = vaccinationScheduleId;
		this.mobileNo = mobileNo;
		this.message = message;
	}
	
	
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public int getVaccinationScheduleId() {
		return vaccinationScheduleId;
	}
	public void setVaccinationScheduleId(int vaccinationScheduleId) {
		this.vaccinationScheduleId = vaccinationScheduleId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getSentTimeStamp() {
		return sentTimeStamp;
	}
	public void setSentTimeStamp(Date sentTimeStamp) {
		this.sentTimeStamp = sentTimeStamp;
	}
	public String getAckId() {
		return ackId;
	}
	public void setAckId(String ackId) {
		this.ackId = ackId;
	}
	public Date getAckTimeStamp() {
		return ackTimeStamp;
	}
	public void setAckTimeStamp(Date ackTimeStamp) {
		this.ackTimeStamp = ackTimeStamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getRetry() {
		return retry;
	}
	public void setRetry(int retry) {
		this.retry = retry;
	}
}
