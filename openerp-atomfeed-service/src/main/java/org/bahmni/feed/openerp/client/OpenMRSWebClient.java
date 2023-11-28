package org.bahmni.feed.openerp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.webclients.ConnectionDetails;
import org.bahmni.webclients.HttpClient;
import org.bahmni.webclients.openmrs.OpenMRSLoginAuthenticator;

public class OpenMRSWebClient extends AbstractWebClient {

    private static Logger logger = LoggerFactory.getLogger(OpenMRSWebClient.class);

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
