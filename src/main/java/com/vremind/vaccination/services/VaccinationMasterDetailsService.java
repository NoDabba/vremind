/**
 * 
 */
package com.vremind.vaccination.services;

import java.util.List;

import com.vremind.vaccination.domain.MasterVaccinationTO;

/**
 * @author sdoddi
 *
 */
public interface VaccinationMasterDetailsService {
	
	public List<MasterVaccinationTO> getVaccinationDetails();
	
	public void saveVaccinationDetails();

}
