package it.gov.pagopa.apiconfig.selfcareintegration.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.gov.pagopa.apiconfig.selfcareintegration.Application;

@SpringBootTest(classes = Application.class)
class HealthCheckRepositoryTest {

  @Autowired
  private HealthCheckRepository healthCheckRepository;

  @Test
  void check() {
    assertTrue(healthCheckRepository.health().isPresent());
  }
}
