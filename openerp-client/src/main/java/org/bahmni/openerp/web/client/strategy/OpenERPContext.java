/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.openerp.web.client.strategy;

import org.bahmni.openerp.web.request.OpenERPRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class OpenERPContext {
    private final OpenERPClientStrategy openERPClient;

    @Autowired
    public OpenERPContext(OpenERPClientStrategy openERPClient) {
        this.openERPClient = openERPClient;
    }

    public Object execute(OpenERPRequest openERPRequest, String URL) {
        return openERPClient.execute(openERPRequest, URL);
    }

}