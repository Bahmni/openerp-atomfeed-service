package org.bahmni.feed.openerp.dao;

import org.bahmni.feed.openerp.domain.scheduler.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JobDao {
	@Autowired
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public List<Job> getAllJobs() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				return connection.prepareStatement("select * from quartz_cron_scheduler");
			}
		}, new JobRowMapper());
	}

}

class JobRowMapper implements RowMapper<Job> {
	@Override
	public Job mapRow(ResultSet resultSet, int i) throws SQLException {
		return new JobExtractor().extractData(resultSet);
	}
}

class JobExtractor implements ResultSetExtractor<Job> {
	@Override
	public Job extractData(ResultSet rs) throws SQLException {
		return new Job(rs.getInt("id"), rs.getString("name"), rs.getString("cron_statement"), rs.getLong("start_delay"), rs.getBoolean("enabled"));
	}

}

