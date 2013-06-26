package org.bahmni.feed.openerp.event;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import org.bahmni.openerp.web.client.OpenERPClient;
import org.bahmni.openerp.web.request.OpenERPRequest;
import org.bahmni.openerp.web.request.builder.Parameter;
import org.ict4h.atomfeed.client.domain.Event;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OpenERPCustomerServiceEventWorkerTest {

    OpenERPClient openERPClient;

    @Before
    public void setUp() throws Exception {
        openERPClient = mock(OpenERPClient.class);
    }

    @Test
    public void shouldCallOpenERPClientWithRightParameters(){
        OpenERPCustomerServiceEventWorker customerServiceEventWorker = new OpenERPCustomerServiceEventWorker("www.openmrs.com",openERPClient);
        Event event = new Event(createEntry(),"www.openmrs.com");
        customerServiceEventWorker.process(event);

        verify(openERPClient).execute(createOpenERPRequest());

    }

    private OpenERPRequest createOpenERPRequest() {
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(createParameter("name","Ram Singh","string"));
        parameters.add(createParameter("ref","GAN111111","string"));
        parameters.add(createParameter("village", "Ganiyari", "string"));
        return new OpenERPRequest("atom.event.worker","process_event",parameters);
    }

    private Entry createEntry() {
        Entry entry = new Entry();
        ArrayList<Content> contents = new ArrayList<Content>();
        Content content = new Content();
        String value ="{\"name\": \"Ram Singh\",\"ref\": \"GAN111111\", \"village\":  \"Ganiyari\"}";
        content.setValue(String.format("%s%s%s", "<![CDATA[", value, "]]>"));
        contents.add(content);
        entry.setContents(contents);

        return entry;
    }

    private Parameter createParameter(String name, String value, String type) {
        return new Parameter(name, value, type);
    }

}
