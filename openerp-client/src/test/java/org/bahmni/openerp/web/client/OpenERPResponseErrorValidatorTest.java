package org.bahmni.openerp.web.client;

import org.junit.Test;

public class OpenERPResponseErrorValidatorTest {

    @Test(expected = RuntimeException.class)
    public void shouldThrowErrorIfResponseHasFaultCode() throws Exception {
        OpenERPResponseErrorValidator.checkForError(errorResponse());
    }

    @Test
    public void shouldNotThrowErrorIfResponseIsSuccess() throws Exception {
        OpenERPResponseErrorValidator.checkForError(successResponse());
    }

    private String errorResponse() {
        return "<?xml version='1.0'?>\n" +
                "<methodResponse>\n" +
                "  <fault>\n" +
                "    <value>\n" +
                "      <struct>\n" +
                "        <member>\n" +
                "          <name>faultCode</name>\n" +
                "          <value><string>global name 'success' is not defined</string></value>\n" +
                "        </member>\n" +
                "        <member>\n" +
                "          <name>faultString</name>\n" +
                "          <value>\n" +
                "            <string>Traceback (most recent call last):\n" +
                "              File \"/usr/lib/python2.6/site-packages/openerp-7.0_20130301_002301-py2.6.egg/openerp/service/wsgi_server.py\", line 82, in xmlrpc_return\n" +
                "                result = openerp.netsvc.dispatch_rpc(service, method, params)\n" +
                "              File \"/usr/lib/python2.6/site-packages/openerp-7.0_20130301_002301-py2.6.egg/openerp/netsvc.py\", line 295, in dispatch_rpc\n" +
                "                result = ExportService.getService(service_name).dispatch(method, params)\n" +
                "              File \"/usr/lib/python2.6/site-packages/openerp-7.0_20130301_002301-py2.6.egg/openerp/service/web_services.py\", line 614, in dispatch\n" +
                "                return getattr(object, method)(cr, uid, *args, **kw)\n" +
                "              File \"/usr/lib/python2.6/site-packages/openerp-7.0_20130301_002301-py2.6.egg/openerp/addons/bahmni_atom_feed/atom_feed_client.py\", line 89, in process_event\n" +
                "                return {success: True}\n" +
                "              NameError: global name 'success' is not defined\n" +
                "            </string>\n" +
                "          </value>\n" +
                "        </member>\n" +
                "      </struct>\n" +
                "    </value>\n" +
                "  </fault>\n" +
                "</methodResponse>\n";
    }

    private String successResponse() {
        return "<?xml version='1.0'?>\n" +
                "<methodResponse>\n" +
                "  <params>\n" +
                "    <param>\n" +
                "      <value>\n" +
                "        <struct>\n" +
                "          <member>\n" +
                "            <name>success</name>\n" +
                "            <value><boolean>1</boolean></value>\n" +
                "          </member>\n" +
                "        </struct>\n" +
                "      </value>\n" +
                "    </param>\n" +
                "  </params>\n" +
                "</methodResponse>\n";
    }
}
