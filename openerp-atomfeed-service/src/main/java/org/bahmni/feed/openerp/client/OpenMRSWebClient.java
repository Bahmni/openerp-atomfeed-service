package org.bahmni.feed.openerp.client;

import org.bahmni.feed.openerp.FeedException;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.webclients.WebClient;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticationResponse;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticator;

import java.net.URI;
import java.util.Map;

public class OpenMRSWebClient{

    public static final String JSESSION_ID_KEY = "JSESSIONID";
    private OpenERPAtomFeedProperties atomFeedProperties;

    public OpenMRSWebClient(OpenERPAtomFeedProperties properties) {
        this.atomFeedProperties = properties;
    }

    public OpenMRSAuthenticator authenticator() {
        return new OpenMRSAuthenticator(atomFeedProperties.getAuthenticationURI(), atomFeedProperties.getConnectionTimeoutInMilliseconds(), atomFeedProperties.getReplyTimeoutInMilliseconds());
    }

    public WebClient getWebClient() {
        OpenMRSAuthenticationResponse authenticationResponse = authenticator().authenticate(atomFeedProperties.getOpenMRSUser(),
                atomFeedProperties.getOpenMRSPassword(), ObjectMapperRepository.objectMapper);
        if (!authenticationResponse.isAuthenticated()) throw new FeedException("Failed to authenticate with OpenMRS");
        String sessionIdValue = authenticationResponse.getSessionId();

        return new WebClient(atomFeedProperties.getConnectionTimeoutInMilliseconds(), atomFeedProperties.getReplyTimeoutInMilliseconds(), JSESSION_ID_KEY, sessionIdValue);
    }

    public String get(URI uri, Map<String, String> stringStringHashMap) {
        return getWebClient().get(uri,stringStringHashMap);
    }
}
