package org.bahmni.feed.openerp.client;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.webclients.ClientCookies;
import org.bahmni.webclients.ConnectionDetails;
import org.bahmni.webclients.HttpClient;
import org.bahmni.webclients.openmrs.OpenMRSLoginAuthenticator;

import java.net.URI;
import java.net.URISyntaxException;

public class OpenMRSWebClient{

    private static HttpClient httpClient;
    private ConnectionDetails connectionDetails;
    private static Logger logger = Logger.getLogger(OpenMRSWebClient.class);

    public OpenMRSWebClient(OpenERPAtomFeedProperties properties) {
        connectionDetails = connectionDetails(properties);
        httpClient = new HttpClient(connectionDetails, new OpenMRSLoginAuthenticator(connectionDetails));
    }

    public String get(URI uri) {
        return httpClient.get(uri);
    }

    private ConnectionDetails connectionDetails(OpenERPAtomFeedProperties properties) {
        return new ConnectionDetails(properties.getAuthenticationURI(),
                properties.getOpenMRSUser(),
                properties.getOpenMRSPassword(),
                properties.getConnectionTimeoutInMilliseconds(),
                properties.getReplyTimeoutInMilliseconds());
    }

    public ClientCookies getCookies() {
        try {
            return httpClient.getCookies(new URI(connectionDetails.getAuthUrl()));
        } catch (URISyntaxException e) {
            logger.error("Unable to get Cookies",e);
        }
        return new ClientCookies();
    }
}
