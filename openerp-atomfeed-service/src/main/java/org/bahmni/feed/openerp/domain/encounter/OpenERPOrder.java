package org.bahmni.feed.openerp.domain.encounter;

import java.util.List;

public class OpenERPOrder {
    private String id;
    private List<String> productIds;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}
