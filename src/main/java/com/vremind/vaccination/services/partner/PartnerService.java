/**
 * 
 */
package com.vremind.vaccination.services.partner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vremind.vaccination.dao.partner.PartnerDAO;
import com.vremind.vaccination.domain.Hospital;
import com.vremind.vaccination.domain.PartnerDetails;
import com.vremind.vaccination.services.HospitalMasterService;

/**
 * @author sdoddi
 *
 */
public class PartnerService {
	
	private final static Logger logger = LoggerFactory.getLogger(PartnerService.class);

	private PartnerDAO partnerDAO;
	private HospitalMasterService hospitalMasterService;

	@Autowired
	public void setPartnerDAO(PartnerDAO partnerDAO) {
		this.partnerDAO = partnerDAO;
	}

	/**
	 * @param hospitalMasterService the hospitalMasterService to set
	 */
	@Autowired
	public void setHospitalMasterService(HospitalMasterService hospitalMasterService) {
		this.hospitalMasterService = hospitalMasterService;
	}

	
	/**
	 * validates the partner credentials.
	 * @param partnerId
	 * @param userId
	 * @param password
	 * @return PartnerUser
	 */
	public PartnerDetails getPartnerUser(String partnerId, String userId, String password) throws PartnerException  {
		
		List<Hospital> hospitalMasterList = null;
		try {
			hospitalMasterList = hospitalMasterService.getHospitalMaster();
		}
		catch (Exception ex) {
			logger.error("Error while getting the Hospital Master data from Service", ex);
			throw new PartnerException("Unable to get Partner Details. Please try later.", ex);
		}
		boolean validPartner = false;
		String logoUrl = null;
		int hospitalId = 0;
		//validate if partner details configured in your system
		if (hospitalMasterList != null) {
			for (Hospital hospital : hospitalMasterList) {
				if (hospital.getKey().equals(partnerId)) {
					validPartner = true;
					logoUrl = hospital.getLogoUrl();
					hospitalId = hospital.getId();
					break;
				}
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("partner ID validation status {}", validPartner);
		}
		
		PartnerDetails user = null;
		if (validPartner) {
			try { 
				user = partnerDAO.getPartnerUserDetails(partnerId, userId, password);
			}
			catch (Exception ex) {
				logger.error("Error while validating the partner credentails", ex);
				throw new PartnerException("Unable to authenticate now. Please try later.", ex);
			}
		}
		
		if (user == null) {
			logger.error("Invalid credentials");
			throw new PartnerException("Invalid credentials. Please check user id or password.");
		}
		
		user.setLogoUrl(logoUrl);
		user.setHospitalId(hospitalId);
		return user;
	}
	
}
