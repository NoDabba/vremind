/**
 * 
 */
package com.vremind.vaccination.dao.vacmasterdetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vremind.vaccination.domain.MasterVaccinationTO;

/**
 * @author sdoddi
 * This will load the Vaccination master data to cache. When service is invoking getter method to retrieve 
 * all vaccination master details, spring will check if the object/List available in cache else will make a database
 * call to get vaccination master details.
 * 
 * This class has also provided procedure to remove all objects from cache when save method is called. This will not
 * perform any activities on database but will remove cached object from cache. And any subsequent call on getter
 * the udpated data will be loaded from database back to cache.
 */
public class VaccinationMasterDetailsDAOImpl implements VaccinationMasterDetailsDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(VaccinationMasterDetailsDAOImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	private final String VACCINATION_MASTER_SQL = "select vac_id, short_desc, name, schedule_day, schedule_day_type from vaccination_master";
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	/* (non-Javadoc)
	 * @see com.vremind.vaccination.dao.vacdetails.VaccinationDetailsDAO#getVaccinationDetails()
	 */
	@Override
	@Cacheable("vaccination_master")
	public List<MasterVaccinationTO> getVaccinationDetails() {
		
		List<MasterVaccinationTO> vaccinationTOList = new ArrayList<MasterVaccinationTO>();
		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(VACCINATION_MASTER_SQL);
		for (Map<String, Object> row : rows) {
			
			MasterVaccinationTO vaccinationTO = new MasterVaccinationTO(
					(Integer)row.get("vac_id"), (String) row.get("short_desc"), (String) row.get("name"), (Integer)row.get("schedule_day"), 
					(String)row.get("schedule_day_type"));
			
			vaccinationTOList.add(vaccinationTO);
		}
		
		if (logger.isDebugEnabled())
		{
			logger.debug("Vaccination master details size {} ", vaccinationTOList.size());
		}
		
		return vaccinationTOList;
	}

	@Override
	@CacheEvict(value = "vaccination_master", allEntries = true)
	public void saveVaccinationDetails() {
		// code removed for brevity
		
	}

}
