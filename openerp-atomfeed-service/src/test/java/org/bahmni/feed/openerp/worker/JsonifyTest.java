package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.domain.OpenMRSPersonAddress;
import org.bahmni.feed.openerp.domain.OpenMRSPersonAttribute;
import org.bahmni.feed.openerp.domain.OpenMRSPersonAttributeType;
import org.bahmni.feed.openerp.domain.OpenMRSPersonAttributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonifyTest {
    private OpenMRSPersonAttributes openMRSPersonAttributes;
    private OpenMRSPersonAddress openMRSPersonAddress;

    @Before
    public void setUp() throws Exception {
        OpenMRSPersonAttributeType type1 = new OpenMRSPersonAttributeType("type1", "attribute1");
        OpenMRSPersonAttributeType type2 = new OpenMRSPersonAttributeType("type2", "attribute2");
        OpenMRSPersonAttributeType type3 = new OpenMRSPersonAttributeType("type3", "attribute3");
        OpenMRSPersonAttribute attr1 = new OpenMRSPersonAttribute("attr1", "value1", type1);
        OpenMRSPersonAttribute attr2 = new OpenMRSPersonAttribute("attr2", "value2", type2);
        OpenMRSPersonAttribute attr3 = new OpenMRSPersonAttribute("attr3", "value3", type3);
        openMRSPersonAttributes = new OpenMRSPersonAttributes();
        openMRSPersonAttributes.add(attr1);
        openMRSPersonAttributes.add(attr2);
        openMRSPersonAttributes.add(attr3);
        openMRSPersonAddress = new OpenMRSPersonAddress("address1", "address2", "address3", "cityVillage",
                "countyDistrict", "stateProvince", "country");
    }

    @Test
    public void testGetJson() throws Exception {
        assertEquals("{\"attribute1\":\"value1\",\"attribute2\":\"value2\",\"attribute3\":\"value3\"}",
                openMRSPersonAttributes.toJsonString());
        assertEquals("{\"address1\":\"address1\",\"address2\":\"address2\",\"address3\":\"address3\"," +
                        "\"cityVillage\":\"cityVillage\",\"countyDistrict\":\"countyDistrict\"," +
                        "\"stateProvince\":\"stateProvince\",\"country\":\"country\"}",
                openMRSPersonAddress.toJsonString());

    }

}
