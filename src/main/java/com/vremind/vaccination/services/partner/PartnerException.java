/**
 * 
 */
package com.vremind.vaccination.services.partner;

/**
 * @author sdoddi
 *
 */
public class PartnerException extends Exception {

	private static final long serialVersionUID = 1L;

	public PartnerException(String msg) {
		super(msg);
	}
	
	public PartnerException(String msg, Throwable th) {
		super(msg, th);
	}
}