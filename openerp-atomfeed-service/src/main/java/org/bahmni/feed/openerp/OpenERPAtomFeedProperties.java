package org.bahmni.feed.openerp;

import org.bahmni.openerp.web.OpenERPProperties;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class OpenERPAtomFeedProperties implements OpenERPProperties {


    public static final String DEFAULT_PROPERTY_FILENAME = "atomfeed";

    private ResourceBundle resourceBundle;

    OpenERPAtomFeedProperties(String propertyFilename) {
        this.resourceBundle = ResourceBundle.getBundle(propertyFilename);
    }

    public OpenERPAtomFeedProperties() {
        this(DEFAULT_PROPERTY_FILENAME);
    }

    public String getSchedulerDelay() {
        return resourceBundle.getString("scheduler.fixed.delay");
    }

    public String getFeedUri(String feedname) {
        return resourceBundle.getString(feedname);
    }

    @Override
    public String getHost() {
        return resourceBundle.getString("openerp.host");
    }

    @Override
    public int getPort() {
        return Integer.parseInt(resourceBundle.getString("openerp.port"));
    }

    @Override
    public String getDatabase() {
        return resourceBundle.getString("openerp.database");
    }

    @Override
    public String getUser() {
        return resourceBundle.getString("openerp.user");
    }

    @Override
    public String getPassword() {
        return resourceBundle.getString("openerp.password");
    }

    @Override
    public int getConnectionTimeoutInMilliseconds() {
        return Integer.parseInt(resourceBundle.getString("openerp.connectionTimeoutInMilliseconds"));
    }

    @Override
    public int getReplyTimeoutInMilliseconds() {
        return Integer.parseInt(resourceBundle.getString("openerp.replyTimeoutInMilliseconds"));
    }

    public int getMaxFailedEvents() {
        return Integer.parseInt(resourceBundle.getString("openerp.maxFailedEvents"));
    }

    public String getAuthenticationURI() {
        return resourceBundle.getString("openmrs.auth.uri");
    }

    public String getOpenElisURI() {
        return resourceBundle.getString("openelis.uri");
    }

    public String getOpenElisUser() {
        return resourceBundle.getString("openelis.user");
    }

    public String getOpenElisPassword() {
        return resourceBundle.getString("openelis.password");
    }

    public String getOpenMRSUser() {
        return resourceBundle.getString("openmrs.user");
    }

    public String getOpenMRSPassword() {
        return resourceBundle.getString("openmrs.password");
    }

    public String getReferenceDataEndpointURI(){
        return  resourceBundle.getString("referencedata.endpoint");
    }
}
