package org.bahmni.feed.openerp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSDrug {

    private String uuid;
    private String name;
    private String genericName;
    private String shortName;
    private String combination;
    private String strength;
    private String dosageForm;
    private String maximumDose;
    private String minimumDose;
    private boolean isActive;

    private Map<String, String> properties;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCombination() {
        return combination;
    }

    public void setCombination(String combination) {
        this.combination = combination;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getMaximumDose() {
        return maximumDose;
    }

    public void setMaximumDose(String maximumDose) {
        this.maximumDose = maximumDose;
    }

    public String getMinimumDose() {
        return minimumDose;
    }

    public void setMinimumDose(String minimumDose) {
        this.minimumDose = minimumDose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
