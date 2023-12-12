package org.bahmni.feed.openerp.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.webclients.ConnectionDetails;
import org.bahmni.webclients.HttpClient;
import org.bahmni.webclients.openmrs.OpenMRSLoginAuthenticator;

public class OpenMRSWebClient extends AbstractWebClient {

    private static Logger logger = LogManager.getLogger(OpenMRSWebClient.class);

    public OpenMRSWebClient(OpenERPAtomFeedProperties properties) {
        connectionDetails = connectionDetails(properties);
        httpClient = new HttpClient(connectionDetails, new OpenMRSLoginAuthenticator(connectionDetails));
    }

    @Override
    protected ConnectionDetails connectionDetails(OpenERPAtomFeedProperties properties) {
        return new ConnectionDetails(properties.getAuthenticationURI(),
                properties.getOpenMRSUser(),
                properties.getOpenMRSPassword(),
                properties.getConnectionTimeoutInMilliseconds(),
                properties.getReplyTimeoutInMilliseconds());
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
