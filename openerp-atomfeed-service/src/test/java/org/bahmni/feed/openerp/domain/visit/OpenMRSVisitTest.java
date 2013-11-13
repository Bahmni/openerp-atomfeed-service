package org.bahmni.feed.openerp.domain.visit;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class OpenMRSVisitTest {

    private final String visitType = "REG";
    private final String uuid = "354asdsfhdgs";

    @Test
    public void shouldConcatenateTypeAndStartDate()  {
        OpenMRSVisit visit = new OpenMRSVisit(uuid,visitType,"2013-09-23T23:59:59.000+0530");
        Assert.assertThat(visit.getDescription(), is("REG" + " " + "23/09/2013 23:59:59"));
    }

    @Test
    public void shouldNotFailIfStartDateCannotBeParsed()  {
        String nonParseableDate = "non-parseable-date";
        OpenMRSVisit visit = new OpenMRSVisit(uuid, visitType, nonParseableDate);
        Assert.assertThat(visit.getDescription(), is("REG" + " " + nonParseableDate));
    }
}
