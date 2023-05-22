package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppError;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetails;
import it.gov.pagopa.apiconfig.starter.repository.PaRepository;
import it.gov.pagopa.apiconfig.starter.repository.PaStazionePaRepository;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class CreditorInstitutionsService {

  @Autowired private PaStazionePaRepository paStazionePaRepository;

  @Autowired private PaRepository paRepository;

  @Autowired private ModelMapper modelMapper;

  public StationDetailsList getStationsDetailsFromCreditorInstitution(@NotNull String creditorInstitutionCode){
    Pa pa = getPaIfExists(creditorInstitutionCode);
    List<PaStazionePa> queryResult = paStazionePaRepository.findAllByFkPa(pa.getObjId());
    List<StationDetails> stations = queryResult.stream()
        .map(paStazionePa -> modelMapper.map(paStazionePa.getFkStazione(), StationDetails.class))
        .collect(Collectors.toList());
    return StationDetailsList.builder().stationsDetailsList(stations).build();
  }

  /**
   * @param creditorInstitutionCode idDominio
   * @return return the PA record from DB if Exists
   * @throws AppException if not found
   */
  protected Pa getPaIfExists(String creditorInstitutionCode) throws AppException {
    Optional<Pa> result = paRepository.findByIdDominio(creditorInstitutionCode);
    if (result.isEmpty()) {
      throw new AppException(AppError.CREDITOR_INSTITUTION_NOT_FOUND, creditorInstitutionCode);
    }
    return result.get();
  }

}
