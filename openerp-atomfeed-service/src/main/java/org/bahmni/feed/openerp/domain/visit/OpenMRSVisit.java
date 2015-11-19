package org.bahmni.feed.openerp.domain.visit;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSVisit {

    private String uuid;
    private List<VisitAttributes> attributes = new ArrayList<>();

    public List<VisitAttributes> getAttributes() {
        return attributes;
    }

    public String getUuid() {
        return uuid;
    }
}
