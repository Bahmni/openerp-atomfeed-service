package org.bahmni.feed.openerp.domain.encounter;

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
    private String type;
    private String description;
    private boolean voided;

    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public void setVisitType(String display) {
        this.type = display;
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

    public String getPreviousOrderId() {
        return previousOrderId;
    }

    public void setPreviousOrderId(String previousOrderId) {
        this.previousOrderId = previousOrderId;
    }

}
