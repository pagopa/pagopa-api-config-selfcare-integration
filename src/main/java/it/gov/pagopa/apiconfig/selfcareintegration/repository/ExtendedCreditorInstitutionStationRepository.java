package it.gov.pagopa.apiconfig.selfcareintegration.repository;

import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.ICIStationRelation;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import it.gov.pagopa.apiconfig.starter.repository.PaStazionePaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@SuppressWarnings(
    "java:S100") // Disabled naming convention rule for method name to use Spring Data interface
@Repository
public interface ExtendedCreditorInstitutionStationRepository extends PaStazionePaRepository {

  List<PaStazionePa> findByFkPa(@Param("fkPa") Long creditorInstitutionId);

  Page<PaStazionePa> findByFkPa(@Param("fkPa") Long creditorInstitutionId, Pageable pageable);

  List<PaStazionePa> findByFkStazioneAndPaIn(@Param("fkStazione") Stazioni station, @Param("pa") List<Pa> paList);

  @Query(value =
          "SELECT p.idDominio as idDominio, paspa.segregazione as segregazione " +
                  "FROM PaStazionePa paspa " +
                  "JOIN Stazioni s ON paspa.fkStazione = s.objId " +
                  "JOIN Pa p ON paspa.fkPa = p.objId " +
                  "JOIN IntermediariPa ip ON s.fkIntermediarioPa = ip.objId " +
                  "WHERE ip.idIntermediarioPa = :brokerTaxCode")
  List<ICIStationRelation> findAllByBrokerTaxCode(@Param("brokerTaxCode") String brokerTaxCode);
}
