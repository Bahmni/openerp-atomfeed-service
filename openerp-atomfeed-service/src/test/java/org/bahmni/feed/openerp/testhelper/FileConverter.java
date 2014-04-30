package org.bahmni.feed.openerp.testhelper;

import java.io.InputStream;
import java.util.Scanner;

public class FileConverter {

    public static String convertToString(String jsonFileName) {
        InputStream resourceAsStream = FileConverter.class.getClassLoader().getResourceAsStream(jsonFileName);
        return new Scanner(resourceAsStream).useDelimiter("\\Z").next();
    }
}
