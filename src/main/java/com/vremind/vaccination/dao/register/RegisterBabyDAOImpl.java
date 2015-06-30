/**
 * 
 */
package com.vremind.vaccination.dao.register;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.vremind.vaccination.common.RegistrationFailedException;
import com.vremind.vaccination.domain.BabyVaccinationScheduleTO;
import com.vremind.vaccination.domain.RequestVaccinationTO;

/**
 * @author sdoddi
 * This class will insert the records in parent_info, child_info and vaccination schedule
 */
public class RegisterBabyDAOImpl implements RegisterBabyDAO {

	private final static Logger logger = LoggerFactory.getLogger(RegisterBabyDAOImpl.class);

	private JdbcTemplate jdbcTemplate;

	/*private final String TEMP_INSERT_SQL = "INSERT INTO temp_babyregister " +
			"(mobileno, babyname, reqdatetime, dob, schedule) VALUES (?, ?, ?, ?, ?)";*/

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Sets the status to 0 in parents, child and vaccination schedule for given mobile, babyname, dob
	 * @param mobileNo
	 * @param babyName
	 * @param dob
	 * @return status
	 */
	@Override
	public int unRegisterBabyForVaccinationReminder(String mobileNo, String babyName, Date dob)
	{
		String updateSql = "update parent_info a, child_info b, vaccination_schedule c set a.status=0, b.status=0, c.status=0 where a.mobile_no = ? and b.name = ? and b.dob = ? " +
						   "and b.parent_id=a.parent_id and c.child_id=b.child_id and a.status = 1 and b.status=1 and c.status=1" ;
		
		Object[] params = { mobileNo, babyName, new java.sql.Date(dob.getTime())};
		
		int[] types = {Types.VARCHAR, Types.VARCHAR, Types.DATE};

		int rows = this.jdbcTemplate.update(updateSql, params, types);

		return rows;
	}
	
	/**
	 * Sets the status to 0 in parents, child and vaccination schedule for given mobile. This might set status to 0 
	 * for all child records
	 * @param mobileNo
	 * @param babyName
	 * @param dob
	 * @return status
	 */
	@Override
	public int unRegisterBabyForVaccinationReminder(String mobileNo)
	{
		String updateSql = "update parent_info a, child_info b, vaccination_schedule c set a.status=0, b.status=0, c.status=0 where a.mobile_no = ?  " +
						   "and b.parent_id=a.parent_id and c.child_id=b.child_id and a.status = 1";
		
		Object[] params = { mobileNo };
		
		int[] types = {Types.VARCHAR};

		int rows = this.jdbcTemplate.update(updateSql, params, types);

		return rows;
	}

	/**
	 * This will insert new record in parent_info if available and child_info for given parameters.
	 * Transaction is set on service layer instead of this method.
	 * @param registerBabyTO
	 * @return status
	 */
	@Override
	public int registerBabyForVaccinationReminder(final RequestVaccinationTO registerBabyTO) {
		if (logger.isDebugEnabled())
		{
			logger.debug("Registering Baby for Vaccination Reminder DAO for {}", registerBabyTO.toString());
		}

		int parentId = findParentIdByMobileNo(registerBabyTO.getMobileNo());

		if (parentId <= 0)
		{
			final String parent_sql = "insert into parent_info(mobile_no, created_datetime, circle, operator, email, hospital_id, father_name, mother_name) " +
					"value (?,?,?,?,?,?,?,?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(
					new PreparedStatementCreator() {  
						public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
							PreparedStatement ps = connection.prepareStatement(parent_sql, new String[] {"parent_id"});

							ps.setString(1, registerBabyTO.getMobileNo());
							ps.setTimestamp(2, new java.sql.Timestamp(registerBabyTO.getRequestedDateTime().getTime()));
							ps.setString(3,  registerBabyTO.getCircle());
							ps.setString(4,  registerBabyTO.getOperatorName());
							if (registerBabyTO.getEmail() != null)
								ps.setString(5, registerBabyTO.getEmail());
							else
								ps.setNull(5, Types.VARCHAR);
							if (registerBabyTO.getHospitalId() > 0)
								ps.setInt(6, registerBabyTO.getHospitalId());
							else
								ps.setNull(6, Types.INTEGER);	
							if (registerBabyTO.getFatherName() != null)
								ps.setString(7, registerBabyTO.getFatherName());
							else
								ps.setNull(7, Types.VARCHAR);
							if (registerBabyTO.getMotherName() != null)
								ps.setString(8, registerBabyTO.getMotherName());
							else
								ps.setNull(8, Types.VARCHAR);
							return ps;
						}
					},
					keyHolder);
			if (logger.isDebugEnabled())
			{
				logger.debug("Parent keyHolder {}", (keyHolder != null ? keyHolder.getKey() : "null"));
			}
			parentId =  keyHolder.getKey().intValue();

