package org.bahmni.feed.openerp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AtomFeedProperties {

    private @Value("${feed.generator.uri}") String feedUri;

    public String getFeedUri() {
        return feedUri;
    }

}
