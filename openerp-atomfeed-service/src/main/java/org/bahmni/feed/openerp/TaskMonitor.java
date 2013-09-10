package org.bahmni.feed.openerp;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TaskMonitor {
    private String taskName;
    private Date startTime;
    private Date endTime;

    @Autowired
    public TaskMonitor(String taskName) {
        this.taskName = taskName;
    }

    public void startTask() {
        this.startTime = new Date();
    }

    public void endTask() {
        this.endTime = new Date();
    }

    public List<TasksMonitoringResponse> getStatus() {
        if (startTime == null)
            return Arrays.asList(new TasksMonitoringResponse(false, taskName, null, null));

        boolean hasStarted = startTime != null;
        return Arrays.asList(new TasksMonitoringResponse(hasStarted, taskName, startTime, null));
    }
}

