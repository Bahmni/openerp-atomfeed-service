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

    public List<OpenERPOrder> removeDuplicateOrders(List<OpenERPOrder> orders) {
        Map<String, OpenERPOrder> latestOrders = new LinkedHashMap<>();

        for (OpenERPOrder order : orders) {
            latestOrders.merge(order.getProductId(), order, (existingOrder, newOrder) ->
                    (existingOrder.getDateCreated().before(newOrder.getDateCreated())) ? newOrder : existingOrder
            );
        }

        return new ArrayList<>(latestOrders.values());
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