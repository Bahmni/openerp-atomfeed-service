package org.bahmni.feed.openerp.client;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.webclients.AnonymousAuthenticator;
import org.bahmni.webclients.ConnectionDetails;
import org.bahmni.webclients.HttpClient;

public class OpenElisWebClient extends AbstractWebClient {

    private static Logger logger = Logger.getLogger(OpenElisWebClient.class);


    public OpenElisWebClient(OpenERPAtomFeedProperties properties) {
        connectionDetails = connectionDetails(properties);
        httpClient = new HttpClient(connectionDetails, new AnonymousAuthenticator(connectionDetails));
    }

    @Override
    protected ConnectionDetails connectionDetails(OpenERPAtomFeedProperties properties) {
        return new ConnectionDetails(properties.getOpenElisURI(), null, null, properties.getConnectionTimeoutInMilliseconds(), properties.getReplyTimeoutInMilliseconds());
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
