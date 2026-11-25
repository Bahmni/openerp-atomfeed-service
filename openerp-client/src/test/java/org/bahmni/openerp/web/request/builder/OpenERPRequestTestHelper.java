/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.openerp.web.request.builder;

import java.util.List;

import static java.util.Arrays.asList;

public class OpenERPRequestTestHelper {
    public OpenERPRequestTestHelper() {
    }

    Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }

    public List<Parameter> createCustomerRequest(String patientName, String patientId, String village) {
        return asList(createParameter("name", patientName, "string"),
                createParameter("ref", patientId, "string"),
                createParameter("village", village, "string"));
    }

}