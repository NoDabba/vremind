/**
 * 
 */
package com.vremind.vaccination.presentation.register;

import java.util.List;

/**
 * @author sdoddi
 *
 */
public class RegisterStatus {
	private List<String> failureList;
	private String success;
	public List<String> getFailureList() {
		return failureList;
	}
	public void setFailureList(List<String> failureList) {
		this.failureList = failureList;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	
	
	
}
