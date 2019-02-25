package org.bahmni.feed.openerp;

import org.apache.log4j.Logger;
import org.bahmni.openerp.web.OpenERPProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class OpenERPAtomFeedProperties implements OpenERPProperties {

    private static Logger logger = Logger.getLogger(OpenERPAtomFeedProperties.class);

    @Value("${chunking.strategy}")
    private String chunkingStrategy;

    @Value("${scheduler.initial.delay:60000}")
    private String schedulerInitialDelay;

    public String getSchedulerInitialDelay() {
        return schedulerInitialDelay;
    }


    /**
     * @deprecated replaced by {@link #getFeedUriForJob(Jobs)} ()}
     */
    @Deprecated
    public String getFeedUri(String feedname) {
        throw new UnsupportedOperationException("This method is no longer used. Please call getFeedUriForJob() instead");
    }


    @Value("${customer.feed.generator.uri}")
    private String customFeedUri;


    @Value("${openelis.saleorder.feed.generator.uri}")
    private String elisSaleOrderFeedUri;

    @Value("${drug.feed.generator.uri}")
    private String drugFeedUri;

    @Value("${lab.feed.generator.uri}")
    private String labFeedUri;


    @Value("${saleorder.feed.generator.uri}")
    private String saleOrderFeed;

    public String getFeedUriForJob(Jobs feedJob) {
        switch (feedJob){
            case CUSTOMER_FEED: return customFeedUri;
            case SALEORDER_FEED: return saleOrderFeed;
            case OPENELIS_SALEORDER_FEED: return elisSaleOrderFeedUri;
            case DRUG_FEED: return drugFeedUri;
            case LAB_FEED: return labFeedUri;
        }
        throw new RuntimeException("Can not identify feed URI for requested Job.");
    }

    @Value("${openerp.host}")
    private String openErpHost;

    @Override
    public String getHost() {
        return openErpHost;
    }

    @Value("${openerp.port}")
    private String openErpPort;

    @Override
    public int getPort() {
        return Integer.parseInt(openErpPort);
    }

    @Value("${openerp.database}")
    private String openErpDatabase;

    @Override
    public String getDatabase() {
        return openErpDatabase;
    }

    @Value("${openerp.user}")
    private String openErpUser;

    @Override
    public String getUser() {
        return openErpUser;
    }

    @Value("${openerp.password}")
    private String openErpPwd;

    @Override
    public String getPassword() {
        return openErpPwd;
    }

    @Value("${openerp.connectionTimeoutInMilliseconds}")
    private String openErpConTimeOut;

    @Override
    public int getConnectionTimeoutInMilliseconds() {
        return Integer.parseInt(openErpConTimeOut);
    }

    @Value("${openerp.replyTimeoutInMilliseconds}")
    private String openErpReplyTimeOut;

    @Override
    public int getReplyTimeoutInMilliseconds() {
        return Integer.parseInt(openErpReplyTimeOut);
    }

    @Value("${openerp.maxFailedEvents}")
    private String openErpMaxFailedEvents;

    public int getMaxFailedEvents() {
        return Integer.parseInt(openErpMaxFailedEvents);
    }

    @Value("${openmrs.auth.uri}")
    private String openmrsAuthUri;

    public String getAuthenticationURI() {
        return openmrsAuthUri;
    }

    @Value("${openelis.uri}")
    private String openElisUri;

    public String getOpenElisURI() {
        return openElisUri;
    }

    @Value("${openelis.user}")
    private String openElisUser;

    public String getOpenElisUser() {
        return openElisUser;
    }

    @Value("${openelis.password}")
    private String openElisPwd;

    public String getOpenElisPassword() {
        return openElisPwd;
    }

    @Value("${openmrs.user}")
    private String openmrsUser;

    public String getOpenMRSUser() {
        return openmrsUser;
    }

    @Value("${openmrs.password}")
    private String openmrsPwd;

    public String getOpenMRSPassword() {
        return openmrsPwd;
    }

    @Value("${referencedata.endpoint}")
    private String refDataEndPt;

    public String getReferenceDataEndpointURI(){
        return refDataEndPt;
    }


    @PostConstruct
    private void debug() {
        logger.debug("**************** DEBUG OpenERPAtomFeedProperties ************************ ");
        HashMap<String, String> properties = getInfo();
        for (String s : properties.keySet()) {
            logger.debug(String.format("%s=%s",s, properties.get(s)));

        }
        logger.debug("**************** DEBUG OpenERPAtomFeedProperties ************************ ");
    }



    private HashMap<String, String> getInfo() {
        HashMap<String, String> values = new HashMap<>();
        values.put("chunking.strategy",chunkingStrategy );
        values.put("scheduler.initial.delay", schedulerInitialDelay);
        values.put("customer.feed.generator.uri",customFeedUri );
        values.put("openelis.saleorder.feed.generator.uri",elisSaleOrderFeedUri );
        values.put("drug.feed.generator.uri",drugFeedUri );
        values.put("lab.feed.generator.uri",labFeedUri );
        values.put("saleorder.feed.generator.uri",saleOrderFeed );
        values.put("openerp.host",openErpHost );
        values.put("openerp.port",openErpPort );
        values.put("openerp.database",openErpDatabase );
        values.put("openerp.user",openErpUser );
        values.put("openerp.connectionTimeoutInMilliseconds",openErpConTimeOut );
        values.put("openerp.replyTimeoutInMilliseconds",openErpReplyTimeOut );
        values.put("openerp.maxFailedEvents",openErpMaxFailedEvents );
        values.put("openmrs.auth.uri",openmrsAuthUri );
        values.put("openelis.uri",openElisUri );
        values.put("openelis.user",openElisUser );
        values.put("openmrs.user",openmrsUser );
        values.put("referencedata.endpoint",refDataEndPt);
        return values;
    }


}
