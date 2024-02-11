package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.domain.OpenMRSPersonAddress;
import org.bahmni.feed.openerp.domain.OpenMRSPersonAttribute;
import org.bahmni.feed.openerp.domain.OpenMRSPersonAttributeType;
import org.bahmni.feed.openerp.domain.OpenMRSPersonAttributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
                "countyDistrict", "stateProvince", "country", "postalCode");
    }

    @Test
    public void testGetJson() throws Exception {
        String attributesJsonString = openMRSPersonAttributes.toJsonString();
        assertTrue(attributesJsonString.contains("\"attribute1\":\"value1\""));
        assertTrue(attributesJsonString.contains("\"attribute2\":\"value2\""));
        assertTrue(attributesJsonString.contains("\"attribute3\":\"value3\""));

        String openmrsAddressJsonString = openMRSPersonAddress.toJsonString();
        assertTrue(openmrsAddressJsonString.contains("\"address1\":\"address1\""));
        assertTrue(openmrsAddressJsonString.contains("\"address2\":\"address2\""));
        assertTrue(openmrsAddressJsonString.contains("\"address3\":\"address3\""));
        assertTrue(openmrsAddressJsonString.contains("\"cityVillage\":\"cityVillage\""));
        assertTrue(openmrsAddressJsonString.contains("\"countyDistrict\":\"countyDistrict\""));
        assertTrue(openmrsAddressJsonString.contains("\"stateProvince\":\"stateProvince\""));
        assertTrue(openmrsAddressJsonString.contains("\"country\":\"country\""));
    }

}
