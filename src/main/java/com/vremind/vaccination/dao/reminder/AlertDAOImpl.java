/**
 * 
 */
package com.vremind.vaccination.dao.reminder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import com.vremind.vaccination.common.util.SMSMessageTemplates;
import com.vremind.vaccination.common.util.WebUtil;
import com.vremind.vaccination.domain.AlertScheduleTO;
import com.vremind.vaccination.domain.AlertTO;

/**
 * @author sdoddi
 * this is main class to create, get all alerts, updates the statues with acks etc.. in vaccination_Alerts table
 */
public class AlertDAOImpl implements AlertDAO {

	private final static Logger logger = LoggerFactory.getLogger(AlertDAOImpl.class);

	private JdbcTemplate jdbcTemplate;

	private String vacSecheduleSQL;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

	@Autowired
	public void setVacSecheduleSQL(String vacSecheduleSQL) {
		this.vacSecheduleSQL = vacSecheduleSQL;
	}

	/**
	 * This will create all eligible vaccination alerts in vacconation_alerts table and also sets the status in vaccination_schedule table to 2
	 * @return number of alerts created
	 */
	@Override
	@Transactional
	public int createVaccinationScheduleAlert() {

		final String sql = vacSecheduleSQL;

		List<AlertScheduleTO>  alertTOList = this.jdbcTemplate.query(sql, new RowMapper<AlertScheduleTO>() {
			@Override
			public AlertScheduleTO mapRow(ResultSet rs, int arg1) throws SQLException {
				AlertScheduleTO alertTO = new AlertScheduleTO(rs.getInt("schedule_id"), rs.getInt("vac_id"), rs.getString("mobile_no"), 
						rs.getString("name"), rs.getString("mother_name"), rs.getString("father_name"), rs.getString("short_desc"), rs.getString("frmt_scheduled_date"));
				return alertTO;
			}
		});

		int count = alertTOList != null ? alertTOList.size() : 0;

		logger.info("Total Alerts to be created : {}", count);

		if (count > 0)
		{

			final String smsMessage =  SMSMessageTemplates.getInstance().getSMSTemplate("vremind.dob.schedule.message");
			final String noBabynameSmsMessage =  SMSMessageTemplates.getInstance().getSMSTemplate("vremind.dob.schedule.nobabyname.message");
			final String noBabynameSmsMessagePlain =  SMSMessageTemplates.getInstance().getSMSTemplate("vremind.dob.schedule.nobabyname.message.plain");
			final Date currentDate = new Date();
			
			final List<AlertScheduleTO>  alertTOList2 = new ArrayList<AlertScheduleTO>(alertTOList);
			final String insert_sql = "insert into vaccination_alerts (vac_id, mobile_no, message) values (?,?,?) ";
			int vaccAlertsStatus[] = this.jdbcTemplate.batchUpdate(insert_sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					AlertScheduleTO alertTO = alertTOList2.get(i);
					boolean isBabyNameExists = true;
					String babyName = alertTO.getBabyName();
					
					if ("#baby#".equals(babyName)) {
						isBabyNameExists = false;
					}
					String message = null;
					if (isBabyNameExists) {
						String babyname = WebUtil.truncateLeadingChars(alertTO.getBabyName(), WebUtil.BABY_NAME_MAX_LENGTH);
						message = String.format(smsMessage, babyname, alertTO.getVacShortCode(), alertTO.getScheduleDate());
					}
					else
					{
						if (alertTO.getScheduleDate() != null)
						{
							Date scheduleDate = WebUtil.getDateFromString(alertTO.getScheduleDate(), "dd-MM-yyyy");
							if (scheduleDate != null) {
								int daysDifference = WebUtil.daysBetween(currentDate, scheduleDate);
								if (daysDifference > 0) {
									String motherName = WebUtil.truncateLeadingChars(alertTO.getMotherName(), WebUtil.BABY_NAME_MAX_LENGTH);
									message = String.format(noBabynameSmsMessagePlain, motherName);
								}
							}
						}
						if (message == null) {
							String motherName = WebUtil.truncateLeadingChars(alertTO.getMotherName(), WebUtil.BABY_NAME_MAX_LENGTH);
							message = String.format(noBabynameSmsMessage, motherName, alertTO.getVacShortCode(), alertTO.getScheduleDate());
						}
					}
					ps.setInt(1, alertTO.getVacId());
					ps.setString(2, alertTO.getMobileNo());
					ps.setString(3, message);
				}

				@Override
				public int getBatchSize() {
					return alertTOList2.size();
				}
			});

			logger.info("Total Alerts to created : {}", (vaccAlertsStatus != null ? vaccAlertsStatus.length : -1));

