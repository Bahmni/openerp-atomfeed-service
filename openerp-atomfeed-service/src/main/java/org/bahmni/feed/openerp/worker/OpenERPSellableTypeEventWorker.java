package org.bahmni.feed.openerp.worker;

import org.apache.log4j.Logger;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.labOrderType.ReferenceDataType;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.service.EventWorker;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenERPSellableTypeEventWorker implements EventWorker {

    private static Logger logger = Logger.getLogger(OpenERPSellableTypeEventWorker.class);

    private final OpenERPClient openERPClient;
    private final String feedUrl;
    private final OpenMRSWebClient webClient;
    private final String urlPrefix;

    public OpenERPSellableTypeEventWorker(String feedUrl, OpenERPClient openERPClient, OpenMRSWebClient openMRSWebClient, String urlPrefix) {
        this.openERPClient = openERPClient;
        this.feedUrl = feedUrl;
        this.webClient = openMRSWebClient;
        this.urlPrefix = urlPrefix;
    }

    @Override
    public void process(Event event) {
        logger.debug("Processing the event [" + event.getContent() + "]");

        try {
            ReferenceDataType referenceDataType = fetchSellableResource(event);
            OpenERPRequest openERPRequest = mapToOpenERPRequest(event, referenceDataType);
            openERPClient.execute(openERPRequest);
        } catch (Exception e) {
            logger.error(String.format("Error occurred while trying to process Sellable Event [%s]", event.getId()), e);
            throw new RuntimeException(String.format("Error occurred while trying to process Sellable Event [%s]", event.getId()), e);
        }

    }

    private ReferenceDataType fetchSellableResource(Event event) throws IOException {
        URI uri = URI.create(urlPrefix + event.getContent());
        System.out.println("********** OpenERPSellableTypeEventWorker ************** ");
        System.out.println("********** OpenERPSellableTypeEventWorker ************** ");

        System.out.println(String.format("fetching event for [%s]", uri.toString()));

        System.out.println("********** OpenERPSellableTypeEventWorker ************** ");
        System.out.println("********** OpenERPSellableTypeEventWorker ************** ");

        String jsonString = webClient.get(uri);
        return ObjectMapperRepository.objectMapper.readValue(jsonString, ReferenceDataType.class);
    }

    public OpenERPRequest mapToOpenERPRequest(Event event, ReferenceDataType referenceDataType) throws IOException {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("name", referenceDataType.getName()));
        parameters.add(new Parameter("uuid", referenceDataType.getUuid()));
        Boolean sellableActive = isSellableActive(referenceDataType);

        parameters.add(new Parameter("is_active", (sellableActive ? "1" : "0"), "boolean"));
        parameters.add(new Parameter("category", "create.sellable"));
        parameters.add(new Parameter("last_read_entry_id",event.getId()));
        parameters.add(new Parameter("feed_uri_for_last_read_entry",event.getFeedUri()));
        parameters.add(new Parameter("feed_uri", feedUrl, "string"));
        if (event.getFeedUri() == null) {
            parameters.add(new Parameter("is_failed_event","1","boolean"));
        }
        Parameter categoryParam = getProductCategoryParameter(referenceDataType);
        if (categoryParam != null) {
            parameters.add(categoryParam);
        }
        return new OpenERPRequest("atom.event.worker", "process_event", parameters);
    }

    private Parameter getProductCategoryParameter(ReferenceDataType referenceDataType) {
        if (referenceDataType.getProperties() != null) {
            String product_category = referenceDataType.getProperties().get("product_category");
            if ((product_category != null) && !"".equals(product_category)) {
                return new Parameter("product_category", product_category);
            }
        }
        return null;
    }

    private Boolean isSellableActive(ReferenceDataType referenceDataType) {
        if (!referenceDataType.getActive()) {
            return false;
        }
        if (referenceDataType.getProperties() != null) {
            return Boolean.valueOf(referenceDataType.getProperties().get("sellable"));
        }
        return true;
    }

    @Override
    public void cleanUp(Event event) {

    }
}
