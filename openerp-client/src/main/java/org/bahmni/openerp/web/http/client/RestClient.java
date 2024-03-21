package org.bahmni.openerp.web.http.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bahmni.openerp.web.OdooSessionExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bahmni.openerp.web.OdooRestException;
import org.bahmni.openerp.web.OpenERPException;
import org.bahmni.openerp.web.ResponseChecker;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.function.Consumer;

public class RestClient {
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
    private WebClient webClient;
    private final String baseURL;
    private final String database;
    private final String username;
    private final String password;
    private final int connectionTimeout;
    private static ResponseCookie session;

    public RestClient(String baseURL, String username, String password, int connectionTimeout, String database) {
        this.database = database;
        this.baseURL = baseURL;
        this.username = username;
        this.password = password;
        this.connectionTimeout = connectionTimeout;
    }

    private void login() {
        String requestBody = buildLoginRequest();
        WebClient client = getWebClient(baseURL);
        HttpHeaders headers = getHttpHeaders();
        Consumer<HttpHeaders> consumer = httpHeaders -> httpHeaders.addAll(headers);
        try {
            client.post().uri("web/session/authenticate").headers(consumer).bodyValue(requestBody).exchangeToMono(loginResponse -> {
                if (loginResponse.statusCode().is2xxSuccessful()) {
                    session = loginResponse.cookies().get("session_id").get(0);
                }
                return loginResponse.bodyToMono(String.class);
            }).timeout(Duration.ofMillis(connectionTimeout)).block();
            logger.debug("\n-----------------------------------------------------Login Initiated-----------------------------------------------------\n* Request : {}\n* Session Id : {}\n-----------------------------------------------------End of Login-----------------------------------------------------", requestBody, session);
        } catch (Exception e) {
            logger.warn("Failed to login for user {}", username);
            throw new OpenERPException(String.format("Failed to login. The login user is : %s, no session cookie set.", username));
        }
    }

    public String post(String URL, String requestBody) {
        if(!isSessionValid())
            login();
        try {
            logger.debug("Post Data: {}", requestBody);
            WebClient client = getWebClient(baseURL);
            HttpHeaders headers = getHttpHeaders();
            Consumer < HttpHeaders > consumer = httpHeaders -> httpHeaders.addAll(headers);
            ResponseEntity<String> responseEntity = client.post().uri(URL).headers(consumer).cookie("session_id", session.getValue()).bodyValue(requestBody).retrieve().toEntity(String.class).timeout(Duration.ofMillis(connectionTimeout)).block();
            ResponseChecker.checkResponse(responseEntity);
            String response = responseEntity.getBody();
            logger.debug("\n-----------------------------------------------------{} Initiated-----------------------------------------------------\n* Session : {}\n* Request : {}\n* Response : {}\n-----------------------------------------------------End of {}-----------------------------------------------------", URL, session, requestBody, response, URL);
            logger.debug("Post Data output: {}", response);
            return response;
        } catch (OdooRestException | JsonProcessingException e) {
            logger.error("Post call to {} failed", URL, e);
            logger.error("Post data: {}", requestBody);
            throw new RuntimeException("Post call to " + URL + " failed", e);
        } catch (OdooSessionExpiredException e) {
            logger.warn("Session expired for user {}. Recreating user session.", username);
            login();
            return post(URL, requestBody);
        }
    }

    private boolean isSessionValid() {
        return session != null && !session.getMaxAge().isNegative();
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