/**
 * 
 */
package com.vremind.vaccination.common;

/**
 * @author sdoddi
 *
 */
public class RegistrationFailedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegistrationFailedException(String msg) {
		super(msg);
	}
}