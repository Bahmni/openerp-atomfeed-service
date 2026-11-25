/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.openerp.web.service.domain;

public class Customer {
    private final String name;
    private final String ref;
    private final String village;

    public Customer(String name, String ref, String village) {
        this.name = name;
        this.ref = ref;
        this.village = village;
    }

    public String getName() {
        return name;
    }

    public String getRef() {
        return ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (!name.equals(customer.name)) return false;
        if (!ref.equals(customer.ref)) return false;
        if (village != null ? !village.equals(customer.village) : customer.village != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + ref.hashCode();
        result = 31 * result + (village != null ? village.hashCode() : 0);
        return result;
    }

    public String getVillage() {
        return village;
    }
}
