/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.client;

import org.slf4j.Logger;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.webclients.ClientCookies;
import org.bahmni.webclients.ConnectionDetails;
import org.bahmni.webclients.HttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class AbstractWebClient {
    protected HttpClient httpClient;
    protected ConnectionDetails connectionDetails;

    public String get(URI uri) {
        return httpClient.get(uri);
    }


    public <T> T get(String uri,Class<T> klass) throws IOException {
        return httpClient.get(uri,klass);
    }

    protected abstract ConnectionDetails connectionDetails(OpenERPAtomFeedProperties properties);

    protected abstract Logger getLogger();

    public ClientCookies getCookies() {
        try {
            return httpClient.getCookies(new URI(connectionDetails.getAuthUrl()));
        } catch (URISyntaxException e) {
            getLogger().error("Unable to get Cookies", e);
        }
        return new ClientCookies();
    }
}
