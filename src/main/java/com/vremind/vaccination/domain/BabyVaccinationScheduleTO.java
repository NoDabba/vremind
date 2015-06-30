/**
 * 
 */
package com.vremind.vaccination.domain;

import java.util.Date;

/**
 * @author sdoddi
 *
 */
public class BabyVaccinationScheduleTO {
	
	private int childId;
	private int vaccinationId;
	private Date scheduledDate;
	
	private Date createdDate;
	private int status;
	private int retry;
	private int sno;
	
	public BabyVaccinationScheduleTO(int childId, int vaccinationId, Date scheduledDate, Date createdDate, int status) {
		super();
		this.childId = childId;
		this.vaccinationId = vaccinationId;
		this.scheduledDate = scheduledDate;
		this.createdDate = createdDate;
		this.status = status;
	}

	public BabyVaccinationScheduleTO(int childId, int vaccinationId, Date scheduledDate,
			Date createdDate, int status, int retry, int sno) {
		super();
		this.childId = childId;
		this.vaccinationId = vaccinationId;
		this.scheduledDate = scheduledDate;
		this.createdDate = createdDate;
		this.status = status;
		this.retry = retry;
		this.sno = sno;
	}

	public int getChildId() {
		return childId;
	}

	public int getVaccinationId() {
		return vaccinationId;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public int getStatus() {
		return status;
	}

	public int getRetry() {
		return retry;
	}

	public int getSno() {
		return sno;
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Child ID : ").append(this.childId).append(",");
		buffer.append("Vaccination ID : ").append(this.vaccinationId).append(",");
		buffer.append("Scheduled Date : ").append(this.scheduledDate);
		return buffer.toString();
	}

}
