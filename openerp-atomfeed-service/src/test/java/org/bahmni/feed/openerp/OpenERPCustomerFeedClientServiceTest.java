package org.bahmni.feed.openerp;

import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import com.sun.syndication.io.FeedException;
import org.bahmni.feed.openerp.event.EventWorkerFactory;
import org.bahmni.feed.openerp.event.OpenERPCustomerServiceEventWorker;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticationResponse;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticator;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.ict4h.atomfeed.jdbc.PropertiesJdbcConnectionProvider;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpenERPCustomerFeedClientServiceTest {

    private AtomFeedClient atomFeedClient;
    private String feedLink = "feedLink";
    private URI feedUri;
    private AllFeeds allFeedsMock;
    private AllFailedEvents allFailedEvents;
    private Entry entry1;
    private Entry entry2;
    private OpenERPAtomFeedProperties atomFeedProperties;
    private EventWorkerFactory workerFactory;
    private OpenERPClient openERPClient;


    @Before
    public void setUp() throws URISyntaxException {
        feedUri = new URI("http://myFeedUri");
        allFeedsMock = mock(AllFeeds.class);
        allFailedEvents = mock(AllFailedEvents.class);
        atomFeedProperties = mock(OpenERPAtomFeedProperties.class);
        workerFactory = mock(EventWorkerFactory.class);
        atomFeedClient = mock(AtomFeedClient.class);
        openERPClient = mock(OpenERPClient.class);
    }

    @Test
    public void shouldCallOpenERPEventWorkerOnProcessingFeed() throws URISyntaxException, FeedException {
        Feed feed = setupFeed();
        OpenERPCustomerServiceEventWorker openERPEventWorker = new OpenERPCustomerServiceEventWorker("www.openmrs.com",openERPClient, null, null);

        when(atomFeedProperties.getOpenMRSUser()).thenReturn("mrsuser");
        when(atomFeedProperties.getOpenMRSPassword()).thenReturn("mrspwd");
        when(atomFeedProperties.getFeedUri("customer.feed.generator.uri")).thenReturn("http://www.openerp.com");
        when(atomFeedProperties.getAuthenticationURI()).thenReturn("http://mrs.auth.uri");

        when(atomFeedProperties.getAuthenticationURI()).thenReturn("http://mrs.auth.uri");

        when(workerFactory.getWorker("openerp.customer.service", "http://www.openerp.com", openERPClient,
                atomFeedProperties.getConnectionTimeoutInMilliseconds(), atomFeedProperties.getReplyTimeoutInMilliseconds(), null, null, null)).thenReturn(openERPEventWorker);
        when(allFeedsMock.getFor(feedUri)).thenReturn(feed);
        when(allFailedEvents.getNumberOfFailedEvents(feedUri.toString())).thenReturn(0);

        OpenMRSAuthenticationResponse authenticationResponse = new OpenMRSAuthenticationResponse();
        authenticationResponse.setAuthenticated(true);
        authenticationResponse.setSessionId("sessionIdValue");
        OpenMRSAuthenticator mrsAuthenticator = mock(OpenMRSAuthenticator.class);
        when(mrsAuthenticator.authenticate("mrsuser", "mrspwd", ObjectMapperRepository.objectMapper)).thenReturn(authenticationResponse);

        JdbcConnectionProvider jdbcConnectionProvider = new PropertiesJdbcConnectionProvider();

        OpenERPCustomerFeedClientService feedClientService =
                new OpenERPCustomerFeedClientService(atomFeedProperties,jdbcConnectionProvider,workerFactory,openERPClient,"customer.feed.generator.uri",
                        allFeedsMock, null,allFailedEvents, mock(TaskMonitor.class), mrsAuthenticator);
        feedClientService.processFeed();

//        verify(atomFeedClient, atLeastOnce()).processEvents(new URI("http://www.openerp.com"), openERPEventWorker);
    }


    private Feed setupFeed() {
        entry1 = new Entry(); entry1.setId("id1");
        entry2 = new Entry(); entry2.setId("id2");
        return getFeed(entry1, entry2);
    }

    private Feed getFeed(Entry... entries) {
        ArrayList mutableEntries = new ArrayList();
        mutableEntries.addAll(Arrays.asList(entries));
        Feed feed = new Feed();
        feed.setOtherLinks(Arrays.asList(new Link[]{getLink("self", feedLink)}));
        feed.setEntries(mutableEntries);
        return feed;
    }

    private Link getLink(String archiveType, String uri) {
        Link link = new Link();
        link.setRel(archiveType);
        link.setHref(uri);
        return link;
    }
}