			if (vaccAlertsStatus != null && vaccAlertsStatus.length > 0)
			{
				final String update_sql = "update vaccination_schedule set status = 2 where schedule_id = ?";
				int updateVaccSchedule[] = this.jdbcTemplate.batchUpdate(update_sql, new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						AlertScheduleTO alertTO = alertTOList2.get(i);
						ps.setInt(1, alertTO.getScheduleId());
					}

					@Override
					public int getBatchSize() {
						return alertTOList2.size();
					}
				});

				logger.info("Total Vaccination schedules udpated : {}", (updateVaccSchedule != null ? updateVaccSchedule.length : -1));

			}
		}

		return count;
	}
	
	/**
	 * helper method to create the alert in vaccination_alert table, this is mostly called during the registration flow in error/(un-)registration success flows.
	 * @param alertTO
	 * @return status of inserted record
	 */
	@Override
	public int createSingleAlert(final AlertTO alertTO)
	{
		final String sql = "insert into vaccination_alerts (vac_id, mobile_no, message) values (?,?,?) ";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(sql, new String[] {"alert_id"});
						ps.setInt(1, alertTO.getVaccinationScheduleId());
						ps.setString(2, alertTO.getMobileNo());
						ps.setString(3, alertTO.getMessage());
						return ps;
					}
				},
				keyHolder);
		if (logger.isDebugEnabled())
		{
			logger.debug("keyHolder {}", (keyHolder != null ? keyHolder.getKey() : "null"));
		}
		return keyHolder.getKey().intValue();
	}

	/**
	 * Gets the all alerts which are eliable to send to SMS provider
	 * @return List<AlertTO>
	 */
	@Override
	public List<AlertTO> findAllAlerts()
	{
		String sql = "select alert_id, vac_id, mobile_no, message from vaccination_alerts where status = 0";
		List<AlertTO>  alertTOList = this.jdbcTemplate.query(sql, new ReminderMapper());

		if (logger.isDebugEnabled())
		{
			logger.debug("Size of alerts due {} ", (alertTOList != null ? alertTOList.size()+"" : "NULL"));
		}

		return alertTOList;
	}

	/**
	 * this will get all alerts which are sent but not acknowledged
	 * @return List<AlertTO>
	 */
	@Override
	public List<AlertTO> findAllAlertsToCheckAcks()
	{
		String sql = "select alert_id, vac_id, mobile_no, message, ack_id, retry_count from vaccination_alerts where status = 1 and ack_datetime is null";
		List<AlertTO>  alertTOList = this.jdbcTemplate.query(sql, new RowMapper<AlertTO>() {
			@Override
			public AlertTO mapRow(ResultSet rs, int arg1) throws SQLException {
				AlertTO alertTO = new AlertTO();
				alertTO.setAckId(rs.getString("ack_id"));
				alertTO.setSno(rs.getInt("alert_id"));
				alertTO.setMessage(rs.getString("message"));
				alertTO.setMobileNo(rs.getString("mobile_no"));
				alertTO.setRetry(rs.getInt("retry_count"));
				return alertTO;
			}
		});

		if (logger.isDebugEnabled())
		{
			logger.debug("Size of alerts due {} ", (alertTOList != null ? alertTOList.size()+"" : "NULL"));
		}
		return alertTOList;
	}

	
	/**
	 * Helper class to read map the database columns to Alerts TO. Used in only one scenario
	 * @author sdoddi
	 *
	 */
	private static final class ReminderMapper implements RowMapper<AlertTO> 
	{
		@Override
		public AlertTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new AlertTO(rs.getInt("alert_id"), rs.getInt("vac_id"), rs.getString("mobile_no"), rs.getString("message"));
		}
	}

	/**
	 * This method will update the ack id, ack date time, status, retry count ect.. in vaccination_alerts table
	 * @param alertTOList
	 * @param ackDateUpdate
	 * @return int[]
	 */
	@Override
	@Transactional
	public int[] updateAllAlertAckIds(final List<AlertTO>  alertTOList, final boolean ackDateUpdate) {

		String vacAlertStatusUpdateSQL = null;
		if (ackDateUpdate)
		{
			vacAlertStatusUpdateSQL = "update vaccination_alerts set status = ?, retry_count = ?, ack_id = ? , ack_datetime = ? where alert_id = ?";
			int vaccAlertsStatus[] = this.jdbcTemplate.batchUpdate(vacAlertStatusUpdateSQL, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					AlertTO alertTO = alertTOList.get(i);
					ps.setInt(1, alertTO.getStatus());
					ps.setInt(2, alertTO.getRetry());
					ps.setString(3, alertTO.getAckId());
					ps.setTimestamp(4, (alertTO.getAckTimeStamp() != null ? new java.sql.Timestamp(alertTO.getAckTimeStamp().getTime()) : null));
					ps.setInt(5, alertTO.getSno());
				}

				@Override
				public int getBatchSize() {
					return alertTOList.size();
				}
			});

			if (logger.isDebugEnabled())
			{
				logger.debug("Ack Vaccination Alerts update stauts {} ", vaccAlertsStatus);
			}

			return vaccAlertsStatus;
		}
		else
		{
			vacAlertStatusUpdateSQL = "update vaccination_alerts set status = 1, retry_count = retry_count + 1, sent_datetime = ?, ack_id = ? where alert_id = ?";
			int vaccAlertsStatus[] = this.jdbcTemplate.batchUpdate(vacAlertStatusUpdateSQL, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					AlertTO alertTO = alertTOList.get(i);
					ps.setTimestamp(1, new java.sql.Timestamp(alertTO.getSentTimeStamp().getTime()));
					ps.setString(2, alertTO.getAckId());
					ps.setInt(3, alertTO.getSno());
				}

				@Override
				public int getBatchSize() {
					return alertTOList.size();
				}
			});

			if (logger.isDebugEnabled())
			{
				logger.debug("Vaccination Alerts update stauts {} ", vaccAlertsStatus);
			}

			return vaccAlertsStatus;
		}
	}

	/**
	 * This method will move all failed (status as -1) or failed (status = 1 and ack date != null) to history table
	 * @return number of records updated
	 */
	@Override
	@Transactional
	public int moveFailedSucessAlertsToHistory() {
		String sql = "insert into vaccination_alerts_history select * from vaccination_alerts where status = -1 or (status = 1 and ack_datetime is not null)";
		int rowsAdded = this.jdbcTemplate.update(sql);
		if (rowsAdded > 0)
		{
			sql = "delete from vaccination_alerts where status = -1 or (status = 1 and ack_datetime is not null)";
			rowsAdded = this.jdbcTemplate.update(sql);
		}
		return rowsAdded;
	}
}
