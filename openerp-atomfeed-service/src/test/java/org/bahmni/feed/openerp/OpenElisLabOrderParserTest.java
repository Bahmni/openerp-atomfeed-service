/*
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/ 
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*/
package org.bahmni.feed.openerp;

import org.bahmni.feed.openerp.testhelper.FileConverter;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class OpenElisLabOrderParserTest {

    @Test
    public void shouldMapValuesFromContract() throws IOException {
        OpenElisLabOrderParser parser = new OpenElisLabOrderParser(ObjectMapperRepository.objectMapper);
        OpenERPRequest request = parser.parse(FileConverter.convertToString("openElisAccession.json"), null, "eventId", "http://feeduri", "http://feedUrl");
        Assert.assertNotNull(request);
    }

}
