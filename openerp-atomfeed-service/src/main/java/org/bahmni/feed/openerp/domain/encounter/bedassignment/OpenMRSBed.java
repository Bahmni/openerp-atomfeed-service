package org.bahmni.feed.openerp.domain.encounter.bedassignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSBed {
    private String id;
    private String bedNumber;
    private OpenMRSBedType bedType;

    private String status;

    public String getId() {
        return id;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public OpenMRSBedType getBedType() {
        return bedType;
    }

    public String getStatus() {
        return status;
    }
}
