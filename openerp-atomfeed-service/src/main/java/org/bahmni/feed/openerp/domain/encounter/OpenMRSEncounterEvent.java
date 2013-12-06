package org.bahmni.feed.openerp.domain.encounter;

import org.bahmni.openerp.web.request.builder.Parameter;

public class OpenMRSEncounterEvent {
    protected Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }

    protected void validateUrls(String feedUri, String feedUrl) {
        if ((feedUrl != null && feedUrl.contains("$param")) || (feedUri != null && feedUri.contains("$param")))
            throw new RuntimeException("Junk values in the feedUrl:$param**");
    }
}
