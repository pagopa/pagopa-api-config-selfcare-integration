package it.gov.pagopa.apiconfig.selfcareintegration.util;

import it.gov.pagopa.apiconfig.selfcareintegration.model.PageInfo;
import org.springframework.data.domain.Page;

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
}
