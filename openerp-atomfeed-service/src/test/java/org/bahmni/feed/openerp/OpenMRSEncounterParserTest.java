package org.bahmni.feed.openerp;


import org.bahmni.feed.openerp.testhelper.SampleEncounter;
import org.bahmni.feed.openerp.domain.encounter.OpenMRSEncounter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class OpenMRSEncounterParserTest {

    @Test
    public void shouldMapAllValuesFromContractSuccessfully() throws IOException {
        OpenMRSEncounter encounter = new OpenMRSEncounterParser(ObjectMapperRepository.objectMapper).parse(SampleEncounter.json());
        Assert.assertNotNull(encounter);
    }
}
