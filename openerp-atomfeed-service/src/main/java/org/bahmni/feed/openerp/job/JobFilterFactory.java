/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.job;

import org.quartz.impl.triggers.CronTriggerImpl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

public class JobFilterFactory extends ArrayList {

	public JobFilterFactory(Collection triggers) {
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
