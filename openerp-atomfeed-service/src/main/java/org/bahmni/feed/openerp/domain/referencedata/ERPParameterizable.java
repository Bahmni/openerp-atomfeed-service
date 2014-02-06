package org.bahmni.feed.openerp.domain.referencedata;

import org.bahmni.openerp.web.request.builder.Parameter;
import java.util.List;

/**
 * Created by indraneel on 4/2/14.
 */
public interface ERPParameterizable {
    List<Parameter> getParameters(String eventId, String feedURIForLastReadEntry, String feedURI);
}
