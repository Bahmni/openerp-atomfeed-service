package org.bahmni.feed.openerp.domain.encounter;

import java.util.Date;

public class OpenERPOrder {
    private String orderId;
    private String previousOrderId;
    private String encounterId;
    private String productId;
    private String productName;
    private Double quantity;
    private String quantityUnits;
    private String action;
    private String visitId;
    private String visitType;
    private String type;
    private String description;
    private boolean voided;
    private String locationName;
    private String providerName;
    private String dispensed;
    private String conceptName;
    private Date dateCreated;


    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisitId() {
        return visitId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isVoided() {
        return voided;
    }

    public void setVoided(boolean voided) {
        this.voided = voided;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getVisitType() {
        return visitType;
    }


    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnits() {
        return quantityUnits;
    }

    public void setQuantityUnits(String quantityUnits) {
        this.quantityUnits = quantityUnits;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPreviousOrderId() {
        return previousOrderId;
    }

    public void setPreviousOrderId(String previousOrderId) {
        this.previousOrderId = previousOrderId;
    }

    public String getLocationName() { return locationName;}

    public void setLocationName(String locationName) { this.locationName = locationName; }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getDispensed() { return dispensed; }

    public void setDispensed(String dispensed) {
        this.dispensed = dispensed;
    }

    public String getConceptName() { return conceptName; }

    public void setConceptName(String conceptName) { this.conceptName = conceptName; }
}

