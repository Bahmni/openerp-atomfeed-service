package org.bahmni.feed.openerp.domain.openelis;

import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.domain.encounter.OpenERPOrder;
import org.bahmni.feed.openerp.domain.encounter.OpenERPOrders;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.bahmni.openerp.web.service.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OpenElisLabOrder {
    private String accessionUuid;
    private String patientUuid;
    private String patientFirstName;
    private String patientLastName;
    private String dateTime;
    private String patientIdentifier;
    private Set<OpenElisTestDetail> testDetails = new HashSet<>();

    public List<Parameter> getParameters(String eventId, ProductService productService, String feedURIForLastReadEntry, String feedURI) throws IOException {
        List<Parameter> parameters = new ArrayList<>();
        String patientId = getPatientIdentifier();

        parameters.add(createParameter("category", "create.sale.order", "string"));
        parameters.add(createParameter("customer_id", patientId, "string"));
        parameters.add(createParameter("feed_uri", feedURI, "string"));
        parameters.add(createParameter("last_read_entry_id", eventId, "string"));
        parameters.add(createParameter("feed_uri_for_last_read_entry", feedURIForLastReadEntry, "string"));

        OpenERPOrders orders = new OpenERPOrders();
        orders.setId(getAccessionUuid());

        mapOrders(parameters, orders, productService);
        return parameters;
    }

    private void mapOrders(List<Parameter> parameters, OpenERPOrders orders, ProductService productService) throws IOException {
        if (hasOrders()) {
            for (OpenElisTestDetail testDetail : testDetails) {
                if(testDetail.getPanelUuid() == null || !orderAlreadyPresent(orders, testDetail)) {
                    addNewOrder(orders, testDetail);
                }
            }
        }
        String ordersJson = ObjectMapperRepository.objectMapper.writeValueAsString(orders);
        parameters.add(createParameter("orders", ordersJson, "string"));
    }

    private boolean orderAlreadyPresent(OpenERPOrders orders, OpenElisTestDetail testDetail) {
        for (OpenERPOrder openERPOrder : orders.getOpenERPOrders()) {
            String conceptUuid = testDetail.getPanelUuid() != null ? testDetail.getPanelUuid() : testDetail.getTestUuid();
            if(openERPOrder.getProductIds().contains(conceptUuid)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasOrders() {
        return testDetails.size() > 0;
    }

    private void addNewOrder(OpenERPOrders orders, OpenElisTestDetail testDetail) {
        OpenERPOrder openERPOrder = new OpenERPOrder();
        openERPOrder.setId(getAccessionUuid());
        openERPOrder.setVoided(testDetail.isCancelled());
        // no visit info in elis
//        openERPOrder.setVisitId(visit.getUuid());
//        openERPOrder.setVisitType(visit.getVisitType());
//        openERPOrder.setDescription(visit.getDescription());

        List<String> productIds = new ArrayList<>();
        if(testDetail.getPanelUuid() != null) {
            productIds.add(testDetail.getPanelUuid());
        } else {
            productIds.add(testDetail.getTestUuid());
        }
        openERPOrder.setProductIds(productIds);
        orders.getOpenERPOrders().add(openERPOrder);
    }

    protected Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }

    public String getAccessionUuid() {
        return accessionUuid;
    }

    public void setAccessionUuid(String accessionUuid) {
        this.accessionUuid = accessionUuid;
    }

    public String getPatientUuid() {
        return patientUuid;
    }

    public void setPatientUuid(String patientUuid) {
        this.patientUuid = patientUuid;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPatientIdentifier() {
        return patientIdentifier;
    }

    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }

    public Set<OpenElisTestDetail> getTestDetails() {
        return testDetails;
    }

    public void setTestDetails(Set<OpenElisTestDetail> testDetails) {
        this.testDetails = testDetails;
    }
}

