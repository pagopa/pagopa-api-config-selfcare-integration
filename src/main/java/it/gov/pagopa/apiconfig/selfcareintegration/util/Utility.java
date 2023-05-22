package it.gov.pagopa.apiconfig.selfcareintegration.util;

import it.gov.pagopa.apiconfig.selfcareintegration.model.PageInfo;
import org.springframework.data.domain.Page;

public class Utility {

  private Utility() {}

  public static <T> PageInfo buildPageInfo(Page<T> page) {
    return PageInfo.builder()
        .page(page.getNumber())
        .limit(page.getSize())
        .totalPages(page.getTotalPages())
        .itemsFound(page.getNumberOfElements())
        .build();
  }
}
