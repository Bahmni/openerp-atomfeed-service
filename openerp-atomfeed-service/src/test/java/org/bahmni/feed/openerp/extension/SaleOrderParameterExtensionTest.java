package org.bahmni.feed.openerp.extension;

import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.encounter.OpenMRSEncounter;
import org.bahmni.odooconnect.extensions.SaleOrderContext;
import org.bahmni.odooconnect.extensions.SaleOrderParameterProvider;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SaleOrderParameterExtensionTest {

    @Mock
    private OpenMRSWebClient webClient;

    @Mock
    private SaleOrderParameterProvider mockProvider1;

    @Mock
    private SaleOrderParameterProvider mockProvider2;

    private OpenMRSEncounter testEncounter;
    private String urlPrefix = "http://localhost:8080";

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        String encounterJson = "{" +
                "\"encounterUuid\": \"encounter-uuid-123\"," +
                "\"patientUuid\": \"patient-uuid-456\"" +
                "}";
        testEncounter = ObjectMapperRepository.objectMapper.readValue(encounterJson, OpenMRSEncounter.class);
    }

    @Test
    public void shouldReturnEmptyListWhenNoProvidersExist() {
        List<SaleOrderParameterProvider> providers = Collections.emptyList();

        List<Parameter> result = SaleOrderParameterExtension.getExtensionParameters(
                testEncounter, providers, webClient, urlPrefix);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnParametersFromSingleProvider() {
        Map<String, Object> extensionParams = new HashMap<>();
        extensionParams.put("customField1", "value1");
        extensionParams.put("customField2", "value2");

        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);

        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(extensionParams);

        List<Parameter> result = SaleOrderParameterExtension.getExtensionParameters(
                testEncounter, providers, webClient, urlPrefix);

        assertNotNull(result);
        assertEquals(2, result.size());

        Parameter param1 = result.stream()
                .filter(p -> p.getName().equals("customField1"))
                .findFirst()
                .orElse(null);
        assertNotNull(param1);
        assertEquals("value1", param1.getValue());

        Parameter param2 = result.stream()
                .filter(p -> p.getName().equals("customField2"))
                .findFirst()
                .orElse(null);
        assertNotNull(param2);
        assertEquals("value2", param2.getValue());
    }

    @Test
    public void shouldReturnParametersFromMultipleProviders() {
        Map<String, Object> params1 = new HashMap<>();
        params1.put("field1", "value1");

        Map<String, Object> params2 = new HashMap<>();
        params2.put("field2", "value2");

        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);
        providers.add(mockProvider2);

        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(params1);
        when(mockProvider2.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(params2);

        List<Parameter> result = SaleOrderParameterExtension.getExtensionParameters(
                testEncounter, providers, webClient, urlPrefix);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void shouldPassCorrectContextToProvider() {
        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);

        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(Collections.emptyMap());

        SaleOrderParameterExtension.getExtensionParameters(testEncounter, providers, webClient, urlPrefix);

        ArgumentCaptor<SaleOrderContext> contextCaptor = ArgumentCaptor.forClass(SaleOrderContext.class);
        verify(mockProvider1).getAdditionalParams(contextCaptor.capture(), any(BiFunction.class));

        SaleOrderContext capturedContext = contextCaptor.getValue();
        assertEquals("encounter-uuid-123", capturedContext.getEncounterUuid());
        assertEquals("patient-uuid-456", capturedContext.getPatientUuid());
    }

    @Test
    public void shouldHandleComplexObjectsAsParameters() {
        Map<String, Object> complexParams = new HashMap<>();
        Map<String, String> nestedObject = new HashMap<>();
        nestedObject.put("key1", "value1");
        nestedObject.put("key2", "value2");
        complexParams.put("complexField", nestedObject);

        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);

        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(complexParams);

        List<Parameter> result = SaleOrderParameterExtension.getExtensionParameters(
                testEncounter, providers, webClient, urlPrefix);

        assertNotNull(result);
        assertEquals(1, result.size());
        Parameter complexParam = result.get(0);
        assertEquals("complexField", complexParam.getName());
        assertTrue(complexParam.getValue().contains("key1"));
        assertTrue(complexParam.getValue().contains("value1"));
    }

    @Test
    public void shouldHandleMixedStringAndComplexParameters() {
        Map<String, Object> mixedParams = new HashMap<>();
        mixedParams.put("stringField", "simpleValue");
        mixedParams.put("objectField", Collections.singletonMap("nested", "value"));

        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);

        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(mixedParams);

        List<Parameter> result = SaleOrderParameterExtension.getExtensionParameters(
                testEncounter, providers, webClient, urlPrefix);

        assertNotNull(result);
        assertEquals(2, result.size());

        Parameter stringParam = result.stream()
                .filter(p -> p.getName().equals("stringField"))
                .findFirst()
                .orElse(null);
        assertNotNull(stringParam);
        assertEquals("simpleValue", stringParam.getValue());

        Parameter objectParam = result.stream()
                .filter(p -> p.getName().equals("objectField"))
                .findFirst()
                .orElse(null);
        assertNotNull(objectParam);
        assertTrue(objectParam.getValue().contains("nested"));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionWhenProviderFails() {
        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);

        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenThrow(new RuntimeException("Provider failed"));

        SaleOrderParameterExtension.getExtensionParameters(testEncounter, providers, webClient, urlPrefix);
    }

    @Test
    public void shouldHandleProviderReturningEmptyMap() {
        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);

        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(Collections.emptyMap());

        List<Parameter> result = SaleOrderParameterExtension.getExtensionParameters(
                testEncounter, providers, webClient, urlPrefix);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldHandleMultipleProvidersWithOneReturningEmpty() {
        Map<String, Object> params2 = new HashMap<>();
        params2.put("field2", "value2");

        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);
        providers.add(mockProvider2);

        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(Collections.emptyMap());
        when(mockProvider2.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenReturn(params2);

        List<Parameter> result = SaleOrderParameterExtension.getExtensionParameters(
                testEncounter, providers, webClient, urlPrefix);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("field2", result.get(0).getName());
        assertEquals("value2", result.get(0).getValue());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenProviderAttemptsToAccessNonOpenMRSEndpoint() throws Exception {
        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);

        // Provider tries to call a non-OpenMRS endpoint which should be blocked
        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenAnswer(invocation -> {
                    BiFunction<String, Class<?>, Object> getFunction = invocation.getArgument(1);
                    // Attempt to access external URL - should fail
                    getFunction.apply("/external/api/data", String.class);
                    return Collections.emptyMap();
                });

        SaleOrderParameterExtension.getExtensionParameters(testEncounter, providers, webClient, urlPrefix);
    }

    @Test
    public void shouldAllowProviderToAccessOpenMRSEndpoints() throws Exception {
        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);

        String mockResponse = "{\"data\": \"test\"}";
        when(webClient.get(eq(urlPrefix + "/openmrs/ws/rest/v1/patient/123"), eq(String.class)))
                .thenReturn(mockResponse);

        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenAnswer(invocation -> {
                    BiFunction<String, Class<?>, Object> getFunction = invocation.getArgument(1);
                    // Access valid OpenMRS endpoint - should succeed
                    Object response = getFunction.apply("/openmrs/ws/rest/v1/patient/123", String.class);
                    assertEquals(mockResponse, response);

                    Map<String, Object> params = new HashMap<>();
                    params.put("patientData", response);
                    return params;
                });

        List<Parameter> result = SaleOrderParameterExtension.getExtensionParameters(
                testEncounter, providers, webClient, urlPrefix);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(webClient).get(eq(urlPrefix + "/openmrs/ws/rest/v1/patient/123"), eq(String.class));
    }

    @Test
    public void shouldPrependUrlPrefixWhenCallingWebClient() throws Exception {
        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);

        String mockPatientData = "{\"uuid\": \"patient-123\"}";
        when(webClient.get(eq("http://localhost:8080/openmrs/ws/rest/v1/patient/patient-uuid-456"), eq(String.class)))
                .thenReturn(mockPatientData);

        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenAnswer(invocation -> {
                    BiFunction<String, Class<?>, Object> getFunction = invocation.getArgument(1);
                    Object response = getFunction.apply("/openmrs/ws/rest/v1/patient/patient-uuid-456", String.class);

                    Map<String, Object> params = new HashMap<>();
                    params.put("enrichedData", response);
                    return params;
                });

        List<Parameter> result = SaleOrderParameterExtension.getExtensionParameters(
                testEncounter, providers, webClient, urlPrefix);

        assertNotNull(result);
        assertEquals(1, result.size());

        // Verify that urlPrefix was prepended to the URL
        verify(webClient).get(eq("http://localhost:8080/openmrs/ws/rest/v1/patient/patient-uuid-456"), eq(String.class));
    }

    @Test(expected = RuntimeException.class)
    public void shouldBlockAccessToRelativePathsOutsideOpenMRS() throws Exception {
        List<SaleOrderParameterProvider> providers = new ArrayList<>();
        providers.add(mockProvider1);

        when(mockProvider1.getAdditionalParams(any(SaleOrderContext.class), any(BiFunction.class)))
                .thenAnswer(invocation -> {
                    BiFunction<String, Class<?>, Object> getFunction = invocation.getArgument(1);
                    // Attempt to access path outside OpenMRS - should fail
                    getFunction.apply("/admin/secrets", String.class);
                    return Collections.emptyMap();
                });

        SaleOrderParameterExtension.getExtensionParameters(testEncounter, providers, webClient, urlPrefix);
    }
}
