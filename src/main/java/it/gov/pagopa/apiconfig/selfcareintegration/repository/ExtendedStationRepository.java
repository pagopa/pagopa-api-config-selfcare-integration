package it.gov.pagopa.apiconfig.selfcareintegration.repository;

import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import it.gov.pagopa.apiconfig.starter.repository.StazioniRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@SuppressWarnings("java:S100") // Disabled naming convention rule for method name to use Spring Data interface
@Repository
public interface ExtendedStationRepository extends StazioniRepository {

    @Query("SELECT distinct s " +
            "FROM Stazioni s, PaStazionePa pas, Pa pa " +
            "WHERE s.objId = pas.fkStazione " +
            "AND pas.fkPa = pa.objId " +
            "AND (:fkBroker is null OR s.fkIntermediarioPa = :fkBroker) " +
            "AND (:ciTaxCode is null OR pa.idDominio = :ciTaxCode) " +
            "ORDER BY s.idStazione")
    Page<Stazioni> findAllFilteredByCITaxCodeOrderById(
            @Param("fkBroker") Long fkBroker,
            @Param("ciTaxCode") String ciTaxCode,
            Pageable pageable);

    @Query("SELECT distinct s " +
            "FROM Stazioni s, PaStazionePa pas, Pa pa " +
            "WHERE s.objId = pas.fkStazione " +
            "AND pas.fkPa = pa.objId " +
            "AND (:fkBroker is null OR s.fkIntermediarioPa = :fkBroker) " +
            "AND (:stationId is null OR upper(s.idStazione) LIKE concat('%', upper(:stationId), '%')) " +
            "AND (:ciTaxCode is null OR pa.idDominio = :ciTaxCode) " +
            "ORDER BY s.idStazione")
    Page<Stazioni> findAllFilteredByStationIdAndCITaxCodeOrderById(
            @Param("fkBroker") Long fkBroker,
            @Param("stationId") String stationId,
            @Param("ciTaxCode") String ciTaxCode,
            Pageable pageable);
}
