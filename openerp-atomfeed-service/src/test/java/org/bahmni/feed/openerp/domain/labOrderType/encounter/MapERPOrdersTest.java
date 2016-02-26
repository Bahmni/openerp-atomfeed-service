package org.bahmni.feed.openerp.domain.labOrderType.encounter;

import junit.framework.Assert;
import org.bahmni.feed.openerp.domain.encounter.MapERPOrders;
import org.bahmni.feed.openerp.domain.encounter.OpenMRSEncounter;
import org.bahmni.feed.openerp.domain.visit.OpenMRSVisit;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MapERPOrdersTest {

    private MapERPOrders mapERPOrders;

    @Mock
    private OpenMRSEncounter openMRSEncounter;

    @Mock
    private OpenMRSVisit openMRSVisit;

    @Before
    public void setUp() {
        mapERPOrders = new MapERPOrders(openMRSEncounter, openMRSVisit);
    }

    @Test
    public void shouldMapOpenERPOrders() throws IOException {
        when(openMRSEncounter.getPatientId()).thenReturn("12345");
        when(openMRSEncounter.getEncounterUuid()).thenReturn("r5d6789uyt32");
        when(openMRSEncounter.getLocationName()).thenReturn("Ganyari");
        List<Parameter> parameterList = mapERPOrders.getParameters("102", "something", "somethingelse");

        Assert.assertEquals(8, parameterList.size());
        Assert.assertEquals("create.sale.order", parameterList.get(0).getValue());
        Assert.assertEquals("12345", parameterList.get(1).getValue());
        Assert.assertEquals("r5d6789uyt32", parameterList.get(2).getValue());
        Assert.assertEquals("Ganyari", parameterList.get(7).getValue());
    }
}