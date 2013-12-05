package org.bahmni.feed.openerp.worker;

import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.client.OpenMRSWebClient;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticationResponse;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticator;
import org.ict4h.atomfeed.client.domain.Event;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.ict4h.atomfeed.jdbc.PropertiesJdbcConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-openerpTest.xml"})
public class OpenERPSaleOrderEventWorkerIT {
    @Mock
    private OpenERPAtomFeedProperties atomFeedProperties;

    @Mock
    private OpenERPClient openERPClient;


    @Mock
    private OpenMRSAuthenticator openMRSAuthenticator;

    @Mock
    private OpenMRSWebClient webClient;

    @Mock
    private AllFailedEvents allFailedEvents;

    private JdbcConnectionProvider jdbcConnectionProvider;

    @Before
    public void setUp() throws URISyntaxException {
        initMocks(this);
        jdbcConnectionProvider = new PropertiesJdbcConnectionProvider();
        openMRSAuthenticator = mock(OpenMRSAuthenticator.class);
    }

    @Test
    public void shouldCreateSaleOrderInERP(){
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("admissionEncounterResource.json");
        String encounterResource = new Scanner(resourceAsStream).useDelimiter("\\Z").next();

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
        when(openERPClient.search(any(String.class), any(Vector.class))).thenReturn(new Object[]{12345});
        when(openERPClient.read(any(String.class),any(Vector.class), any(Vector.class))).thenReturn(new Object[]{fieldMap});

        String feedUrl = "http://xxxx/encounter/feed/2";
        OpenERPSaleOrderEventWorker eventWorker = new OpenERPSaleOrderEventWorker(feedUrl,openERPClient,webClient,"http://mrs.auth.uri");
        Event event = new Event("Test",feedUrl);
        eventWorker.process(event);

    }
}
