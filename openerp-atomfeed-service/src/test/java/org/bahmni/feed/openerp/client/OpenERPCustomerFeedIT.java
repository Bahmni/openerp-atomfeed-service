package org.bahmni.feed.openerp.client;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import com.sun.syndication.io.FeedException;
import org.bahmni.feed.openerp.ObjectMapperRepository;
import org.bahmni.feed.openerp.OpenERPAtomFeedProperties;
import org.bahmni.feed.openerp.job.Jobs;
import org.bahmni.feed.openerp.job.OpenERPCustomerFeedJob;
import org.bahmni.feed.openerp.job.SimpleFeedJob;
import org.bahmni.feed.openerp.worker.OpenERPCustomerServiceEventWorker;
import org.bahmni.feed.openerp.worker.WorkerFactory;
import org.bahmni.openerp.web.client.strategy.OpenERPContext;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticationResponse;
import org.bahmni.webclients.openmrs.OpenMRSAuthenticator;
import org.ict4h.atomfeed.Configuration;
import org.ict4h.atomfeed.client.domain.Marker;
import org.ict4h.atomfeed.client.repository.AllFailedEvents;
import org.ict4h.atomfeed.client.repository.AllFeeds;
import org.ict4h.atomfeed.client.repository.jdbc.AllMarkersJdbcImpl;
import org.ict4h.atomfeed.client.service.AtomFeedClient;
import org.ict4h.atomfeed.client.service.FeedEnumerator;
import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.ict4h.atomfeed.jdbc.JdbcUtils;
import org.ict4h.atomfeed.server.transaction.AtomFeedSpringTransactionSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-openerpTest.xml"})
public class OpenERPCustomerFeedIT {

    @Mock
    private   AllFeeds allFeedsMock;

    @Mock
    private OpenERPAtomFeedProperties atomFeedProperties;

    @Mock
    private OpenMRSAuthenticator openMRSAuthenticator;

    @Mock
    private OpenMRSWebClient webClient;

    @Mock
    private AllFailedEvents allFailedEvents;

    @Mock
    private OpenERPContext openERPContext;

    @Mock
    private WorkerFactory workerFactory;
    
    @Mock
    private OpenERPCustomerServiceEventWorker openERPCustomerServiceEventWorker;

    @Mock
    private WebClientProvider webClientProvider;



    private URI notificationsUri;
    private URI firstFeedUri;
    private URI secondFeedUri;
    private URI recentFeedUri;
    Feed first;
    Feed second;
    Feed last;
    
    OpenERPAllMarkersJdbcImpl allMarkersJdbc;
    
    @Autowired
    private AtomFeedSpringTransactionSupport transactionSupport;

    @Before
    public void setUp() throws URISyntaxException {
        initMocks(this);

        allMarkersJdbc = new OpenERPAllMarkersJdbcImpl(transactionSupport);

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

        last.setOtherLinks(Arrays.asList(new Link[]{getLink("prev-archive", secondFeedUri),getLink("self", recentFeedUri),getLink("via", recentFeedUri)}));

        second.setOtherLinks(Arrays.asList(getLink("prev-archive", firstFeedUri), getLink("next-archive", recentFeedUri),getLink("self", secondFeedUri),getLink("via", secondFeedUri)));

        first.setOtherLinks(Arrays.asList(new Link[]{getLink("next-archive", secondFeedUri),getLink("self", firstFeedUri),getLink("via", firstFeedUri)}));
    }

    @After
    public void tearDown() throws Exception {
        allMarkersJdbc.delete(notificationsUri);
    }

    private Link getLink(String archiveType, URI uri) {
        Link link = new Link();
        link.setRel(archiveType);
        link.setHref(uri.toString());
        return link;
    }

