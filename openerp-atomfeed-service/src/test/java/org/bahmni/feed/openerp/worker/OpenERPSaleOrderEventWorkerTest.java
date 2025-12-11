package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.odooconnect.extensions.SaleOrderContext;
import org.bahmni.odooconnect.extensions.SaleOrderParameterProvider;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.function.BiFunction;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class OpenERPSaleOrderEventWorkerTest {

    @Mock
    private OpenERPAtomFeedProperties atomFeedProperties;
    @Mock
    private OpenERPContext openERPContext;
    @Mock
    private OpenMRSWebClient webClient;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private SaleOrderParameterProvider mockProvider1;
    @Mock
    private SaleOrderParameterProvider mockProvider2;

    private String feedUrl = "http://feeduri";
    private String odooURL = "http://odooURL";
    private String urlPrefix = "http://prefix/";
    private Boolean isOdoo16 = false;
    private OpenERPSaleOrderEventWorker worker;

    private String encounterJson;
    private String visitJson;

    @Before
    public void setUp() throws IOException {
        initMocks(this);

        encounterJson = "{\n" +
                "    \"encounterUuid\": \"encounter-uuid-123\",\n" +
                "    \"patientUuid\": \"patient-uuid-456\",\n" +
                "    \"visitUuid\": \"visit-uuid-789\",\n" +
                "    \"orders\": [\n" +
                "        {\n" +
                "            \"uuid\": \"order-uuid-1\",\n" +
                "            \"orderType\": \"Lab Order\",\n" +
                "            \"concept\": {\"display\": \"Blood Test\", \"uuid\": \"concept-uuid\"}\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        visitJson = "{\n" +
                "    \"uuid\": \"visit-uuid-789\",\n" +
                "    \"visitType\": {\"uuid\": \"visit-type-uuid\"},\n" +
                "    \"location\": {\"name\": \"Test Location\"}\n" +
                "}";

        when(atomFeedProperties.getOpenMRSUser()).thenReturn("test-user");
        when(atomFeedProperties.getOpenMRSPassword()).thenReturn("test-password");

        // Setup default webClient mock behavior
        when(webClient.get(any(URI.class))).thenAnswer(invocation -> {
            URI uri = invocation.getArgument(0);
            String uriString = uri.toString();
            if (uriString.contains("content")) {
                return encounterJson;
            } else if (uriString.contains("visit")) {
                return visitJson;
            }
            return null;
        });
    }

    @Test
    public void shouldAddExtensionParametersWhenProvidersExist() throws IOException {
        // Setup
        Map<String, Object> extensionParams = new HashMap<>();
        extensionParams.put("customField1", "value1");
        extensionParams.put("customField2", "value2");

        Map<String, SaleOrderParameterProvider> providers = new HashMap<>();
        providers.put("provider1", mockProvider1);

        when(applicationContext.getBeansOfType(SaleOrderParameterProvider.class))
                .thenReturn(providers);
        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(extensionParams);

        Event event = new Event("event-id", "content", "test", feedUrl, new Date());
        worker = new OpenERPSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClient,
                urlPrefix, atomFeedProperties, isOdoo16, applicationContext);

        // Execute
        worker.process(event);

        // Verify
        ArgumentCaptor<OpenERPRequest> requestCaptor = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPContext).execute(requestCaptor.capture(), eq(odooURL));

        OpenERPRequest capturedRequest = requestCaptor.getValue();
        List<Parameter> parameters = capturedRequest.getParameters();

        // Verify extension parameters were added
        assertTrue("Should contain customField1",
                parameters.stream().anyMatch(p -> "customField1".equals(p.getName()) && "value1".equals(p.getValue())));
        assertTrue("Should contain customField2",
                parameters.stream().anyMatch(p -> "customField2".equals(p.getName()) && "value2".equals(p.getValue())));
    }

    @Test
    public void shouldNotAddExtensionParametersWhenNoProvidersExist() throws IOException {
        // Setup - empty providers
        when(applicationContext.getBeansOfType(SaleOrderParameterProvider.class))
                .thenReturn(Collections.emptyMap());

        Event event = new Event("event-id", "content", "test", feedUrl, new Date());
        worker = new OpenERPSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClient,
                urlPrefix, atomFeedProperties, isOdoo16, applicationContext);

        // Execute
        worker.process(event);

        // Verify
        verify(applicationContext).getBeansOfType(SaleOrderParameterProvider.class);
        verify(mockProvider1, never()).getAdditionalParams(any(), any());
        verify(openERPContext).execute(any(OpenERPRequest.class), eq(odooURL));
    }

    @Test
    public void shouldNotAddExtensionParametersWhenApplicationContextIsNull() throws IOException {
        // Setup - null application context
        Event event = new Event("event-id", "content", "test", feedUrl, new Date());
        when(webClient.get(URI.create(urlPrefix + "content"))).thenReturn(encounterJson);
        when(webClient.get(URI.create(urlPrefix + "/openmrs/ws/rest/v1/visit/visit-uuid-789?v=full")))
                .thenReturn(visitJson);

        worker = new OpenERPSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClient,
                urlPrefix, atomFeedProperties, isOdoo16, null);

        // Execute
        worker.process(event);

        // Verify
        ArgumentCaptor<OpenERPRequest> requestCaptor = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPContext).execute(requestCaptor.capture(), eq(odooURL));

        // Should still process successfully, just without extension parameters
        OpenERPRequest capturedRequest = requestCaptor.getValue();
        assertNotNull(capturedRequest);
    }

    @Test
    public void shouldPassCorrectContextToExtensionProviders() throws IOException {
        // Setup
        Map<String, SaleOrderParameterProvider> providers = new HashMap<>();
        providers.put("provider1", mockProvider1);

        when(applicationContext.getBeansOfType(SaleOrderParameterProvider.class))
                .thenReturn(providers);
        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(Collections.emptyMap());

        Event event = new Event("event-id", "content", "test", feedUrl, new Date());
        worker = new OpenERPSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClient,
                urlPrefix, atomFeedProperties, isOdoo16, applicationContext);

        // Execute
        worker.process(event);

        // Verify
        ArgumentCaptor<SaleOrderContext> contextCaptor = ArgumentCaptor.forClass(SaleOrderContext.class);
        verify(mockProvider1).getAdditionalParams(contextCaptor.capture(), any(BiFunction.class));

        SaleOrderContext capturedContext = contextCaptor.getValue();
        assertEquals("encounter-uuid-123", capturedContext.getEncounterUuid());
        assertEquals("patient-uuid-456", capturedContext.getPatientUuid());
    }

    @Test
    public void shouldAddParametersFromMultipleProviders() throws IOException {
        // Setup
        Map<String, Object> params1 = new HashMap<>();
        params1.put("field1", "value1");

        Map<String, Object> params2 = new HashMap<>();
        params2.put("field2", "value2");

        Map<String, SaleOrderParameterProvider> providers = new LinkedHashMap<>();
        providers.put("provider1", mockProvider1);
        providers.put("provider2", mockProvider2);

        when(applicationContext.getBeansOfType(SaleOrderParameterProvider.class))
                .thenReturn(providers);
        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(params1);
        when(mockProvider2.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(params2);

        Event event = new Event("event-id", "content", "test", feedUrl, new Date());
        worker = new OpenERPSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClient,
                urlPrefix, atomFeedProperties, isOdoo16, applicationContext);

        // Execute
        worker.process(event);

        // Verify
        ArgumentCaptor<OpenERPRequest> requestCaptor = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPContext).execute(requestCaptor.capture(), eq(odooURL));

        OpenERPRequest capturedRequest = requestCaptor.getValue();
        List<Parameter> parameters = capturedRequest.getParameters();

        // Verify both providers' parameters were added
        assertTrue("Should contain field1",
                parameters.stream().anyMatch(p -> "field1".equals(p.getName()) && "value1".equals(p.getValue())));
        assertTrue("Should contain field2",
                parameters.stream().anyMatch(p -> "field2".equals(p.getName()) && "value2".equals(p.getValue())));

        // Verify both providers were called
        verify(mockProvider1).getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class));
        verify(mockProvider2).getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class));
    }

    @Test
    public void shouldHandleComplexObjectsFromExtensionProviders() throws IOException {
        // Setup
        Map<String, Object> complexParams = new HashMap<>();
        Map<String, String> nestedObject = new HashMap<>();
        nestedObject.put("nestedKey", "nestedValue");
        complexParams.put("complexField", nestedObject);

        Map<String, SaleOrderParameterProvider> providers = new HashMap<>();
        providers.put("provider1", mockProvider1);

        when(applicationContext.getBeansOfType(SaleOrderParameterProvider.class))
                .thenReturn(providers);
        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(complexParams);

        Event event = new Event("event-id", "content", "test", feedUrl, new Date());
        worker = new OpenERPSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClient,
                urlPrefix, atomFeedProperties, isOdoo16, applicationContext);

        // Execute
        worker.process(event);

        // Verify
        ArgumentCaptor<OpenERPRequest> requestCaptor = ArgumentCaptor.forClass(OpenERPRequest.class);
        verify(openERPContext).execute(requestCaptor.capture(), eq(odooURL));

        OpenERPRequest capturedRequest = requestCaptor.getValue();
        List<Parameter> parameters = capturedRequest.getParameters();

        // Verify complex parameter was serialized and added
        Optional<Parameter> complexParam = parameters.stream()
                .filter(p -> "complexField".equals(p.getName()))
                .findFirst();

        assertTrue("Should contain complexField", complexParam.isPresent());
        assertTrue("Complex field should be JSON serialized",
                complexParam.get().getValue().contains("nestedKey"));
        assertTrue("Complex field should contain nested value",
                complexParam.get().getValue().contains("nestedValue"));
    }

    @Test
    public void shouldNotFailWhenProviderReturnsEmptyMap() throws IOException {
        // Setup
        Map<String, SaleOrderParameterProvider> providers = new HashMap<>();
        providers.put("provider1", mockProvider1);

        when(applicationContext.getBeansOfType(SaleOrderParameterProvider.class))
                .thenReturn(providers);
        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(Collections.emptyMap());

        Event event = new Event("event-id", "content", "test", feedUrl, new Date());
        worker = new OpenERPSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClient,
                urlPrefix, atomFeedProperties, isOdoo16, applicationContext);

        // Execute
        worker.process(event);

        // Verify - should complete successfully
        verify(openERPContext).execute(any(OpenERPRequest.class), eq(odooURL));
        verify(mockProvider1).getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class));
    }

    @Test
    public void shouldInvokeExtensionProvidersOnlyForConsumableEvents() throws IOException {
        // Setup - encounter without orders (should not be consumed)
        String nonConsumableEncounter = "{\n" +
                "    \"encounterUuid\": \"encounter-uuid-123\",\n" +
                "    \"patientUuid\": \"patient-uuid-456\",\n" +
                "    \"visitUuid\": \"visit-uuid-789\",\n" +
                "    \"orders\": [],\n" +
                "    \"drugOrders\": []\n" +
                "}";

        Map<String, SaleOrderParameterProvider> providers = new HashMap<>();
        providers.put("provider1", mockProvider1);

        when(applicationContext.getBeansOfType(SaleOrderParameterProvider.class))
                .thenReturn(providers);

        Event event = new Event("event-id", "content", "test", feedUrl, new Date());
        // Override default mock to return non-consumable encounter
        when(webClient.get(any(URI.class))).thenAnswer(invocation -> {
            URI uri = invocation.getArgument(0);
            if (uri.toString().contains("content")) {
                return nonConsumableEncounter;
            }
            return null;
        });

        worker = new OpenERPSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClient,
                urlPrefix, atomFeedProperties, isOdoo16, applicationContext);

        // Execute
        worker.process(event);

        // Verify - provider should not be invoked because event is not consumable
        verify(mockProvider1, never()).getAdditionalParams(any(), any());
        verify(openERPContext, never()).execute(any(), any());
    }
}
