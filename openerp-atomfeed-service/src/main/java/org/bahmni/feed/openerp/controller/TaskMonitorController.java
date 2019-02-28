package org.bahmni.feed.openerp.controller;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.TasksMonitoringResponse;
import org.bahmni.openerp.web.service.CustomerService;
import org.bahmni.openerp.web.service.OpenERPService;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
// TODO : Mujir - this doesnt have any authentication right now. Change nagios when we add basic auth.
public class TaskMonitorController {
    private OpenERPService openERPService;
    private SchedulerFactoryBean schedulerFactoryBean;

    private static Logger logger = Logger.getLogger(TaskMonitorController.class);

    @Autowired
    public TaskMonitorController(SchedulerFactoryBean schedulerFactoryBean, OpenERPService openERPService) {
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.openERPService = openERPService;
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public
    @ResponseBody
    List<TasksMonitoringResponse> taskStatus() {
        List<TasksMonitoringResponse> monitoringResponses = new ArrayList<>();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    Trigger trigger = triggers.get(0);
                    monitoringResponses.add(new TasksMonitoringResponse(scheduler.isStarted(), jobKey.getName(), trigger.getPreviousFireTime(), trigger.getNextFireTime()));
                }
            }
        } catch (SchedulerException e) {
            logger.error(e);
        }
        return monitoringResponses;
    }

    @RequestMapping(value = "/customer", method = RequestMethod.GET, params = {"patientId"})
    @ResponseBody
    public Object[] search(@RequestParam String patientId) {
        return openERPService.findCustomers(patientId);
    }

}
