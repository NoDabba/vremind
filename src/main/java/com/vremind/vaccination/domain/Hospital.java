/**
 * 
 */
package com.vremind.vaccination.domain;

/**
 * @author sdoddi
 *
 */
public class Hospital {
	
	private int id;
	private String key;
	private String name;
	private String address;
	private String status;
	private String logoUrl;
	/**
	 * @param id
	 * @param key
	 * @param name
	 * @param address
	 * @param status
	 * @param logoUrl
	 */
	public Hospital(int id, String key, String name, String address,
			String status, String logoUrl) {
		super();
		this.id = id;
		this.key = key;
		this.name = name;
		this.address = address;
		this.status = status;
		this.logoUrl = logoUrl;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @return the logoUrl
	 */
	public String getLogoUrl() {
		return logoUrl;
	}
}
