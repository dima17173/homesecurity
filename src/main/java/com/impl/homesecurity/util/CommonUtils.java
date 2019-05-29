package com.impl.homesecurity.util;

import org.apache.commons.lang3.StringUtils;
import java.nio.charset.Charset;

/**
 * Created by dima.
 * Creation date 04.01.19.
 */
public class CommonUtils {

    public static String recoverStringEncoding(String uglyString) {
        if (StringUtils.isNotEmpty(uglyString)) {
            Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
            Charset UTF_8 = Charset.forName("UTF-8");
            byte[] isoBytes = uglyString.getBytes(ISO_8859_1);
            return new String(isoBytes, UTF_8);
        }
        return uglyString;
    }
}
