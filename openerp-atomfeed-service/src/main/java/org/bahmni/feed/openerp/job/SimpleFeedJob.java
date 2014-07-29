package org.bahmni.feed.openerp.job;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.FeedException;
import org.bahmni.feed.openerp.client.AtomFeedClientHelper;
import org.ict4h.atomfeed.client.service.AtomFeedClient;

public class SimpleFeedJob {
    private static Logger logger = Logger.getLogger(SimpleFeedJob.class);

    private AtomFeedClientHelper atomFeedClientHelper;
    private AtomFeedClient atomFeedClient;

    //@Autowired
    public SimpleFeedJob(
            AtomFeedClientHelper atomFeedClientHelper) throws com.sun.syndication.io.FeedException {
        this.atomFeedClientHelper = atomFeedClientHelper;
    }

    public void processFeed(String feedName, Jobs jobName) {
        try {
            logger.info("Processing " + feedName + ". ");
            initAtomFeedClient(feedName, jobName);
            atomFeedClient.processEvents();
        } catch (Exception e) {
            logger.error("failed customer feed execution ", e);
            handleAuthorizationException(e, feedName, jobName);
        }
    }

    private void initAtomFeedClient(String feedName, Jobs jobName) {
        if(atomFeedClient == null){
            atomFeedClient = (AtomFeedClient) atomFeedClientHelper.getAtomFeedClient(feedName, jobName);
        }
    }

    public void processFailedEvents(String feedName, Jobs jobName) {
        try {
            logger.info("Processing failed events for Customer Feed");
            initAtomFeedClient(feedName, jobName);
            if(atomFeedClient != null){
                atomFeedClient.processFailedEvents();
            }
        } catch (Exception e) {
            logger.error("failed customer feed execution ", e);
            handleAuthorizationException(e, feedName, jobName);
        }
    }

    protected void handleAuthorizationException(Throwable e, String feedName, Jobs jobName) throws FeedException {
        if (e != null && isUnauthorised(e)) {
            atomFeedClient = (AtomFeedClient) atomFeedClientHelper.getAtomFeedClient(feedName, jobName);
        }
    }

    private boolean isUnauthorised(Throwable e) {
        return ExceptionUtils.getStackTrace(e).contains("HTTP response code: 401")
                || ExceptionUtils.getStackTrace(e).contains("HTTP response code: 403");
    }
}
