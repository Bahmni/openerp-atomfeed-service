package org.bahmni.feed.openerp.domain.encounter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSOrderAttributeResponse {
    private List<OpenMRSOrderAttribute> results;

    public List<OpenMRSOrderAttribute> getResults() {
        return results;
    }

    public void setResults(List<OpenMRSOrderAttribute> results) {
        this.results = results;
    }
}
