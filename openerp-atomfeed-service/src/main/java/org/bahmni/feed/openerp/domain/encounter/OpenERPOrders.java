package org.bahmni.feed.openerp.domain.encounter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpenERPOrders {
    private String id;
    private List<OpenERPOrder> openERPOrders;

    public OpenERPOrders(String id){
        this.id = id;
        this.openERPOrders = new ArrayList<OpenERPOrder>();
    }

    public List<OpenERPOrder> getOpenERPOrders() {
        return openERPOrders;
    }

    public void add(OpenERPOrder order) {
        if(openERPOrders == null) {
            openERPOrders = new ArrayList<>();
        }
        openERPOrders.add(order);
        Collections.sort(openERPOrders, (order1, order2) -> order1.getDateCreated().compareTo(order2.getDateCreated()));
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
