package org.bahmni.feed.openerp.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.feed.openerp.domain.encounter.OpenMRSEncounter;
import org.bahmni.odooconnect.extensions.SaleOrderContext;
import org.bahmni.odooconnect.extensions.SaleOrderParameterProvider;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;


public class SaleOrderParameterExtension {

    public static List<Parameter> getExtensionParameters(OpenMRSEncounter encounter, List<SaleOrderParameterProvider> providers, OpenMRSWebClient webClient) {
        List<Parameter> parameters = new ArrayList<>();
        for (SaleOrderParameterProvider provider : providers) {
            Map<String, Object> additionalParams = provider.getAdditionalParams(getSaleOrderContext(encounter), buildOpenMRSGetFunction(webClient));
            try {
                parameters.addAll(mapToParameters(additionalParams));
            } catch (Exception e) {
                LoggerFactory.getLogger(SaleOrderParameterExtension.class)
                        .error("Failed getting parameters from extension provider: {}", provider.getClass().getName(), e);
                throw new RuntimeException(e);
            }
        }
        return parameters;
    }

    private static SaleOrderContext getSaleOrderContext(OpenMRSEncounter encounter) {
        return SaleOrderContext.builder()
                .encounterUuid(encounter.getEncounterUuid())
                .patientUuid(encounter.getPatientUuid())
                .build();
    }

    private static BiFunction<String, Class<?>, Object> buildOpenMRSGetFunction(OpenMRSWebClient openMRSWebClient) {
        return (url, returnType) -> {
            try {
                return openMRSWebClient.get(url, returnType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static List<Parameter> mapToParameters(Map<String, Object> parameters) throws JsonProcessingException {
        List<Parameter> parameterList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (entry.getValue() instanceof String)
                parameterList.add(new Parameter(entry.getKey(), (String) entry.getValue()));
            else
                parameterList.add(new Parameter(entry.getKey(), ObjectMapperRepository.objectMapper.writeValueAsString(entry.getValue())));
        }
        return parameterList;
    }


}
