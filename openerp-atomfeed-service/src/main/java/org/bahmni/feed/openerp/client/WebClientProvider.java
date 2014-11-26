package org.bahmni.feed.openerp.client;

import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.job.Jobs;

import java.util.HashMap;
import java.util.Map;

public class WebClientProvider {

    private static final String OPENMRS_WEBCLIENT = "openmrs.webclient";
    private static final String OPENELIS_WEBCLIENT = "openelis.webclient";
    private static final String REFDATA_WEBCLIENT = "referencedata.webclient";
    private OpenERPAtomFeedProperties openERPAtomFeedProperties;

    private Map<String,AbstractWebClient> webClients = new HashMap<>();

    public WebClientProvider(OpenERPAtomFeedProperties openERPAtomFeedProperties) {
        this.openERPAtomFeedProperties = openERPAtomFeedProperties;
    }


    public  OpenMRSWebClient getOpenMRSWebClient(){
        OpenMRSWebClient openMRSWebClient = (OpenMRSWebClient) webClients.get(OPENMRS_WEBCLIENT);
        if(openMRSWebClient == null){
            openMRSWebClient = new OpenMRSWebClient(openERPAtomFeedProperties);
            webClients.put(OPENMRS_WEBCLIENT,openMRSWebClient);
        }
        return openMRSWebClient;
    }

    public  ReferenceDataWebClient referenceDataWebClient(){
        ReferenceDataWebClient referenceDataWebClient = (ReferenceDataWebClient) webClients.get(REFDATA_WEBCLIENT);
        if(referenceDataWebClient == null){
            referenceDataWebClient = new ReferenceDataWebClient(openERPAtomFeedProperties);
            webClients.put(REFDATA_WEBCLIENT,referenceDataWebClient);
        }
        return referenceDataWebClient;
    }

    public AbstractWebClient getWebClient(Jobs jobName){
        switch (jobName){
            case CUSTOMER_FEED: return getOpenMRSWebClient() ;
            case DRUG_FEED: return getOpenMRSWebClient() ;
            case SALEORDER_FEED: return getOpenMRSWebClient() ;
            case REFERENCE_DATA_FEED: return referenceDataWebClient() ;
            case OPENELIS_SALEORDER_FEED: return openElisWebClient();
            default: return null;
        }
    }

    public OpenElisWebClient openElisWebClient() {
        OpenElisWebClient openElisWebClient = (OpenElisWebClient) webClients.get(OPENELIS_WEBCLIENT);
        if(openElisWebClient == null){
            openElisWebClient = new OpenElisWebClient(openERPAtomFeedProperties);
            webClients.put(OPENELIS_WEBCLIENT,openElisWebClient);
        }
        return openElisWebClient;
    }
}
