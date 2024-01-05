package org.bahmni.feed.openerp.domain.encounter;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSDrugOrder {
    private OpenMRSConcept concept;
    private String drugNonCoded;
    private OpenMRSDrug drug;
    private DosingInstructions dosingInstructions;
    private String action;
    private String uuid;
    private String previousOrderUuid;
    private String orderType;
    private Date dateActivated;
    private boolean voided;

    public OpenMRSConcept getConcept() {
        return concept;
    }

    public String getConceptName() {
        return getConcept() != null ? getConcept().getName() : null;
    }

    public String getConceptClass() {
        return getConcept() != null ? getConcept().getConceptClass() : null;
    }

    public String getDrugNonCoded() {
        return drugNonCoded;
    }

    public OpenMRSDrug getDrug() {
        return drug;
    }

    public DosingInstructions getDosingInstructions() {
        return dosingInstructions;
    }

    public String getAction() {
        return action;
    }

    public Date getDateActivated() {
        return dateActivated;
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
