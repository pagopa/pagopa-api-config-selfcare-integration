package it.gov.pagopa.apiconfig.selfcareintegration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import it.gov.pagopa.apiconfig.Application;
import org.junit.jupiter.api.Test;

class ApplicationTest {

  @Test
  void applicationContextLoaded() {
    assertTrue(true); // it just tests that an error has not occurred
  }

  @Test
  void applicationContextTest() {
    Application.main(new String[] {});
    assertTrue(true); // it just tests that an error has not occurred
  }
}
