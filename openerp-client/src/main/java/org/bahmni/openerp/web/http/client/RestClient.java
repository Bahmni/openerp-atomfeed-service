package org.bahmni.openerp.web.http.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bahmni.openerp.web.OdooRestException;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.ResponseChecker;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.function.Consumer;

public class RestClient {
    private static final Logger logger = LogManager.getLogger(RestClient.class);
    private WebClient webClient;
    private final String baseURL;
    private final String database;
    private final String username;
    private final String password;
    private final int connectionTimeout;
    private String sessionId;

    public RestClient(String baseURL, String username, String password, int connectionTimeout, String database) {
        this.database = database;
        this.baseURL = baseURL;
        this.username = username;
        this.password = password;
        this.connectionTimeout = connectionTimeout;
    }

    private void login() {
        if (sessionId == null) {
            String requestBody = buildLoginRequest();
            WebClient client = getWebClient(baseURL);
            HttpHeaders headers = getHttpHeaders();
            Consumer < HttpHeaders > consumer = httpHeaders -> httpHeaders.addAll(headers);
            try {
                client.post().uri("web/session/authenticate").headers(consumer).bodyValue(requestBody).exchangeToMono(loginResponse -> {
                if(loginResponse.statusCode().is2xxSuccessful()) {
                    try {
                        sessionId = loginResponse.cookies().get("session_id").get(0).getValue();
                        logger.debug("Login session_id: {}", sessionId);
                    } catch (Exception e) {
                        logger.warn("Failed to login for user {}", username);
                        throw new OpenERPException(String.format("Failed to login. The login user is : %s", username));
                    }
                }
                return loginResponse.bodyToMono(String.class);
                }).timeout(Duration.ofMillis(connectionTimeout)).block();
                logger.debug("\n-----------------------------------------------------Login Initiated-----------------------------------------------------\n* Request : {}\n* Session Id : {}\n-----------------------------------------------------End of Login-----------------------------------------------------", requestBody, sessionId);
            } catch (Exception e) {
                logger.warn("Failed to login for user {}", username);
                throw new OpenERPException(String.format("Failed to login. The login user is : %s", username));
            }
        }
    }

    public String post(String URL, String requestBody) {
        try {
            login();
            logger.debug("Post Data: {}", requestBody);
            WebClient client = getWebClient(baseURL);
            HttpHeaders headers = getHttpHeaders();
            Consumer < HttpHeaders > consumer = httpHeaders -> httpHeaders.addAll(headers);
            ResponseEntity<String> responseEntity = client.post().uri(URL).headers(consumer).cookie("session_id", sessionId).bodyValue(requestBody).retrieve().toEntity(String.class).timeout(Duration.ofMillis(connectionTimeout)).block();
            ResponseChecker.checkResponse(responseEntity);
            String response = responseEntity.getBody();
            logger.debug("\n-----------------------------------------------------{} Initiated-----------------------------------------------------\n* Cookies : {}\n* Request : {}\n* Response : {}\n-----------------------------------------------------End of {}-----------------------------------------------------", URL, sessionId, requestBody, response, URL);
            logger.debug("Post Data output: {}", response);
            return response;
        } catch (OdooRestException e) {
            logger.error("Could not post to {}", URL, e);
            logger.error("Post data: {}", requestBody);
            throw e;
        } catch (Exception e) {
            logger.error("Could not post to {}", URL, e);
            logger.error("Post data: {}", requestBody);
            throw new RuntimeException("Could not post message", e);
        }
    }
    private WebClient getWebClient(String baseURL) {
        if (webClient == null) {
            webClient = WebClient.builder()
                    .baseUrl(baseURL)
                    .build();
        }
        return webClient;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }

    private String buildLoginRequest() {
        try {
            HashMap < String, Object > params = new HashMap < > ();
            params.put("db", database);
            params.put("login", username);
            params.put("password", password);
            HashMap < String, Object > requestBody = new HashMap < > ();
            requestBody.put("params", params);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new OpenERPException(e);
        }
    }

}