package org.bahmni.feed.openerp.job;

import org.quartz.impl.triggers.CronTriggerImpl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

public class JobFilterFactory extends ArrayList {

	public JobFilterFactory(Collection allBeans) {
		super(allBeans);
	}

	public static JobFilterFactory create(Collection<Object> availableBeans) throws IOException, ParseException {
		JobFilter jobFilter = null;
		Collection<CronTriggerImpl> cronTriggers = new ArrayList<>();

		for (Object availableBean : availableBeans) {
			if (availableBean instanceof JobFilter) {
				jobFilter = (JobFilter) availableBean;
			} else {
				cronTriggers.add((CronTriggerImpl) availableBean);
			}
		}
		availableBeans.remove(jobFilter);

		return new JobFilterFactory(jobFilter.filterBeans(cronTriggers));
	}

}
