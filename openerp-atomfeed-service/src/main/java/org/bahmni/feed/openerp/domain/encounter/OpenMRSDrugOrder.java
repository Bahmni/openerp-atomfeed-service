package org.bahmni.feed.openerp.domain.encounter;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSDrugOrder {
    private OpenMRSDrug drug;
    private DosingInstructions dosingInstructions;
    private String action;
    private String uuid;
    private String previousOrderUuid;
    private String orderType;
    private boolean voided;

    public OpenMRSDrug getDrug() {
        return drug;
    }

    public DosingInstructions getDosingInstructions() {
        return dosingInstructions;
    }

    public String getAction() {
        return action;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPreviousOrderUuid() {
        return previousOrderUuid;
    }

    public String getDrugUuid() {
        return getDrug() != null ? getDrug().getUuid() : null;
    }

    public String getDrugName() {
        return getDrug() != null ? getDrug().getName() : null;
    }

    public Double getQuantity() {
        return getDosingInstructions() != null ? getDosingInstructions().getQuantity() : null;
    }

    public String getQuantityUnits() {
        return getDosingInstructions() != null ? getDosingInstructions().getQuantityUnits() : null;
    }

    public boolean isVoided() {
        return voided;
    }

    public String getOrderType() {
        return orderType;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class OpenMRSDrug {
        private String uuid;
        private String name;
        private String form;

        public String getUuid() {
            return uuid;
        }

        public String getName() {
            return name;
        }

        public String getForm() {
            return form;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class DosingInstructions {
        private Double quantity;
        private String quantityUnits;

        public Double getQuantity() {
            return quantity;
        }

        public String getQuantityUnits() {
            return quantityUnits;
        }
    }
}
