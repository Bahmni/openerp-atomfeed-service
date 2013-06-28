package org.bahmni.feed.openerp;

import org.bahmni.openerp.web.OpenERPProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AtomFeedProperties implements OpenERPProperties {

    private @Value("${feed.generator.uri}") String feedUri;
    private @Value("${openerp.host}") String host;
    private @Value("${openerp.port}") int port;
    private @Value("${openerp.database}") String database;
    private @Value("${openerp.user}") String user;
    private @Value("${openerp.password}") String password;

    public String getFeedUri() {
        return feedUri;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
