/**
 * 
 */
package com.vremind.vaccination.dao.partner;

import com.vremind.vaccination.domain.PartnerDetails;

/**
 * @author sdoddi
 *
 */
public interface PartnerDAO {

	public PartnerDetails getPartnerUserDetails(String hospitalKey, String userId, String password);
	
}
