package org.bahmni.feed.openerp;

import org.bahmni.feed.openerp.testhelper.FileConverter;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.service.ProductService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenMRSBedAssignmentParserTest {
    //TODO : Fix bed assignment code when required
    @Ignore
    @Test
    public void consumes_bed_assignment_encounter() throws Exception {
        String visitId = "5582a60a-5a13-4a41-81d0-678ff5bd0a7d";
        String bedId = "41";

        ProductService productService = mock(ProductService.class);
        String generalBedProductIdInERP = "12234324";
        when(productService.findProductByName("General")).thenReturn(generalBedProductIdInERP);

        OpenMRSBedAssignmentParser openMRSBedAssignmentParser = new OpenMRSBedAssignmentParser(ObjectMapperRepository.objectMapper);
        OpenERPRequest erpRequest = openMRSBedAssignmentParser.parse(FileConverter.convertToString("encounterResourceForBedAssignment.json"),
                productService, "eventId", "http://lastReadEntry", "http://feedUrl");

        Assert.assertNotSame(OpenERPRequest.DO_NOT_CONSUME, erpRequest);

        List<Parameter> parameters = erpRequest.getParameters();
        Assert.assertTrue(parameters.contains(new Parameter("category", "create.sale.order", "string")));
        Assert.assertTrue(parameters.contains(new Parameter("customer_id", "BAM200003", "string")));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri", "http://feedUrl", "string")));
        Assert.assertTrue(parameters.contains(new Parameter("last_read_entry_id", "eventId", "string")));
        Assert.assertTrue(parameters.contains(new Parameter("feed_uri_for_last_read_entry", "http://lastReadEntry", "string")));

        String erpOrdersJson = getERPOrdersJson(parameters);
        Assert.assertNotNull(erpOrdersJson);

        Assert.assertTrue(erpOrdersJson.contains(generalBedProductIdInERP));
        Assert.assertTrue(erpOrdersJson.contains(visitId));
        Assert.assertTrue(erpOrdersJson.contains(bedId));
    }

    @Test
    public void does_not_consume_lab_order_encounter() throws Exception {
        OpenMRSBedAssignmentParser openMRSBedAssignmentParser = new OpenMRSBedAssignmentParser(ObjectMapperRepository.objectMapper);
        OpenERPRequest encounter = openMRSBedAssignmentParser.parse(FileConverter.convertToString("admissionEncounterResourceWithoutOrders.json"),
                null, "eventId", "http://feeduri", "http://feedUrl");
        Assert.assertSame(OpenERPRequest.DO_NOT_CONSUME, encounter);
    }

    @Test
    public void does_not_consume_admission_encounter() throws Exception {
        OpenMRSBedAssignmentParser openMRSBedAssignmentParser = new OpenMRSBedAssignmentParser(ObjectMapperRepository.objectMapper);
        OpenERPRequest encounter = openMRSBedAssignmentParser.parse(FileConverter.convertToString("encounterResourceForDrugOrder.json"),
                null, "eventId", "http://feeduri", "http://feedUrl");
        Assert.assertSame(OpenERPRequest.DO_NOT_CONSUME, encounter);
    }

    @Test(expected = OpenERPException.class)
    public void throws_exception_if_product_not_found() throws Exception {

        OpenMRSBedAssignmentParser openMRSBedAssignmentParser= new OpenMRSBedAssignmentParser(ObjectMapperRepository.objectMapper);
        ProductService productService = mock(ProductService.class);
        when(productService.findProductByName("General Bed")).thenReturn(null);
        openMRSBedAssignmentParser.parse(FileConverter.convertToString("encounterResourceForBedAssignment.json"),
            productService, "eventID", "http://feeduri", "http://feedUrl");
    }

    private String getERPOrdersJson(List<Parameter> parameters) {
        for (Parameter parameter : parameters) {
            if (parameter.getName().equals("orders")) {
                return parameter.getValue();
            }
        }
        return null;
    }
}
