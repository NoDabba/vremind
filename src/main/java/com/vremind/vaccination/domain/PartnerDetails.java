/**
 * 
 */
package com.vremind.vaccination.domain;

/**
 * @author sdoddi
 *
 */
public class PartnerDetails {
	
	private String hospitalKey;
	private String userId;
	private String branchName;
	private String logoUrl;
	private int hospitalId;
	/**
	 * @param hospitalKey
	 * @param userId
	 * @param branchName
	 */
	public PartnerDetails(String hospitalKey, String userId, String branchName) {
		super();
		this.hospitalKey = hospitalKey;
		this.userId = userId;
		this.branchName = branchName;
	}
	/**
	 * @return the hospitalKey
	 */
	public String getHospitalKey() {
		return hospitalKey;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}
	/**
	 * @return the logoUrl
	 */
	public String getLogoUrl() {
		return logoUrl;
	}
	/**
	 * @param logoUrl the logoUrl to set
	 */
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
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
	
}
