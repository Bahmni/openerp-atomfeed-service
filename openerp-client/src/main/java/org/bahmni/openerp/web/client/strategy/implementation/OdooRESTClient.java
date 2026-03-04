/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.openerp.web.client.strategy.implementation;

import org.bahmni.openerp.web.OpenERPProperties;
import org.bahmni.openerp.web.client.strategy.OpenERPClientStrategy;
import org.bahmni.openerp.web.http.client.RestClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class OdooRESTClient implements OpenERPClientStrategy {
    private final RestClient restClient;

    @Autowired
    public OdooRESTClient(OpenERPProperties openERPProperties) {
        final String host = openERPProperties.getHost();
        final int port = openERPProperties.getPort();
        final String database = openERPProperties.getDatabase();
        final String user = openERPProperties.getUser();
        final String password = openERPProperties.getPassword();
        final int connectionTimeoutInMilliseconds = openERPProperties.getConnectionTimeoutInMilliseconds();
        restClient = new RestClient("http://" + host + ":" + port, user, password, connectionTimeoutInMilliseconds, database);
    }

    @Override
    public Object execute(OpenERPRequest openERPRequest, String URL) {
        String requestBody = RequestBuilder.buildNewRestRequest(openERPRequest);
        return restClient.post(URL, requestBody);
    }
}
