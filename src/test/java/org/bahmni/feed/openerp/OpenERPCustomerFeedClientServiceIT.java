package org.bahmni.feed.openerp;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import org.bahmni.feed.openerp.event.EventWorkerFactory;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.jdbc.AllFailedEventsJdbcImpl;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.FeedEnumerator;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.ict4h.atomfeed.jdbc.PropertiesJdbcConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-openerpTest.xml"})
public class OpenERPCustomerFeedClientServiceIT {
    private   AllFeeds allFeedsMock;
    private AtomFeedProperties atomFeedProperties;

    @Autowired
    private OpenERPClient openERPClient;

    private AtomFeedClient atomFeedClient;

    private URI notificationsUri;
    private URI firstFeedUri;
    private URI secondFeedUri;
    private URI recentFeedUri;
    Feed first;
    Feed second;
    Feed last;

    @Before
    public void setUp() throws URISyntaxException {
        atomFeedProperties = mock(AtomFeedProperties.class);
        allFeedsMock = mock(AllFeeds.class);
        JdbcConnectionProvider jdbcConnectionProvider = new PropertiesJdbcConnectionProvider();
        AllMarkersJdbcImpl allMarkersJdbc = new AllMarkersJdbcImpl(jdbcConnectionProvider);

        atomFeedClient =  new AtomFeedClient(allFeedsMock,allMarkersJdbc,new AllFailedEventsJdbcImpl(jdbcConnectionProvider),false);

        first = new Feed();
        second = new Feed();
        last = new Feed();

        first.setEntries(getEntries(1,3));
        second.setEntries(getEntries(4,6));
        last.setEntries(getEntries(7,9));

        notificationsUri = new URI("http://host/patients/notifications");
        recentFeedUri = new URI("http://host/patients/3");
        secondFeedUri = new URI("http://host/patients/2");
        firstFeedUri = new URI("http://host/patients/1");

        last.setOtherLinks(Arrays.asList(new Link[]{getLink("prev-archive", secondFeedUri),getLink("self", recentFeedUri)}));

        second.setOtherLinks(Arrays.asList(getLink("prev-archive", firstFeedUri), getLink("next-archive", recentFeedUri),getLink("self", secondFeedUri)));

        first.setOtherLinks(Arrays.asList(new Link[]{getLink("next-archive", secondFeedUri),getLink("self", firstFeedUri)}));

    }

    private Link getLink(String archiveType, URI uri) {
        Link link = new Link();
        link.setRel(archiveType);
        link.setHref(uri.toString());
        return link;
    }

    private List<Entry> getEntries(int startNum, int endNum) {
        List<Entry> entries = new ArrayList<Entry>();
        for (int i = startNum; i <= endNum; i++) {
            Entry entry = createEntry();
            entry.setId("" + i);
            entries.add(entry);
        }
        return entries;
    }

    private List<String> getEntries(FeedEnumerator feedEnumerator) {
        List<String> entryIds = new ArrayList<String>();
        for(Entry entry : feedEnumerator) {
            entryIds.add(entry.getId());
        }
        return entryIds;
    }


    @Test
    public void shouldCreateCustomerInOpenERP() throws URISyntaxException {
        when(atomFeedProperties.getFeedUri()).thenReturn("http://host/patients/notifications");
        when(allFeedsMock.getFor(notificationsUri)).thenReturn(last);
        when(allFeedsMock.getFor(recentFeedUri)).thenReturn(last);
        when(allFeedsMock.getFor(secondFeedUri)).thenReturn(second);
        when(allFeedsMock.getFor(firstFeedUri)).thenReturn(first);
        when(allFeedsMock.getFor(new URI("https://github.com/ICT4H/atomfeed"))).thenReturn(last);


        OpenERPCustomerFeedClientService feedClientService = new OpenERPCustomerFeedClientService(atomFeedProperties,atomFeedClient,new EventWorkerFactory(),openERPClient);
        feedClientService.processFeed();

    }

    private Entry createEntry() {
        Entry entry = new Entry();
        ArrayList<Content> contents = new ArrayList<Content>();
        Content content = new Content();
        String value ="{\"name\": \"Ram Singh\",\"ref\": \"GAN111133\", \"village\":  \"Ganiyari\"}";
        content.setValue(String.format("%s%s%s", "<![CDATA[", value, "]]>"));
        contents.add(content);
        entry.setContents(contents);

        return entry;
    }

}



