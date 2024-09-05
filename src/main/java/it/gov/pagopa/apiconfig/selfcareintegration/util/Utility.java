package it.gov.pagopa.apiconfig.selfcareintegration.util;

import it.gov.pagopa.apiconfig.selfcareintegration.model.PageInfo;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public class Utility {

    private Utility() {
    }

    public static <T> PageInfo buildPageInfo(Page<T> page) {
        return PageInfo.builder()
                .page(page.getNumber())
                .limit(page.getSize())
                .totalPages(page.getTotalPages())
                .itemsFound(page.getNumberOfElements())
                .totalItems(page.getTotalElements())
                .build();
    }

    /**
     * @param value value to deNullify.
     * @return return empty string if value is null
     */
    public static String deNull(Object value) {
        return Optional.ofNullable(value).orElse("").toString();
    }

    public static Boolean deNull(Boolean value) {
        return Optional.ofNullable(value).orElse(false);
    }

    /**
     * Compute the station's connection flag
     *
     * @param station station model
     * @return true if the station is configured to be synchronous, false otherwise
     */
    public static boolean isConnectionSync(Stazioni station) {
        return (StringUtils.isNotBlank(station.getTargetPath()) && StringUtils.isNotBlank(station.getRedirectIp()))
                || StringUtils.isNotBlank(station.getTargetPathPof());
    }

    /**
     * @param timestamp {@link Timestamp} to convert
     * @return convert timestamp to {@link OffsetDateTime}
     */
    public static OffsetDateTime toOffsetDateTime(Timestamp timestamp) {
        return timestamp != null
                ? OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.UTC)
                : null;
    }



}
