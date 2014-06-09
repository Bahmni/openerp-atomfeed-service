package org.bahmni.feed.openerp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TasksMonitoringResponse {
    private Boolean started;
    private String taskClass;
    private Date lastExecutionTime;
    private Date nextExecutionTime;
    private String lastExecutionTimeString;
    private String nextExecutionTimeString;

    TasksMonitoringResponse() {
    }

    public TasksMonitoringResponse(Boolean started, String taskClass, Date lastExecutionTime, Date nextExecutionTime) {
        this.started = started;
        this.taskClass = taskClass;
        this.lastExecutionTime = lastExecutionTime;
        this.nextExecutionTime = nextExecutionTime;
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy 'T' HH:mm:ss");
        this.lastExecutionTimeString = df.format(lastExecutionTime);
        this.nextExecutionTimeString = df.format(nextExecutionTime);
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

    public String getLastExecutionTimeString() {
        return lastExecutionTimeString;
    }

    public void setLastExecutionTimeString(String lastExecutionTimeString) {
        this.lastExecutionTimeString = lastExecutionTimeString;
    }

    public String getNextExecutionTimeString() {
        return nextExecutionTimeString;
    }

    public void setNextExecutionTimeString(String nextExecutionTimeString) {
        this.nextExecutionTimeString = nextExecutionTimeString;
    }
}
