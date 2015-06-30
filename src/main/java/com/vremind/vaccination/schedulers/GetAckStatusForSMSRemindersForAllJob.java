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

import com.vremind.vaccination.dao.reminder.AlertDAO;
import com.vremind.vaccination.domain.AlertTO;
import com.vremind.vaccination.services.alert.AlertService;

/**
 * @author sdoddi
 * This will get sms message acknowledgments from SMS provider and update the status accordingly to database. 
 * This job will be triggered every one hour from 09 AM till 06 PM
 */
public class GetAckStatusForSMSRemindersForAllJob extends QuartzJobBean {
	
	private final static Logger logger = LoggerFactory.getLogger(GetAckStatusForSMSRemindersForAllJob.class);
	
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
			logger.info("GetAckStatusForSMSRemindersForAllJob starting....");
		}
		List<AlertTO> alertAckTOList = alertDAO.findAllAlertsToCheckAcks();
		if (logger.isDebugEnabled())
		{
			logger.debug("alertAckTOList size {} ", (alertAckTOList != null ? alertAckTOList.size() : "NULL"));
		}
		
		alertService.ackMessage(alertAckTOList);
		if (logger.isInfoEnabled())
		{
			logger.info("GetAckStatusForSMSRemindersForAllJob ended ....");
		}
	}

}