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