/**
 * 
 */
package com.vremind.vaccination.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sdoddi
 *
 */
public class SMSMessageTemplates {
	
	private Map<String,String> messagesMap = new HashMap<String, String>();
	
	private static final SMSMessageTemplates templates = new SMSMessageTemplates();
	
	public static SMSMessageTemplates getInstance()
	{
		return templates;
	}
	
	public void addSMSTemplate(String key, String value)
	{
		messagesMap.put(key, value);
	}
	
	public String getSMSTemplate(String key)
	{
		return messagesMap.get(key);
	}
}
