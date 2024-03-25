package org.bahmni.openerp.web;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseCheckerTest {

    private ResponseChecker responseChecker;

    @Before
    public void setUp() {
        responseChecker = new ResponseChecker();
    }

    @Test
    public void shouldThrowOdooRestExceptionWhenResponseStatusIsNot2xx() {
        ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);
        when(mockResponseEntity.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        when(mockResponseEntity.getBody()).thenReturn("Response body");

        OdooRestException exception = assertThrows(OdooRestException.class, () -> {
            responseChecker.checkResponse(mockResponseEntity);
        });
        assertEquals("Response status: 404 NOT_FOUND", exception.getMessage());
    }

    @Test
    public void shouldThrowOdooRestExceptionWhenResponseIsNull() {
        ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);
        when(mockResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponseEntity.getBody()).thenReturn(null);

        OdooRestException exception = assertThrows(OdooRestException.class, () -> {
            responseChecker.checkResponse(mockResponseEntity);
        });
        assertEquals("Response is null", exception.getMessage());
    }

    @Test
    public void shouldThrowOdooRestExceptionWhenResponseHasErrorInResult() {
        ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);
        when(mockResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponseEntity.getBody()).thenReturn("{\"result\":{\"error\":\"Error message\",\"status\":500}}");

        OdooRestException exception = assertThrows(OdooRestException.class, () -> {
            responseChecker.checkResponse(mockResponseEntity);
        });
        assertEquals("Error found in result. Response status: 500. Error message: Error message", exception.getMessage());
    }

    @Test
    public void shouldThrowOdooRestExceptionWhenResponseHasError() {
        ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);
        when(mockResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponseEntity.getBody()).thenReturn("{\"error\":{\"message\":\"Error message\",\"status\":500}}");

        OdooRestException exception = assertThrows(OdooRestException.class, () -> {
            responseChecker.checkResponse(mockResponseEntity);
        });
        assertEquals("Error found in response. Response status: 500. Error message: Error message", exception.getMessage());
    }

    @Test
    public void shouldThrowOdooRestExceptionWhenResponseIsEmpty() {
        ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);
        when(mockResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponseEntity.getBody()).thenReturn("");

        OdooRestException exception = assertThrows(OdooRestException.class, () -> {
            responseChecker.checkResponse(mockResponseEntity);
        });
        assertEquals("Response is empty", exception.getMessage());
    }

    @Test
    public void shouldThrowOdooSessionExpiredExceptionWhenOdooSessionExpires() {
        ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);
        when(mockResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponseEntity.getBody()).thenReturn("{\"error\":{\"message\":\"Error message\",\"code\":100}}");

        OdooSessionExpiredException exception = assertThrows(OdooSessionExpiredException.class, () -> {
            responseChecker.checkResponse(mockResponseEntity);
        });
        assertEquals("Odoo Session Expired", exception.getMessage());
    }

    @Test
    public void shouldThrowOdooSessionExpiredExceptionWhenStatusIsForbidden() {
        ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);
        when(mockResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(mockResponseEntity.getBody()).thenReturn("{\"error\":{\"message\":\"Error message\",\"status\":403}}");

        OdooSessionExpiredException exception = assertThrows(OdooSessionExpiredException.class, () -> {
            responseChecker.checkResponse(mockResponseEntity);
        });
        assertEquals("Odoo Session Expired", exception.getMessage());
    }
}
