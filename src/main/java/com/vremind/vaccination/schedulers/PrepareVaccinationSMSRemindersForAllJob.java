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
 *  This Job will prepare the list of Vaccination sms reminders for all baby's who are due for 7 and 1 day
 */
public class PrepareVaccinationSMSRemindersForAllJob extends QuartzJobBean {
	
	private final static Logger logger = LoggerFactory.getLogger(PrepareVaccinationSMSRemindersForAllJob.class);
	
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
			logger.info("PrepareVaccinationSMSRemindersForAllJob starting....");
		}
		int totalAlertsCreated = this.alertDAO.createVaccinationScheduleAlert();
		
		if (logger.isDebugEnabled())
		{
			logger.debug("totalAlertsCreated : {}",totalAlertsCreated);
		}
		
		if (logger.isInfoEnabled())
		{
			logger.info("PrepareVaccinationSMSRemindersForAllJob ended ....");
		}
		
	}

}
