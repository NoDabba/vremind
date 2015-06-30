/**
 * 
 */
package com.vremind.vaccination.domain;

/**
 * @author sdoddi
 *
 */
public class MasterVaccinationTO {

	private int sno;
	private String name;
	private String shortDesc;
	private int days;
	private String type;
	public MasterVaccinationTO(int sno, String shortDesc, String name, int days, String type) {
		super();
		this.sno = sno;
		this.name = name;
		this.days = days;
		this.type = type;
		this.shortDesc = shortDesc;
	}
	public int getSno() {
		return sno;
	}
	public String getName() {
		return name;
	}
	public int getDays() {
		return days;
	}
	public String getType() {
		return type;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	
	
}
