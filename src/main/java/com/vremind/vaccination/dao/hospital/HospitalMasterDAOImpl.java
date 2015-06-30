/**
 * 
 */
package com.vremind.vaccination.dao.hospital;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vremind.vaccination.domain.Hospital;

/**
 * @author sdoddi
 *
 */
public class HospitalMasterDAOImpl implements HospitalMasterDAO {

	private final static Logger logger = LoggerFactory.getLogger(HospitalMasterDAOImpl.class);

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	@Cacheable("hospital_master")
	public List<Hospital> getAllHospitals() {
		List<Hospital> hospitalList = new ArrayList<Hospital>();
		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList("SELECT hospital_id, hospital_key, hospital_name, hospital_address, logo, status FROM hospital_master");
		for (Map<String, Object> row : rows) {
			
			Hospital hospitalTO = new Hospital(
					(Integer)row.get("hospital_id"), (String) row.get("hospital_key"), (String) row.get("hospital_name"), 
					(String) row.get("hospital_address"), (String) row.get("status"), (String) row.get("logo"));
			
			hospitalList.add(hospitalTO);
		}
		
		if (logger.isDebugEnabled())
		{
			logger.debug("Hospital master details size {} ", hospitalList.size());
		}
		
		return hospitalList;
	}
}
