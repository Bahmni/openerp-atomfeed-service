package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticationResponse;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticator;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.server.transaction.AtomFeedSpringTransactionSupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-openerpTest.xml"})
public class OpenERPSaleOrderEventWorkerIT {
    @Mock
    private OpenERPAtomFeedProperties atomFeedProperties;
    @Mock
    private OpenERPContext openERPContext;
    @Mock
    private OpenMRSAuthenticator openMRSAuthenticator;
    @Mock
    private OpenMRSWebClient webClient;
    @Mock
    private AllFailedEvents allFailedEvents;

    @Autowired
    private AtomFeedSpringTransactionSupport transactionSupport;
    private Boolean isOdoo16;

    @Before
    public void setUp() throws URISyntaxException {
        initMocks(this);
        openMRSAuthenticator = mock(OpenMRSAuthenticator.class);
    }

    //TODO: Fix these test.
    @Ignore
    @Test
    public void create_SaleOrder_In_ERP_For_Lab_Order(){
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("encounterResourceForLabOrder.json");
        String encounterResource = new Scanner(resourceAsStream).useDelimiter("\\Z").next();

        when(webClient.get((URI) any())).thenReturn(encounterResource);

        when(atomFeedProperties.getAuthenticationURI()).thenReturn("http://mrs.auth.uri");

        when(atomFeedProperties.getOpenMRSUser()).thenReturn("mrsuser");
        when(atomFeedProperties.getOpenMRSPassword()).thenReturn("mrspwd");
        when(atomFeedProperties.getAuthenticationURI()).thenReturn("http://mrs.auth.uri");

        when(webClient.get((URI) any())).thenReturn(encounterResource);

        when(atomFeedProperties.getAuthenticationURI()).thenReturn("http://mrs.auth.uri");

        when(atomFeedProperties.getOpenMRSUser()).thenReturn("mrsuser");
        when(atomFeedProperties.getOpenMRSPassword()).thenReturn("mrspwd");
        when(atomFeedProperties.getAuthenticationURI()).thenReturn("http://mrs.auth.uri");

        OpenMRSAuthenticationResponse authenticationResponse = new OpenMRSAuthenticationResponse();
        authenticationResponse.setAuthenticated(true);
        authenticationResponse.setSessionId("sessionIdValue");
        when(openMRSAuthenticator.authenticate("mrsuser", "mrspwd", ObjectMapperRepository.objectMapper)).thenReturn(authenticationResponse);

        HashMap fieldMap = new HashMap();
        fieldMap.put("uuid","12345");
//        when(openERPContext.search(any(String.class), any(Vector.class))).thenReturn(new Object[]{12345});
//        when(openERPContext.read(any(String.class),any(Vector.class), any(Vector.class))).thenReturn(new Object[]{fieldMap});

        String feedUrl = "http://xxxx/encounter/feed/2";
        String odooURL = "http://odooURL";
        OpenERPSaleOrderEventWorker eventWorker = new OpenERPSaleOrderEventWorker(feedUrl,odooURL,openERPContext,webClient,"http://mrs.auth.uri", atomFeedProperties, isOdoo16, null);
        Event event = new Event("Test",feedUrl);
        eventWorker.process(event);
    }

    //TODO: Fix these test.
    @Ignore
    @Test
    public void create_SaleOrder_For_Bed_In_ERP_When_Bed_Is_Assigned() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("encounterResourceForBedAssignment.json");
        String bedAssignmentResource = new Scanner(resourceAsStream).useDelimiter("\\Z").next();

        when(webClient.get((URI) any())).thenReturn(bedAssignmentResource);
        when(atomFeedProperties.getAuthenticationURI()).thenReturn("http://mrs.auth.uri");
        when(atomFeedProperties.getOpenMRSUser()).thenReturn("mrsuser");
        when(atomFeedProperties.getOpenMRSPassword()).thenReturn("mrspwd");
        when(atomFeedProperties.getAuthenticationURI()).thenReturn("http://mrs.auth.uri");

        OpenMRSAuthenticationResponse authenticationResponse = new OpenMRSAuthenticationResponse();
        authenticationResponse.setAuthenticated(true);
        authenticationResponse.setSessionId("sessionIdValue");
        when(openMRSAuthenticator.authenticate("mrsuser", "mrspwd", ObjectMapperRepository.objectMapper)).thenReturn(authenticationResponse);

        HashMap fieldMap = new HashMap();
        fieldMap.put("uuid","12345");
//        when(openERPContext.search(any(String.class), any(Vector.class))).thenReturn(new Object[]{12345});
//        when(openERPContext.read(any(String.class),any(Vector.class), any(Vector.class))).thenReturn(new Object[]{fieldMap});

        String feedUrl = "http://xxxx/encounter/feed/2";
        String odooURL = "http://odooURL";
        Event event = new Event("Test", feedUrl);

        OpenERPSaleOrderEventWorker eventWorker = new OpenERPSaleOrderEventWorker(feedUrl, odooURL, openERPContext, webClient,"http://mrs.auth.uri", atomFeedProperties, isOdoo16, null);
        eventWorker.process(event);

    }
}
