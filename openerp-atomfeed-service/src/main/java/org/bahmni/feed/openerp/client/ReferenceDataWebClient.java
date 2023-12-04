package org.bahmni.feed.openerp.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.webclients.AnonymousAuthenticator;
import org.bahmni.webclients.ConnectionDetails;
import org.bahmni.webclients.HttpClient;

public class ReferenceDataWebClient extends AbstractWebClient {

    private static Logger logger = LogManager.getLogger(OpenMRSWebClient.class);

    public ReferenceDataWebClient(OpenERPAtomFeedProperties properties) {
        connectionDetails = connectionDetails(properties);
        httpClient = new HttpClient(connectionDetails, new AnonymousAuthenticator(connectionDetails));
    }

    protected ConnectionDetails connectionDetails(OpenERPAtomFeedProperties properties) {
        return new ConnectionDetails(properties.getReferenceDataEndpointURI(),
                null,
                null,
                properties.getConnectionTimeoutInMilliseconds(),
                properties.getReplyTimeoutInMilliseconds());
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }


}
