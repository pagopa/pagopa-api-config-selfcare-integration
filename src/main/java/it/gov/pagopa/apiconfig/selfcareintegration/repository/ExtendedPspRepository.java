package it.gov.pagopa.apiconfig.selfcareintegration.repository;

import it.gov.pagopa.apiconfig.starter.entity.IbanMaster;
import it.gov.pagopa.apiconfig.starter.entity.Psp;
import it.gov.pagopa.apiconfig.starter.repository.IbanMasterRepository;
import it.gov.pagopa.apiconfig.starter.repository.PspRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings(
        "java:S100") // Disabled naming convention rule for method name to use Spring Data interface
@Repository
public interface ExtendedPspRepository extends PspRepository {

    List<Psp> findAllByEnabled(Boolean enabled);
}
