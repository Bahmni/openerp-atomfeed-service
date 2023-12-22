package org.bahmni.openerp.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

public class ResponseChecker {
    public static void checkResponse(ResponseEntity<String> responseEntity,String URL) throws JsonProcessingException {
        String response = responseEntity.getBody();
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new OpenERPException(String.format("Failed to post to %s. Response status: %s", URL, responseEntity.getStatusCode()));
        }
        if (response == null) {
            throw new OpenERPException(String.format("Failed to post to %s. Response is null", URL));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response);
        if (jsonResponse.has("result")) {
            JsonNode result = jsonResponse.get("result");
            if (result.has("error")) {
                String errorMsg = result.get("error").asText();
                int status = result.get("status").asInt();
                throw new OpenERPException(String.format("Failed to post to %s. Response status: %s. Error message: %s", URL, status, errorMsg));
            }
        }
        else if (jsonResponse.has("error")) {
            JsonNode error = jsonResponse.get("error");
            String errorMsg = error.get("message").asText();
            int status = error.get("status").asInt();
            throw new OpenERPException(String.format("Failed to post to %s. Response status: %s. Error message: %s", URL, status, errorMsg));
        }
        else{
            throw new OpenERPException(String.format("Failed to post to %s.", URL));
        }
    }
}
