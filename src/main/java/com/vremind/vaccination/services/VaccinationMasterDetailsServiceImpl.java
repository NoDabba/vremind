/**
 * 
 */
package com.vremind.vaccination.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vremind.vaccination.dao.vacmasterdetails.VaccinationMasterDetailsDAO;
import com.vremind.vaccination.domain.MasterVaccinationTO;

/**
 * @author sdoddi
 *
 */
public class VaccinationMasterDetailsServiceImpl implements VaccinationMasterDetailsService {

	private final static Logger logger = LoggerFactory.getLogger(VaccinationMasterDetailsServiceImpl.class);

	private VaccinationMasterDetailsDAO vaccinationMasterDetailsDAO;
	
	@Autowired
	public void setVaccinationMasterDetailsDAO(VaccinationMasterDetailsDAO vaccinationMasterDetailsDAO) {
		this.vaccinationMasterDetailsDAO = vaccinationMasterDetailsDAO;
	}

	/* (non-Javadoc)
	 * @see com.vremind.vaccination.services.VaccinationDetailsService#getVaccinationDetails()
	 */
	@Override
	public List<MasterVaccinationTO> getVaccinationDetails() {
		
		if (logger.isDebugEnabled())
		{
			logger.debug("Getting the vaccination master details....");
		}
		return vaccinationMasterDetailsDAO.getVaccinationDetails();
	}

	/* (non-Javadoc)
	 * @see com.vremind.vaccination.services.VaccinationDetailsService#saveVaccinationDetails()
	 */
	@Override
	public void saveVaccinationDetails() {
		if (logger.isDebugEnabled())
		{
			logger.debug("Triggering the cache framework to remove vaccination master details from cache");
		}
		vaccinationMasterDetailsDAO.saveVaccinationDetails();
	}

}
