package it.gov.pagopa.apiconfig.selfcareintegration.util;

import lombok.experimental.UtilityClass;

public class Constants {

    @UtilityClass
    public static class DateTimeFormat {
        public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    }

    public static final String HEADER_REQUEST_ID = "X-Request-Id";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_TIME_WITHOUT_SECONDS_FORMAT = "yyyy-MM-dd'T'HH:mm'Z'";

    private Constants() {
    }
}
