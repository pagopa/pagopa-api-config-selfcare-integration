package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.starter.repository.HealthCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    @Autowired
    HealthCheckRepository healthCheckRepository;

    public boolean checkDatabaseConnection() {
        try {
            return healthCheckRepository.health().isPresent();
        } catch (DataAccessResourceFailureException e) {
           return false;
        }
    }
}
