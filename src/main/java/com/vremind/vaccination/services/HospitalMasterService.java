/**
 * 
 */
package com.vremind.vaccination.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vremind.vaccination.dao.hospital.HospitalMasterDAO;
import com.vremind.vaccination.domain.Hospital;

/**
 * @author sdoddi
 *
 */
public class HospitalMasterService {

	private final static Logger logger = LoggerFactory.getLogger(HospitalMasterService.class);

	private HospitalMasterDAO hospitalMasterDAO;

	@Autowired
	public void setHospitalMasterDAO(HospitalMasterDAO hospitalMasterDAO) {
		this.hospitalMasterDAO = hospitalMasterDAO;
	}


	/**
	 * gets all hospital master from dao
	 * @return List<Hospital>
	 */
	public List<Hospital> getHospitalMaster() {
		if (logger.isDebugEnabled())
			logger.debug("getting the HospitalMaster data");
		return hospitalMasterDAO.getAllHospitals();
	}
}
