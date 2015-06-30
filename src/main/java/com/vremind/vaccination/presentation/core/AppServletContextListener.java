/**
 * 
 */
package com.vremind.vaccination.presentation.core;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vremind.vaccination.common.util.SMSMessageTemplates;

/**
 * @author sdoddi
 * Starts during server startup and loads all application configuration to System properties file. And also populate the sms templates to singleton object
 */
public class AppServletContextListener implements ServletContextListener {

	private final static Logger logger = LoggerFactory.getLogger(AppServletContextListener.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//ignore		
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext ctx = servletContextEvent.getServletContext();
    	String appconfile = ctx.getInitParameter("appconfig");
		if (appconfile == null)
		{
			throw new RuntimeException("appconfig is not configured. Please check your web.xml");
		}
		String fileNames[] = appconfile.split("[,]");
		loadFiles(servletContextEvent.getServletContext(), fileNames);
		String smsMessageFileName = ctx.getInitParameter("smsmessagetemplates");
		if (smsMessageFileName == null)
		{
			throw new RuntimeException("smsMessageFileName is not configured. Please check your web.xml");
		}
		loadSMSMessageTemplates(servletContextEvent.getServletContext(), smsMessageFileName);
	}
	
	/**
	 * loads the properties file from resource stream to System property
	 * @param servletContextEvent
	 * @param fileNames
	 */
	private void loadFiles(ServletContext servletContext, String[] fileNames)
	{
		for (String fileName : fileNames)
		{
			logger.info("Loading configuration fileName {} ", fileNames);
			InputStream in = null;
			try {
				in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
				
				Properties prop = new Properties();
				prop.load(in);
				
				Iterator<Object> it = prop.keySet().iterator();
				while(it.hasNext())
				{
					String key = (String) it.next();
					System.setProperty(key, prop.getProperty(key));
				}
			}
			catch (Exception ex)
			{
				logger.error("Error loading configuration fileName {} error {} ", fileNames, ex);
				throw new RuntimeException("Unable to read "+fileName+" during server startup.", ex);
			}
			finally {
				if (in != null)
				{
					try {
						in.close();
					}catch(Exception ex){}
				}
			}

		}
	}
	
	/**
	 * Read all SMS message temapltes and store it in Singleton class SMSMessageTemplates
	 * @param servletContext
	 * @param smsMessageFileName
	 */
	private void loadSMSMessageTemplates(ServletContext servletContext, String smsMessageFileName)
	{
		logger.info("Loading sms message templates fileName {} ", smsMessageFileName);
		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(smsMessageFileName);
			
			Properties prop = new Properties();
			prop.load(in);
			
			Iterator<Object> it = prop.keySet().iterator();
			while(it.hasNext())
			{
				String key = (String) it.next();
				SMSMessageTemplates.getInstance().addSMSTemplate(key, prop.getProperty(key));
			}
		}
		catch (Exception ex)
		{
			logger.info("Error loading sms message templates fileName {} error {} ", smsMessageFileName, ex);
			throw new RuntimeException("Unable to read "+smsMessageFileName+" during server startup.", ex);
		}
		finally {
			if (in != null)
			{
				try {
					in.close();
				}catch(Exception ex){}
			}
		}
	}
}
