package org.bahmni.feed.openerp.domain.encounter;

import org.junit.Test;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class OpenERPOrdersTest {

    @Test
    public void shouldRemoveDuplicateOrders() {
        OpenERPOrder order1 = new OpenERPOrder();
        order1.setProductId("1");
        order1.setDateCreated(new Date(1704718128000L));

        OpenERPOrder order2 = new OpenERPOrder();
        order2.setProductId("2");
        order2.setDateCreated(new Date(17047181283000L));

        OpenERPOrder order3 = new OpenERPOrder();
        order3.setProductId("1");
        order3.setDateCreated(new Date(1704718125000L));

        OpenERPOrder order4 = new OpenERPOrder();
        order4.setProductId("3");
        order4.setDateCreated(new Date(1704718131000L));

        OpenERPOrders openerpOrders = new OpenERPOrders("123");
        openerpOrders.add(order1);
        openerpOrders.add(order2);
        openerpOrders.add(order3);
        openerpOrders.add(order4);

        List<OpenERPOrder> result = openerpOrders.removeDuplicateOrders(openerpOrders.getOpenERPOrders());

        List<OpenERPOrder> expected = Arrays.asList(order1, order2, order4);

        assertEquals(expected, result);
    }
}

