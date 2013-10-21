package org.bahmni.feed.openerp.domain.encounter;

import java.util.ArrayList;
import java.util.List;

public class OpenERPOrders {
    private String id;
    private List<OpenERPOrder> openERPOrders;

    public OpenERPOrders(){
        this.openERPOrders = new ArrayList<OpenERPOrder>();
    }

    public List<OpenERPOrder> getOpenERPOrders() {
        return openERPOrders;
    }

    public void setOpenERPOrders(List<OpenERPOrder> openERPOrders) {
        this.openERPOrders = openERPOrders;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
