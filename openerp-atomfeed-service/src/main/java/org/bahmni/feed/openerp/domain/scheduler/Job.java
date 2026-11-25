/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.domain.scheduler;

public class Job {
	private int id;
	private String name;
	private String cronStatement;
	private boolean enabled;
	private long startDelay;

	public Job(int id, String name, String cronStatement, long startDelay, boolean enabled) {
		this.id = id;
		this.name = name;
		this.cronStatement = cronStatement;
		this.startDelay = startDelay;
		this.enabled = enabled;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCronStatement() {
		return cronStatement;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public long getStartDelay() {
		return startDelay;
	}
}