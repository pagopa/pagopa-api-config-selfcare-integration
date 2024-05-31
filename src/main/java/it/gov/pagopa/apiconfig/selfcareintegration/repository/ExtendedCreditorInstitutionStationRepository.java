package it.gov.pagopa.apiconfig.selfcareintegration.repository;

import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import it.gov.pagopa.apiconfig.starter.repository.PaStazionePaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@SuppressWarnings(
    "java:S100") // Disabled naming convention rule for method name to use Spring Data interface
@Repository
public interface ExtendedCreditorInstitutionStationRepository extends PaStazionePaRepository {

  List<PaStazionePa> findByFkPa(@Param("fkPa") Long creditorInstitutionId);

  Page<PaStazionePa> findByFkPa(@Param("fkPa") Long creditorInstitutionId, Pageable pageable);

  List<PaStazionePa> findByFkStazione(@Param("fkStazione") Stazioni station);
}
