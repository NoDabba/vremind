/**
 * 
 */
package com.vremind.vaccination.schedulers;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.vremind.vaccination.dao.reminder.AlertDAO;

/**
 * @author sdoddi
 * This job will get all Completed or failed Alerts and moves them to History table. This will be executed only once in a day
 */
public class MoveAlertsToHistoryJob extends QuartzJobBean {
	
	private final static Logger logger = LoggerFactory.getLogger(MoveAlertsToHistoryJob.class);
	
	private AlertDAO alertDAO;


	@Autowired
	public void setAlertDAO(
			AlertDAO alertDAO) {
		this.alertDAO = alertDAO;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		if (logger.isInfoEnabled())
		{
			logger.info("MoveAlertsToHistoryJob starting....");
		}
		int rowsMoved= alertDAO.moveFailedSucessAlertsToHistory();
		if (logger.isDebugEnabled())
		{
			logger.debug("number of rows moved to history {} ", rowsMoved);
		}
		
		if (logger.isInfoEnabled())
		{
			logger.info("MoveAlertsToHistoryJob ended....");
		}
	}

}
