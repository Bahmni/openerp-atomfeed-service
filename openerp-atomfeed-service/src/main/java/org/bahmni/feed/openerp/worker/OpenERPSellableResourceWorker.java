package org.bahmni.feed.openerp.worker;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.OpenMRSResource;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class OpenERPSellableResourceWorker implements EventWorker {

    public static final String ERP_EVENT_CATEGORY = "create.service.sellable";
    public static final String SELLABLE = "sellable";
    public static final String PRODUCT_CATEGORY = "product_category";
    private OpenERPClient openERPClient;
    private String feedUrl;
    private OpenMRSWebClient openMRSWebClient;
    private String urlPrefix;

    private static Logger logger = Logger.getLogger(OpenERPSellableResourceWorker.class);

    public OpenERPSellableResourceWorker(String feedUrl, OpenERPClient openERPClient, OpenMRSWebClient openMRSWebClient, String urlPrefix) {
        this.openERPClient = openERPClient;
        this.feedUrl = feedUrl;
        this.openMRSWebClient = openMRSWebClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        logger.debug(String.format("Process event [%s] with content: %s", event.getId(), event.getContent()));
        try {
            OpenMRSResource resource = getOpenMRSResource(event);
            if (!isSellableResource(resource)) {
                logger.info(String.format("Resource is not a sellable resource. Ignoring. Event [%s]",event.getId()));
                return;
            }
            openERPClient.execute(mapToOpenERPRequest(event, resource));
        } catch (Exception e) {
            logger.error(String.format("Error occurred while trying to process Sellable Event [%s]", event.getId()), e);
            throw new RuntimeException(String.format("Error occurred while trying to process Sellable Event [%s]", event.getId()), e);
        }
    }

    private boolean isSellableResource(OpenMRSResource resource) {
        if (resource.getProperties() != null) {
            return resource.getProperties().containsKey(SELLABLE);
        }
        return false;
    }

    @Override
    public void cleanUp(Event event) {

    }

    private OpenERPRequest mapToOpenERPRequest(Event event, OpenMRSResource resource) throws IOException {
        List<Parameter> parameters = buildParameters(event, resource);
        return new OpenERPRequest("atom.event.worker", "process_event", parameters);
    }

    private OpenMRSResource getOpenMRSResource(Event event) throws IOException {
        String resourceJson = openMRSWebClient.get(URI.create(urlPrefix + event.getContent()));
        return ObjectMapperRepository.objectMapper.readValue(resourceJson, OpenMRSResource.class);
    }

    private List<Parameter> buildParameters(Event event, OpenMRSResource resource) {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("name", resource.getName()));
        parameters.add(new Parameter("uuid", resource.getUuid()));
        Boolean sellableActive = isSellableActive(resource);
        parameters.add(new Parameter("is_active", (sellableActive ? "1" : "0"), "boolean"));

        parameters.add(new Parameter("category", ERP_EVENT_CATEGORY));

        parameters.add(new Parameter("feed_uri", event.getFeedUri()));
        parameters.add(new Parameter("last_read_entry_id",event.getId()));
        parameters.add(new Parameter("feed_uri_for_last_read_entry",event.getFeedUri()));

        if (event.getFeedUri() == null) {
            parameters.add(new Parameter("is_failed_event","1","boolean"));
        }

        Parameter categoryParam = getProductCategoryParameter(resource);
        if (categoryParam != null) {
            parameters.add(categoryParam);
        }
        return parameters;
    }

    private Boolean isSellableActive(OpenMRSResource resource) {
        if (!resource.isActive()) {
            return false;
        }
        if (resource.getProperties() != null) {
            String sellable = resource.getProperties().get(SELLABLE);
            if ((sellable != null) && !"".equals(sellable)) {
                return Boolean.valueOf(sellable);
            }
        }
        return true;
    }

    private Parameter getProductCategoryParameter(OpenMRSResource resource) {
        if (resource.getProperties() != null) {
            String product_category = resource.getProperties().get(PRODUCT_CATEGORY);
            if ((product_category != null) && !"".equals(product_category)) {
                return new Parameter(PRODUCT_CATEGORY, product_category);
            }
        }
        return null;
    }
}
