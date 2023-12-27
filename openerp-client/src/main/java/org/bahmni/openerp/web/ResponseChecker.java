package org.bahmni.openerp.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

public class ResponseChecker {
    public static void checkResponse(ResponseEntity<String> responseEntity) throws JsonProcessingException {
        String response = responseEntity.getBody();
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new OdooRestException(String.format("Response status: %s", responseEntity.getStatusCode()));
        }
        if (response == null) {
            throw new OdooRestException(String.format("Response is null"));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response);
        if (jsonResponse.has("result")) {
            JsonNode result = jsonResponse.get("result");
            if (result.has("error")) {
                String errorMsg = result.get("error").asText();
                int status = result.get("status").asInt();
                throw new OdooRestException(String.format("Error found in result. Response status: %s.", status, errorMsg));
            }
        }
        else if (jsonResponse.has("error")) {
            JsonNode error = jsonResponse.get("error");
            String errorMsg = error.get("message").asText();
            int status = error.get("status").asInt();
            throw new OdooRestException(String.format("Error found in response. Response status: %s. Error message: %s", status, errorMsg));
        }
        else{
            throw new OdooRestException(String.format("Response is empty"));
        }
    }
}
