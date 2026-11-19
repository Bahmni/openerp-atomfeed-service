package org.bahmni.feed.openerp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.webclients.Authenticator;
import org.bahmni.webclients.ConnectionDetails;
import org.bahmni.webclients.HttpClient;
import org.bahmni.webclients.WebClientsException;
import org.bahmni.webclients.OAuth2ConnectionDetails;
import org.bahmni.webclients.openmrs.OpenMRSLoginAuthenticator;
import org.bahmni.webclients.openmrs.OpenMRSOAuthLoginAuthenticator;

import java.util.Properties;

public class OpenMRSWebClient extends AbstractWebClient {

    private static Logger logger = LoggerFactory.getLogger(OpenMRSWebClient.class);

    public OpenMRSWebClient(OpenERPAtomFeedProperties properties) {
        connectionDetails = connectionDetails(properties);
        httpClient = new HttpClient(connectionDetails, getauthenticator(connectionDetails, properties.getOpenMRSOAuthProperties()));
    }

    private static Authenticator getauthenticator(ConnectionDetails connectionDetails, Properties oauth) {
        if (!isOAuth2Enabled(oauth)) {
            return new OpenMRSLoginAuthenticator(connectionDetails);
        }
        return createOAuth2Authenticator(connectionDetails, oauth);
    }

    private static boolean isOAuth2Enabled(Properties oauth) {
        return "true".equalsIgnoreCase(trimmedOrNull(oauth, "oauth2.enabled"));
    }

    private static Authenticator createOAuth2Authenticator(ConnectionDetails connectionDetails, Properties oauth) {
        String tokenUrl = trimmedOrNull(oauth, "openmrs.oauth.token.url");
        String clientId = trimmedOrNull(oauth, "openmrs.oauth.client.id");
        String clientSecret = trimmedOrNull(oauth, "openmrs.oauth.client.secret");
        validateOAuth2Settings(tokenUrl, clientId, clientSecret);
        OAuth2ConnectionDetails Openmrsoauth2 = new OAuth2ConnectionDetails(connectionDetails.getAuthUrl(), tokenUrl,
                clientId, clientSecret, null, connectionDetails.getConnectionTimeout(),
                connectionDetails.getReadTimeout(), OAuth2ConnectionDetails.DEFAULT_EXPIRY_SKEW_SECONDS);
        return new OpenMRSOAuthLoginAuthenticator(Openmrsoauth2);
    }

    private static void validateOAuth2Settings(String tokenUrl, String clientId, String clientSecret) {
        if (tokenUrl == null || clientId == null || clientSecret == null) {
            throw new WebClientsException("oauth2.enabled is true but openmrs.oauth.token.url, openmrs.oauth.client.id "
                    + "and openmrs.oauth.client.secret must all be set");
        }
    }

    private static String trimmedOrNull(Properties properties, String key) {
        String value = properties.getProperty(key);
        return value == null || value.trim().isEmpty() ? null : value.trim();
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
