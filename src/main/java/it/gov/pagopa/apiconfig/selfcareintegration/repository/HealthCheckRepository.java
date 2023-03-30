package it.gov.pagopa.apiconfig.selfcareintegration.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class HealthCheckRepository {

    @Autowired
    EntityManager entityManager;

    public Optional<Object> health() {
      // TODO la query dev'essere presa da properties (vedi apiconfig)
        return Optional.of(entityManager.createNativeQuery("select 1 from DUAL").getSingleResult());
    }
}
