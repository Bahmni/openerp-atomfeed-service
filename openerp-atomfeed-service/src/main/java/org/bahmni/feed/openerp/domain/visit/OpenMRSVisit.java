package org.bahmni.feed.openerp.domain.visit;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSVisit {
    private String uuid;
    private String visitType;
    private String startDatetime;
    private static final String SPACE = " ";

    public OpenMRSVisit() {
    }

    public OpenMRSVisit(String uuid, String visitType, String startDatetime) {
        this.uuid = uuid;
        this.visitType = visitType;
        this.startDatetime = startDatetime;
    }

    public String getUuid() {
        return uuid;
    }

    @JsonDeserialize(using = VisitTypeDeserializer.class)
    public String getVisitType() {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = "";
        try {
            date = formatter.format(parser.parse(startDatetime));
        } catch (ParseException e) {
            date = startDatetime;
        }

        return visitType + SPACE + date;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

}
