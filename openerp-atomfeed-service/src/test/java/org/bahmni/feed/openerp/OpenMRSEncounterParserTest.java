package org.bahmni.feed.openerp;


import org.bahmni.feed.openerp.testhelper.FileConverter;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class OpenMRSEncounterParserTest {

    @Test
    public void shouldMapAllValuesFromContractSuccessfully() throws IOException {
        OpenMRSEncounterParser openMRSEncounterParser = new OpenMRSEncounterParser(ObjectMapperRepository.objectMapper);
        OpenERPRequest encounter = openMRSEncounterParser.parse(FileConverter.convertToString("encounterResourceForLabOrder.json"),
                null, "eventId", "http://feeduri", "http://feedUrl");
        Assert.assertNotNull(encounter);
    }
}
