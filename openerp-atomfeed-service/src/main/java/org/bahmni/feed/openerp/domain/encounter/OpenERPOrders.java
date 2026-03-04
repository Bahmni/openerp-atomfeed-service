/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.domain.encounter;

import java.util.*;

public class OpenERPOrders {
    private String id;
    private List<OpenERPOrder> openERPOrders;

    public OpenERPOrders(String id){
        this.id = id;
        this.openERPOrders = new ArrayList<OpenERPOrder>();
    }

    public OpenERPOrders(){
    }

    public List<OpenERPOrder> getOpenERPOrders() {
        return openERPOrders;
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