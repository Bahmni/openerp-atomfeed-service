package org.bahmni.feed.openerp.domain.encounter.bedassignment;

import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.domain.OpenMRSPatient;
import org.bahmni.feed.openerp.domain.encounter.OpenERPOrder;
import org.bahmni.feed.openerp.domain.encounter.OpenERPOrders;
import org.bahmni.feed.openerp.domain.encounter.OpenMRSEncounter;
import org.bahmni.feed.openerp.domain.encounter.OpenMRSEncounterEvent;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.service.ProductService;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMRSBedAssignment extends OpenMRSEncounterEvent {
    private OpenMRSEncounter encounter;
    private OpenMRSPatient patient;
    private OpenMRSBed bed;

    public boolean shouldERPConsumeEvent() {
        return bed != null && patient != null && encounter != null;
    }

    public List<Parameter> getParameters(String eventId, ProductService productService, String feedURIForLastReadEntry, String feedURI) throws IOException {
        validateUrls(feedURIForLastReadEntry, feedURI);

        List<Parameter> parameters = new ArrayList<>();

        String patientId = patient.getDisplay().split(" ")[0];

        parameters.add(createParameter("category", "create.sale.order", "string"));
        parameters.add(createParameter("customer_id", patientId, "string"));
        parameters.add(createParameter("feed_uri", feedURI, "string"));
        parameters.add(createParameter("last_read_entry_id", eventId, "string"));
        parameters.add(createParameter("feed_uri_for_last_read_entry", feedURIForLastReadEntry, "string"));
        parameters.add(createParameter("orders", getOrderJson(productService, bed), "string"));

        return parameters;
    }

    private String getOrderJson(ProductService productService, OpenMRSBed bed) throws IOException {
        String productId = productService.findProductByName(bed.getBedType().getName());
        if (productId == null)
            throw new OpenERPException("Product " + bed.getBedType().getName() + " not Found");

        OpenERPOrder openERPOrder = new OpenERPOrder();
        openERPOrder.setVisitId(encounter.getVisitUuid());
//        openERPOrder.setVisitType(encounter.getVisit().getVisitType());
//        openERPOrder.setDescription(encounter.getVisit().getDescription());

        openERPOrder.setProductId(productId);

        OpenERPOrders orders = new OpenERPOrders(bed.getId());
        orders.getOpenERPOrders().add(openERPOrder);

        return ObjectMapperRepository.objectMapper.writeValueAsString(orders);
    }

    public OpenMRSEncounter getEncounter() {
        return encounter;
    }

    public OpenMRSPatient getPatient() {
        return patient;
    }

    public OpenMRSBed getBed() {
        return bed;
    }
}
