package org.bahmni.feed.openerp.event;

import org.ict4h.atomfeed.client.service.EventWorker;

public class EventWorkerFactory {
    public EventWorker getWorker(String workerName) {
        if(workerName.equals("openerp.service"))
            return new OpenERPServiceEventWorker();
        return new EmptyWorker();
    }
}
