/**
 * 
 */
package com.vremind.vaccination.domain;

/**
 * @author sdoddi
 *
 */
public class SMSProviderDetails {
	
	private String schema;
	private String host;
	private String sendPath;
	private String ackPath;
	private String userName;
	private String senderId;
	public SMSProviderDetails(String schema, String host, String sendPath, String ackPath, String userName,
			String senderId) {
		super();
		this.schema = schema;
		this.host = host;
		this.sendPath = sendPath;
		this.ackPath = ackPath;
		this.userName = userName;
		this.senderId = senderId;
	}
	public String getSchema() {
		return schema;
	}
	public String getHost() {
		return host;
	}
	public String getSendPath() {
		return sendPath;
	}
	public String getAckPath() {
		return ackPath;
	}
	public String getUserName() {
		return userName;
	}
	public String getSenderId() {
		return senderId;
	}
	
}