			if (parentId <=0 )
			{
				if (logger.isErrorEnabled())
				{
					logger.error("Unable to create Parent Info for {}, dob {}", new Object[]{registerBabyTO.getMobileNo(), registerBabyTO.getDob()});
				}
				throw new RegistrationFailedException("1000");
			}
		}
		else
		{
			
			//check the baby details already exists
			final String sql = "SELECT COUNT(*) as total FROM child_info where parent_id = ? and name = ? and dob = ?";
			final int parentIdTemp = parentId;
			int total = jdbcTemplate.query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql);
					ps.setInt(1, parentIdTemp);
					ps.setString(2, registerBabyTO.getBabyName().toLowerCase());
					ps.setDate(3, new java.sql.Date(registerBabyTO.getDob().getTime()));
					return ps;
				}
			}, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException,
				DataAccessException {
					return rs.next() ? rs.getInt("total") : -1;
				}
			});
			
			if (total >= 1)
			{
				throw new RegistrationFailedException("1001");
			}
		}
	

		final String child_sql = "insert into child_info(parent_id, name, dob, created_datetime) value (?,?,?,?)";
		final int tempParentId = parentId;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(child_sql, new String[] {"child_id"});
						ps.setInt(1, tempParentId);
						ps.setString(2, registerBabyTO.getBabyName() != null ? registerBabyTO.getBabyName().toLowerCase() : "#baby#");
						ps.setDate(3, new java.sql.Date(registerBabyTO.getDob().getTime()));
						ps.setTimestamp(4, new java.sql.Timestamp(registerBabyTO.getRequestedDateTime().getTime()));
						return ps;
					}
				},
				keyHolder);

		if (logger.isDebugEnabled())
		{
			logger.debug("Child keyHolder {}", (keyHolder != null ? keyHolder.getKey() : "null"));
		}

		return keyHolder.getKey().intValue();
	}

	/**
	 * Helper method to get the Parent Id for given mobile number 
	 * @param mobileNo
	 * @return int
	 */
	private int findParentIdByMobileNo(final String mobileNo)
	{
		final String sql = "SELECT parent_id FROM parent_info WHERE mobile_no = ?";
		return jdbcTemplate.query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, mobileNo);
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException,
			DataAccessException {
				return rs.next() ? rs.getInt("parent_id") : -1;
			}
		});
	}


	/**
	 * This method will create baby vaccination schedule for given baby
	 */
	@Override
	public int[] createBabyVaccinationSchedule(final 
			List<BabyVaccinationScheduleTO> vaccinationTOList) {

		String sql = "INSERT INTO vaccination_schedule(child_id, vac_id, scheduled_date, created_datetime, status) " +
				"VALUES (?, ?, ?, ?, ?)";

		return this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				logger.debug("from create schedule {} ",i);

				BabyVaccinationScheduleTO vaccinationTO = vaccinationTOList.get(i);
				ps.setInt(1, vaccinationTO.getChildId());
				ps.setInt(2, vaccinationTO.getVaccinationId());
				ps.setDate(3,  new java.sql.Date(vaccinationTO.getScheduledDate().getTime()));
				ps.setTimestamp(4, new java.sql.Timestamp(vaccinationTO.getCreatedDate().getTime()));
				ps.setInt(5, vaccinationTO.getStatus());
			}

			@Override
			public int getBatchSize() {
				return vaccinationTOList.size();
			}
		});
	}

}
