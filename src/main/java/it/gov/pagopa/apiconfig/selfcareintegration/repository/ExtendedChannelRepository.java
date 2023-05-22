package it.gov.pagopa.apiconfig.selfcareintegration.repository;

import it.gov.pagopa.apiconfig.starter.entity.Canali;
import it.gov.pagopa.apiconfig.starter.repository.CanaliRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@SuppressWarnings(
    "java:S100") // Disabled naming convention rule for method name to use Spring Data interface
@Repository
public interface ExtendedChannelRepository extends CanaliRepository {

  @Query(
      value =
          "SELECT DISTINCT c FROM Canali c WHERE (c.fkIntermediarioPsp.objId = :fkIntermediario) ORDER"
              + " BY c.idCanale")
  Page<Canali> findAllByFiltersOrderById(
      @Param("fkIntermediario") Long brokerId, Pageable pageable);

  @Query(
      value =
          "select distinct c from Canali c join c.fkIntermediarioPsp where (:fkIntermediario is null or c.fkIntermediarioPsp.idIntermediarioPsp"
              + " = :fkIntermediario) and (:idCanale is null or upper(c.idCanale) like"
              + " concat('%', upper(:idCanale), '%')) order by c.idCanale")
  Page<Canali> findAllByFiltersOrderById(
      @Param("fkIntermediario") Long brokerId,
      @Param("idCanale") String channelId,
      Pageable pageable);
}
