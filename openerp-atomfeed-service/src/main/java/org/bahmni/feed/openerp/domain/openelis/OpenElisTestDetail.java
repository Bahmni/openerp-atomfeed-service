package org.bahmni.feed.openerp.domain.openelis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenElisTestDetail {
    private String testUuid;
    private String panelUuid;
    private String result;
    private String status;

    @JsonIgnore
    public boolean isCancelled() {
        return "Canceled".equals(status);
    }

    public String getTestUuid() {
        return testUuid;
    }

    public void setTestUuid(String testUuid) {
        this.testUuid = testUuid;
    }

    public String getPanelUuid() {
        return panelUuid;
    }

    public void setPanelUuid(String panelUuid) {
        this.panelUuid = panelUuid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
