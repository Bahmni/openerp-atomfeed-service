package org.bahmni.feed.openerp.job;

import org.bahmni.feed.openerp.dao.JobDao;
import org.bahmni.feed.openerp.domain.scheduler.Job;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class JobFilter {

	private List<Job> jobs;

	@Autowired
	private JobDao jobDao;

	public JobFilter() throws IOException {
	}

	public Collection filterBeans(Collection<CronTriggerImpl> availableBeans) throws IOException, ParseException {
		jobs = jobDao.getAllJobs();
		if (jobs == null || jobs.isEmpty()) return availableBeans;

		List<CronTriggerImpl> requiredBeans = new ArrayList<>();

		for (CronTriggerImpl availableBean : availableBeans) {
			Job matchingJobInDb = getJobDefinedInDb(jobs, availableBean);
			if (matchingJobInDb != null) {
				if (matchingJobInDb.isEnabled()) {
					availableBean.setCronExpression(matchingJobInDb.getCronStatement());
					availableBean.setStartTime(new Date(System.currentTimeMillis() + matchingJobInDb.getStartDelay()));

					requiredBeans.add(availableBean);
				}
			} else {
				requiredBeans.add(availableBean);
			}
		}

		return requiredBeans;
	}

	private Job getJobDefinedInDb(List<Job> jobsInDb, CronTriggerImpl availableBean) {
		for (Job jobInDb : jobsInDb) {
			if (availableBean.getName().equals(jobInDb.getName())) {
				return jobInDb;
			}
		}
		return null;
	}

	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	public JobDao getJobDao() {
		return jobDao;
	}
}