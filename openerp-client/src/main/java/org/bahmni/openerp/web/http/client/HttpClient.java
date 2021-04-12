package org.bahmni.openerp.web.http.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClient {
    private static final Logger logger = LogManager.getLogger(HttpClient.class);
    private RestTemplate restTemplate;

    private boolean isTimeoutSet;

    @Autowired
    public HttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String post(String url, String formPostData) {
        try {
            logger.debug("Post Data: " + formPostData);
            HttpEntity<String> stringHttpEntity = new HttpEntity<String>(formPostData, getHttpHeaders());
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, stringHttpEntity, String.class);
            String response = responseEntity != null ? responseEntity.getBody() : "";
            logger.debug("Post Data output: " + response);
            return response;
        } catch (Exception e) {
            logger.error("Could not post  to " + url, e);
            logger.error("Post data: " + formPostData);
            throw new RuntimeException("Could not post message", e);
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", "application/xml; charset=UTF-8");
        return httpHeaders;
    }

    public void setTimeout(int replyTimeoutInMilliseconds) {
        if (!isTimeoutSet) {
            try {
                HttpComponentsClientHttpRequestFactory requestFactoryWithTimeout = new HttpComponentsClientHttpRequestFactory();
                requestFactoryWithTimeout.setReadTimeout(replyTimeoutInMilliseconds);
                restTemplate.setRequestFactory(requestFactoryWithTimeout);

                isTimeoutSet = true;
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
