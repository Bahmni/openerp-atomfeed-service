/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.testhelper;

import org.bahmni.openerp.web.OpenERPProperties;

public class OpenERPPropertiesStub implements OpenERPProperties {
    @Override
    public String getHost() {
        return "localhost";
    }

    @Override
    public int getPort() {
        return 8069;
    }

    @Override
    public String getDatabase() {
        return "openerp";
    }

    @Override
    public String getUser() {
        return "admin";
    }

    @Override
    public String getPassword() {
        return "password";
    }

    @Override
    public int getConnectionTimeoutInMilliseconds() {
        return -1;
    }

    @Override
    public int getReplyTimeoutInMilliseconds() {
        return -1;
    }
}
