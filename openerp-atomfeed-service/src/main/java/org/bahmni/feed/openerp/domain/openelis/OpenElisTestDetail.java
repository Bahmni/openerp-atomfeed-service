package org.bahmni.feed.openerp.domain.openelis;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Set;

public class OpenElisTestDetail {
    private String testName;
    private String testUnitOfMeasurement;
    private String testUuid;
    private String panelUuid;
    private Double minNormal;
    private Double maxNormal;
    private String result;
    private Set<String> notes;
    private String resultType;
    private String providerUuid;
    private String dateTime;
    private String status;
    private Boolean abnormal;

    @JsonIgnore
    public boolean isCancelled() {
        return "Canceled".equals(status);
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestUnitOfMeasurement() {
        return testUnitOfMeasurement;
    }

    public void setTestUnitOfMeasurement(String testUnitOfMeasurement) {
        this.testUnitOfMeasurement = testUnitOfMeasurement;
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

    public Double getMinNormal() {
        return minNormal;
    }

    public void setMinNormal(Double minNormal) {
        this.minNormal = minNormal;
    }

    public Double getMaxNormal() {
        return maxNormal;
    }

    public void setMaxNormal(Double maxNormal) {
        this.maxNormal = maxNormal;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Set<String> getNotes() {
        return notes;
    }

    public void setNotes(Set<String> notes) {
        this.notes = notes;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getProviderUuid() {
        return providerUuid;
    }

    public void setProviderUuid(String providerUuid) {
        this.providerUuid = providerUuid;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(Boolean abnormal) {
        this.abnormal = abnormal;
    }

}
