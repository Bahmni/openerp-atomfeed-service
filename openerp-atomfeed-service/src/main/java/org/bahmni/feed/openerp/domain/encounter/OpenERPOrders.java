package org.bahmni.feed.openerp.domain.encounter;

import java.util.*;

public class OpenERPOrders {
    private String id;
    private List<OpenERPOrder> openERPOrders;

    public OpenERPOrders(String id){
        this.id = id;
        this.openERPOrders = new ArrayList<OpenERPOrder>();
    }

    public List<OpenERPOrder> getOpenERPOrders() {
        return removeDuplicateOrders(openERPOrders);
    }

    //Filters orders to keep only the latest action for each product except for drugs. This is necessary for ensuring consistent and accurate quotation generation, particularly when order objects may not be in chronological order.
    public List<OpenERPOrder> removeDuplicateOrders(List<OpenERPOrder> orders) {
        Map<String, OpenERPOrder> latestOrders = new LinkedHashMap<>();
        List<OpenERPOrder> finalOrders = new ArrayList<>();
        for (OpenERPOrder order : orders) {
            if("Drug Order".equals(order.getType())) {
                finalOrders.add(order);
            } else {
                latestOrders.merge(order.getProductId(), order, (existingOrder, newOrder) ->
                        (existingOrder.getDateCreated().before(newOrder.getDateCreated())) ? newOrder : existingOrder
                );
            }
        }
        finalOrders.addAll(latestOrders.values());
        return finalOrders;
    }

    public void add(OpenERPOrder order) {
        if(openERPOrders == null) {
            openERPOrders = new ArrayList<>();
        }
        openERPOrders.add(order);
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