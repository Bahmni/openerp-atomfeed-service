/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://www.bahmni.org/license/mplv2hd.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.bahmni.feed.openerp.repository;

import org.ict4h.atomfeed.server.domain.EventRecord;
import org.ict4h.atomfeed.server.repository.AllEventRecords;
import org.ict4h.atomfeed.server.repository.EventRecordCreator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

public class DbEventRecordCreator extends EventRecordCreator {

    public DbEventRecordCreator(AllEventRecords allEventRecords) {
        super(allEventRecords);
    }

    public EventRecord create(String uuid, String title, String url, String contents) throws URISyntaxException {
        EventRecord eventRecord = new EventRecord(uuid, title, url, contents, new Date(), "product");
        allEventRecords.add(eventRecord);
        return eventRecord;
    }
}
