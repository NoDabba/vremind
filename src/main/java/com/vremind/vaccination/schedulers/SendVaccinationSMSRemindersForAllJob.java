/**
 * 
 */
package com.vremind.vaccination.schedulers;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.vremind.vaccination.common.util.WebUtil;
import com.vremind.vaccination.dao.reminder.AlertDAO;
import com.vremind.vaccination.domain.AlertTO;
import com.vremind.vaccination.services.alert.AlertService;

/**
 * @author sdoddi
 * This class will send all vaccination schedule sms message through provider. This will be called once every night at 2:00 AM
 */
public class SendVaccinationSMSRemindersForAllJob extends QuartzJobBean {
	
	private final static Logger logger = LoggerFactory.getLogger(SendVaccinationSMSRemindersForAllJob.class);
	
	private AlertDAO alertDAO;

	private AlertService alertService;
	
	@Autowired
	public void setAlertDAO(
			AlertDAO alertDAO) {
		this.alertDAO = alertDAO;
	}
	
	@Autowired
	public void setAlertService(AlertService alertService) {
		this.alertService = alertService;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		if (logger.isInfoEnabled())
		{
			logger.info("SendVaccinationSMSRemindersForAllJob starting....");
		}
		List<AlertTO> alertTOList = this.alertDAO.findAllAlerts();
		if (logger.isDebugEnabled())
		{
			logger.debug("alertTOList size {} ", (alertTOList != null ? alertTOList.size() : "NULL"));
		}
		
		String scheduledDate = WebUtil.getCurrentDate("yyyy-MM-dd");
		if (alertTOList != null && alertTOList.size() > 0)
		{
			alertService.publishMessage(alertTOList, scheduledDate, "08:00:00", false);
			//TODO change this method. testing only
			//alertService.publishMessage(alertTOList, null, null, false);
		}
		if (logger.isInfoEnabled())
		{
			logger.info("SendVaccinationSMSRemindersForAllJob ended....");
		}
	}
}
