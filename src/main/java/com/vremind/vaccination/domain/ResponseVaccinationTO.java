/**
 * 
 */
package com.vremind.vaccination.domain;

/**
 * @author sdoddi
 *
 */
public class ResponseVaccinationTO {

	public enum RESPONSE_VACCINATION_STATUS {
		IGNORED,
		SYSTEM_ERROR,
		SUCCESS
	};
	private String message;
	private RESPONSE_VACCINATION_STATUS status;
	/**
	 * @param message
	 * @param status
	 */
	public ResponseVaccinationTO(String message,
			RESPONSE_VACCINATION_STATUS status) {
		super();
		this.message = message;
		this.status = status;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @return the status
	 */
	public RESPONSE_VACCINATION_STATUS getStatus() {
		return status;
	}
}
