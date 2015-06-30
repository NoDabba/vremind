/**
 * 
 */
package com.vremind.vaccination.dao.vacmasterdetails;

import java.util.List;

import com.vremind.vaccination.domain.MasterVaccinationTO;

/**
 * @author sdoddi
 *
 */
public interface VaccinationMasterDetailsDAO {

	public List<MasterVaccinationTO> getVaccinationDetails();
	
	public void saveVaccinationDetails();
	
}
