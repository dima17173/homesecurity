package com.impl.homesecurity.util;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

/**
 * Created by dima.
 * Creation date 25.10.18.
 */
@NoArgsConstructor
public class HeaderUtil {

    private static final String APPLICATION_NAME = "homeSecurityApp";

    private static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-homeSecurityApp-alert", message);
        headers.add("X-homeSecurityApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert(APPLICATION_NAME + "." + entityName + ".deleted", param);
    }
}
