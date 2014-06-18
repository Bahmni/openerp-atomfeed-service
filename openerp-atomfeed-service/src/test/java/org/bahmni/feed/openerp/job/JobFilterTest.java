package org.bahmni.feed.openerp.job;


import org.bahmni.feed.openerp.dao.JobDao;
import org.bahmni.feed.openerp.domain.scheduler.Job;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobFilterTest {
	public static final String DEFAULT_CRON_EXPRESSION = "0/1 * * * * ?";
	private final int startDelay = 60000;

	@Mock
	private JobDao jobDao;

	@Before
	public void setUp() throws Exception {
		initMocks(this);
	}

	@Test
	public void filter_Jobs_That_Are_Disabled_In_Database() throws IOException, ParseException {
		String jobName = "customerFeedJobTrigger";
		boolean enabled = false;

		List<Job> jobsInDB= new ArrayList<>();
		jobsInDB.add(new Job(1, jobName, "0/1 * * * * ?", startDelay, enabled));

		Collection<CronTriggerImpl> beansInConfig = new ArrayList<>();
		CronTriggerImpl cronBean = new CronTriggerImpl();
		cronBean.setName(jobName);
		cronBean.setCronExpression(DEFAULT_CRON_EXPRESSION);
		beansInConfig.add(cronBean);

		when(jobDao.getAllJobs()).thenReturn(jobsInDB);
		JobFilter jobFilter = new JobFilter();
		jobFilter.setJobDao(jobDao);

		Collection filteredJobs = jobFilter.filterBeans(beansInConfig);

		assertThat(filteredJobs.size(), is(0));
	}

	@Test
	public void overwrite_Cron_Expression_When_Job_Is_Enabled_In_Db() throws IOException, ParseException {
		String jobName = "customerFeedJobTrigger";
		boolean enabled = true;

		List<Job> jobsInDB= new ArrayList<>();
		String cronExpressionFromDB = "0/1 * * * * ?";
		jobsInDB.add(new Job(1, jobName, cronExpressionFromDB, startDelay, enabled));

		Collection<CronTriggerImpl> beansInConfig = new ArrayList<>();
		CronTriggerImpl cronBean = new CronTriggerImpl();
		cronBean.setName(jobName);
		cronBean.setCronExpression(DEFAULT_CRON_EXPRESSION);
		beansInConfig.add(cronBean);

		when(jobDao.getAllJobs()).thenReturn(jobsInDB);
		JobFilter jobFilter = new JobFilter();
		jobFilter.setJobDao(jobDao);

		List<CronTriggerImpl> filteredJobs = (List<CronTriggerImpl>) jobFilter.filterBeans(beansInConfig);

		assertThat(filteredJobs.size(), is(1));
		assertThat((filteredJobs.get(0)).getName(),is("customerFeedJobTrigger"));
		assertThat((filteredJobs.get(0)).getCronExpression(), is(cronExpressionFromDB));
	}

	@Test
	public void no_Jobs_In_Database_Configures_Jobs_As_Per_Config() throws Exception {
		String jobName = "customerFeedJobTrigger";

		Collection<CronTriggerImpl> beansInConfig = new ArrayList<>();
		CronTriggerImpl cronBean = new CronTriggerImpl();
		cronBean.setName(jobName);
		cronBean.setCronExpression(DEFAULT_CRON_EXPRESSION);
		beansInConfig.add(cronBean);

		List<Job> noJobsInDB = new ArrayList<>();
		when(jobDao.getAllJobs()).thenReturn(noJobsInDB);

		JobFilter jobFilter = new JobFilter();
		jobFilter.setJobDao(jobDao);

		List<CronTriggerImpl> filteredJobs = (List) jobFilter.filterBeans(beansInConfig);

		assertThat(filteredJobs.size(), is(1));
		assertThat((filteredJobs.get(0)).getName(),is("customerFeedJobTrigger"));
		assertThat((filteredJobs.get(0)).getCronExpression(), is(DEFAULT_CRON_EXPRESSION));
	}

	@Test
	public void no_Jobs_In_Config_Does_Not_Configure_Jobs() throws Exception {
		String jobName = "customerFeedJobTrigger";
		boolean enabled = false;

		List<Job> jobsInDB= new ArrayList<>();
		jobsInDB.add(new Job(1, jobName, "0/1 * * * * ?", startDelay, enabled));

		Collection<CronTriggerImpl> beansInConfig = new ArrayList<>();

		when(jobDao.getAllJobs()).thenReturn(jobsInDB);
		JobFilter jobFilter = new JobFilter();
		jobFilter.setJobDao(jobDao);

		Collection filteredJobs = jobFilter.filterBeans(beansInConfig);

		assertThat(filteredJobs.size(), is(0));
	}
}




