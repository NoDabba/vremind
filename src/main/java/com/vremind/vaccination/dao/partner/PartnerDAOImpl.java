/**
 * 
 */
package com.vremind.vaccination.dao.partner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.vremind.vaccination.domain.PartnerDetails;

/**
 * @author sdoddi
 *
 */
public class PartnerDAOImpl implements PartnerDAO {
	
	private final static Logger logger = LoggerFactory.getLogger(PartnerDAOImpl.class);

	private JdbcTemplate jdbcTemplate;

	/*private final String TEMP_INSERT_SQL = "INSERT INTO temp_babyregister " +
			"(mobileno, babyname, reqdatetime, dob, schedule) VALUES (?, ?, ?, ?, ?)";*/

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}


	/* (non-Javadoc)
	 * @see com.vremind.vaccination.dao.partner.PartnerDAO#validatePartnerCredentials(java.lang.String, java.lang.String)
	 */
	@Override
	public PartnerDetails getPartnerUserDetails(final String hospitalKey, final String userId, final String password) {
		
		final String sql = "SELECT hospital_key, userid, branch_name FROM partner_login WHERE hospital_key = ? and userid = ? and password = PASSWORD(?) and active = 1";
		PartnerDetails user =  jdbcTemplate.query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setString(1, hospitalKey);
				ps.setString(2, userId);
				ps.setString(3, password);
				return ps;
			}
		}, new ResultSetExtractor<PartnerDetails>() {
			@Override
			public PartnerDetails extractData(ResultSet rs) throws SQLException,
			DataAccessException {
				return rs.next() ? new PartnerDetails(rs.getString("hospital_key"), rs.getString("userid"), rs.getString("branch_name")) : null;
			}
		});
		
		if (logger.isDebugEnabled()) {
			logger.debug("Partner Userid = {} is authentication status = {} ", userId,
					(user != null ? "Authentication Sucess" : "Authentication failed"));
		}
		return user;
	}

}
