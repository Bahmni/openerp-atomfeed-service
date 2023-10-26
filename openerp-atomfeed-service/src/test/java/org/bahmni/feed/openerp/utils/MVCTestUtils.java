package org.bahmni.feed.openerp.utils;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

public class MVCTestUtils {
    public static MockMvc mockMvc(Object controller) {
        StandaloneMockMvcBuilder mockMvcBuilder = MockMvcBuilders.standaloneSetup(controller);

        return mockMvcBuilder.build();
    }
}
