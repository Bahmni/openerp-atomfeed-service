package org.bahmni.feed.openerp;

import java.util.Date;

public class TasksMonitoringResponse {
    private Boolean started;
    private String taskClass;
    private Date lastExecutionTime;
    private Date nextExecutionTime;

    TasksMonitoringResponse() {
    }

    public TasksMonitoringResponse(Boolean started, String taskClass, Date lastExecutionTime, Date nextExecutionTime) {
        this.started = started;
        this.taskClass = taskClass;
        this.lastExecutionTime = lastExecutionTime;
        this.nextExecutionTime = nextExecutionTime;
    }

    public Boolean getStarted() {
        return started;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public Date getLastExecutionTime() {
        return lastExecutionTime;
    }

    public Date getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public void setLastExecutionTime(Date lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

    public void setNextExecutionTime(Date nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }
}
