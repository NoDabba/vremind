/**
 * 
 */
package com.vremind.vaccination.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sdoddi
 *
 */
public class CryptWithMD5 {

	private static MessageDigest md;

	/**
	 * encrypts the pwd using MD5
	 * @param pass
	 * @return String
	 */
	public static String cryptWithMD5(String pass){
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] passBytes = pass.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<digested.length;i++){
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
		}
		return null;
	}
}