    private List<Entry> getEntries(int startNum, int endNum) {
        List<Entry> entries = new ArrayList<>();
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

    /**
     * TODO: WORST TEST EVER. 
     * 1 - Write proper Integration test or mock. Don't do half way
     * 2 - Ensure that the test passes. 
     * 3 - Write proper assertion
     */
    @Test
    public void shouldCreateCustomerInOpenERP() throws URISyntaxException, FeedException {
        String feedUrl = "http://host/patients/notifications";
        String feedname = "customer.feed.generator.uri";
        
        when(atomFeedProperties.getFeedUri(feedname)).thenReturn(feedUrl);
        when(allFeedsMock.getFor(notificationsUri)).thenReturn(last);
        when(allFeedsMock.getFor(recentFeedUri)).thenReturn(last);
        when(allFeedsMock.getFor(secondFeedUri)).thenReturn(second);
        when(allFeedsMock.getFor(firstFeedUri)).thenReturn(first);

        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("patientResource.xml");
        String patientResource = new Scanner(resourceAsStream).useDelimiter("\\Z").next();

        when(webClient.get((URI) any())).thenReturn(patientResource);
        
        when(atomFeedProperties.getAuthenticationURI()).thenReturn("http://mrs.auth.uri");

        when(atomFeedProperties.getOpenMRSUser()).thenReturn("mrsuser");
        when(atomFeedProperties.getOpenMRSPassword()).thenReturn("mrspwd");
        when(atomFeedProperties.getAuthenticationURI()).thenReturn("http://mrs.auth.uri");
        when(atomFeedProperties.getMaxFailedEvents()).thenReturn(10);

        OpenMRSAuthenticationResponse authenticationResponse = new OpenMRSAuthenticationResponse();
        authenticationResponse.setAuthenticated(true);
        authenticationResponse.setSessionId("sessionIdValue");

        when(openMRSAuthenticator.authenticate("mrsuser", "mrspwd", ObjectMapperRepository.objectMapper)).thenReturn(authenticationResponse);
        when(webClientProvider.getWebClient(any(Jobs.class))).thenReturn(webClient);
        when(workerFactory.getWorker(Jobs.CUSTOMER_FEED, feedUrl, openERPContext, "http://mrs.auth.uri")).thenReturn(openERPCustomerServiceEventWorker);

        AtomFeedClientHelper clientHelper = mock(AtomFeedClientHelper.class);
        AtomFeedClient atomFeedClient = new AtomFeedClient(allFeedsMock, allMarkersJdbc, allFailedEvents, FeedClientFactory.atomFeedProperties(atomFeedProperties),
                transactionSupport, new URI(feedUrl), openERPCustomerServiceEventWorker);
        
        when(clientHelper.getAtomFeedClient(Jobs.CUSTOMER_FEED)).
                thenReturn(atomFeedClient);
        
        SimpleFeedJob openMRSFeedJob = new SimpleFeedJob(clientHelper);
        OpenERPCustomerFeedJob feedJob = new OpenERPCustomerFeedJob(openMRSFeedJob);

        feedJob.processFeed();

        Marker marker = allMarkersJdbc.get(notificationsUri);
        assertNotNull(marker);
        assertThat(marker.getLastReadEntryId(), is("9") );
    }

    private Entry createEntry() {
        Entry entry = new Entry();
        ArrayList<Content> contents = new ArrayList<Content>();
        Content content = new Content();
        String value ="/Test";
        content.setValue(String.format("%s%s%s", "<![CDATA[", value, "]]>"));
        contents.add(content);
        entry.setContents(contents);

        return entry;
    }

    class OpenERPAllMarkersJdbcImpl extends AllMarkersJdbcImpl{
        public OpenERPAllMarkersJdbcImpl(JdbcConnectionProvider connectionProvider) {
            super(connectionProvider);
        }

        public void delete(URI feedUri) throws SQLException {
            Connection connection = null;
            PreparedStatement stmt;
            try {
                connection = transactionSupport.getConnection();
                String sql = String.format("delete from %s where feed_uri = ?",
                        JdbcUtils.getTableName(Configuration.getInstance().getSchema(), "markers"));
                stmt = connection.prepareStatement(sql);
                stmt.setString(1, feedUri.toString());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if(connection != null){
                    connection.close();
                }
            }
        }
    }
}



