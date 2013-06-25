package org.bahmni.feed.openerp;

import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import org.bahmni.feed.openerp.event.EventWorkerFactory;
import org.bahmni.feed.openerp.event.OpenERPServiceEventWorker;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.AllMarkers;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.EventWorker;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class OpenERPAtomFeedClientServiceTest {

    private AtomFeedClient atomFeedClient;
    private String feedLink = "feedLink";
    private URI feedUri;
    private AllFeeds allFeedsMock;
    private AllFailedEvents allFailedEvents;
    private Entry entry1;
    private Entry entry2;
    private AtomFeedProperties atomFeedProperties;
    private EventWorkerFactory workerFactory;


    @Before
    public void setUp() throws URISyntaxException {
        feedUri = new URI("http://myFeedUri");
        allFeedsMock = mock(AllFeeds.class);
        allFailedEvents = mock(AllFailedEvents.class);
        atomFeedProperties = mock(AtomFeedProperties.class);
        workerFactory = mock(EventWorkerFactory.class);
        atomFeedClient = mock(AtomFeedClient.class);

    }

    @Test
    public void shouldCallOpenERPEventWorkerOnProcessingFeed() throws URISyntaxException {
        Feed feed = setupFeed();
        OpenERPServiceEventWorker openERPEventWorker = new OpenERPServiceEventWorker();
        when(atomFeedProperties.getFeedUri()).thenReturn("http://www.openerp.com");
        when(workerFactory.getWorker("openerp.service")).thenReturn(openERPEventWorker);
        when(allFeedsMock.getFor(feedUri)).thenReturn(feed);
        when(allFailedEvents.getNumberOfFailedEvents(feedUri.toString())).thenReturn(0);


        OpenERPAtomFeedClientService feedClientService = new OpenERPAtomFeedClientService(atomFeedProperties,atomFeedClient,workerFactory);
        feedClientService.processFeed();

        verify(atomFeedClient, atLeastOnce()).processEvents(new URI("http://www.openerp.com"), openERPEventWorker);
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
