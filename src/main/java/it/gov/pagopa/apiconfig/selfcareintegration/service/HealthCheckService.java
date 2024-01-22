package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.starter.repository.HealthCheckRepository;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    final HealthCheckRepository healthCheckRepository;

    public HealthCheckService(HealthCheckRepository healthCheckRepository) {
        this.healthCheckRepository = healthCheckRepository;
    }

    public boolean checkDatabaseConnection() {
        try {
            return healthCheckRepository.health().isPresent();
        } catch (DataAccessResourceFailureException e) {
            return false;
        }
    }
}
