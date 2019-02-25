package org.bahmni.feed.openerp.job;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.FeedException;
import org.bahmni.feed.openerp.Jobs;
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

    public void processFeed(Jobs jobName) {
        try {
            logger.info(String.format("Processing Feed [%s] Job [%s]",jobName.getFeedUri() , jobName));
            initAtomFeedClient(jobName);
            atomFeedClient.processEvents();
        } catch (Exception e) {
            logger.error(String.format("Failed Feed [%s] execution. Job [%s]",jobName.getFeedUri() , jobName), e);
            handleAuthorizationException(e, jobName);
        }
    }

    private void initAtomFeedClient(Jobs jobName) {
        if(atomFeedClient == null){
            atomFeedClient = (AtomFeedClient) atomFeedClientHelper.getAtomFeedClient(jobName);
        }
    }

    public void processFailedEvents(Jobs jobName) {
        try {
            logger.info(String.format("Processing failed events for Feed [%s]",jobName.getFeedUri()));
            initAtomFeedClient(jobName);
            if(atomFeedClient != null){
                atomFeedClient.processFailedEvents();
            }
        } catch (Exception e) {
            logger.error(String.format("Failed Feed [%s] execution. Job [%s]",jobName.getFeedUri() , jobName), e);
            handleAuthorizationException(e, jobName);
        }
    }

    protected void handleAuthorizationException(Throwable e, Jobs jobName) throws FeedException {
        if (e != null &&
                (ExceptionUtils.getStackTrace(e).contains("HTTP response code: 401") || ExceptionUtils.getStackTrace(e).contains("Bad response code of 403"))
           ) {
            atomFeedClient = (AtomFeedClient) atomFeedClientHelper.getAtomFeedClient(jobName);
        }
    }

    private boolean isUnauthorised(Throwable e) {
        return ExceptionUtils.getStackTrace(e).contains("HTTP response code: 401")
                || ExceptionUtils.getStackTrace(e).contains("HTTP response code: 403");
    }
}
