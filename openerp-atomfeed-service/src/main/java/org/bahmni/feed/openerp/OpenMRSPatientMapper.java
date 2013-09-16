package org.bahmni.feed.openerp;

import org.bahmni.feed.openerp.domain.OpenMRSPatient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class OpenMRSPatientMapper {
    private ObjectMapper objectMapper;

    public OpenMRSPatientMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public OpenMRSPatient map(String patientJSON) throws IOException {
        return objectMapper.readValue(patientJSON, OpenMRSPatient.class);
    }
}