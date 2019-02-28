package org.bahmni.feed.openerp.job;

import org.quartz.impl.triggers.CronTriggerImpl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

public class JobFilterFactory extends ArrayList {

	JobFilterFactory(Collection triggers) {
		super(triggers);
	}

	public static JobFilterFactory create(JobFilter jobFilter, Collection<Object> availableBeans) throws IOException, ParseException {
		Collection<CronTriggerImpl> cronTriggers = new ArrayList<>();

		for (Object availableBean : availableBeans) {
			cronTriggers.add((CronTriggerImpl) availableBean);
		}

		return new JobFilterFactory(jobFilter.filterBeans(cronTriggers));
	}

}
