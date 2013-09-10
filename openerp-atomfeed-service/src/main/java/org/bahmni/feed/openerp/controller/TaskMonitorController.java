package org.bahmni.feed.openerp.controller;

import org.bahmni.feed.openerp.TaskMonitor;
import org.bahmni.feed.openerp.TasksMonitoringResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/tasks")
// TODO : Mujir - this doesnt have any authentication right now. Change nagios when we add basic auth.
public class TaskMonitorController {
    private TaskMonitor taskMonitor;

    @Autowired
    public TaskMonitorController(TaskMonitor taskMonitor) {
        this.taskMonitor = taskMonitor;
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<TasksMonitoringResponse> taskStatus() {
        return taskMonitor.getStatus();
    }
}
