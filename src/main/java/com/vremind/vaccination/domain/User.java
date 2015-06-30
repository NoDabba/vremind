/**
 * 
 */
package com.vremind.vaccination.domain;

import java.util.Date;

/**
 * @author sdoddi
 *
 */
public class User {
	
	private String userId;
	private Date loggedInTime;
	private UserType userType;
	private PartnerDetails partnerDetails;
	/**
	 * @param userId
	 * @param loggedInTime
	 * @param userType
	 * @param partnerDetails
	 */
	public User(String userId, Date loggedInTime, UserType userType,
			PartnerDetails partnerDetails) {
		super();
		this.userId = userId;
		this.loggedInTime = loggedInTime;
		this.userType = userType;
		this.partnerDetails = partnerDetails;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @return the loggedInTime
	 */
	public Date getLoggedInTime() {
		return loggedInTime;
	}
	/**
	 * @return the userType
	 */
	public UserType getUserType() {
		return userType;
	}
	/**
	 * @return the partnerDetails
	 */
	public PartnerDetails getPartnerDetails() {
		return partnerDetails;
	}
	
	
	
}
