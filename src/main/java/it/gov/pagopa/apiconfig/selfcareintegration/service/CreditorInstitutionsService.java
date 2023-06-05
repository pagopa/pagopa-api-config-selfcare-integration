package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppError;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.CIAssociatedCode;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.CIAssociatedCodeList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedCreditorInstitutionStationRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Utility;
import it.gov.pagopa.apiconfig.starter.repository.PaRepository;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.stream.LongStream;

@Service
public class CreditorInstitutionsService {

  @Value("${sc-int.application_code.max_value}")
  private Integer applicationCodeMaxValue;

  @Autowired
  private ExtendedCreditorInstitutionStationRepository ciStationRepository;

  @Autowired
  private PaRepository paRepository;

  @Autowired
  private ModelMapper modelMapper;

  public StationDetailsList getStationsDetailsFromCreditorInstitution(
      @NotNull String creditorInstitutionCode, Pageable pageable) {
    Pa pa = getPaIfExists(creditorInstitutionCode);
    Page<PaStazionePa> queryResult =
        ciStationRepository.findByFkPa(pa.getObjId(), pageable);
    List<StationDetails> stations =
        queryResult.stream()
            .map(
                paStazionePa -> modelMapper.map(paStazionePa.getFkStazione(), StationDetails.class))
            .collect(Collectors.toList());
    return StationDetailsList.builder()
        .pageInfo(Utility.buildPageInfo(queryResult))
        .stationsDetailsList(stations)
        .build();
  }


  public CIAssociatedCodeList getApplicationCodesFromCreditorInstitution(
      @NotNull String creditorInstitutionCode,
      boolean getUsed) {
    Pa pa = getPaIfExists(creditorInstitutionCode);
    List<PaStazionePa> queryResult = ciStationRepository.findByFkPa(pa.getObjId());
    Map<Long, PaStazionePa> alreadyUsedApplicationCodes = queryResult.stream()
        .collect(Collectors.toMap(PaStazionePa::getProgressivo, station -> station));

    List<CIAssociatedCode> usedCodes = new LinkedList<>();
    List<CIAssociatedCode> unusedCodes = new LinkedList<>();

    LongStream.rangeClosed(0, applicationCodeMaxValue)
        .boxed()
        .forEach(
            applicationCodeFromSequence -> {
              // generate model to be added
              CIAssociatedCode ciAssociatedCode = CIAssociatedCode.builder()
                  .code(String.valueOf(applicationCodeFromSequence < 10 ? "0".concat(
                      String.valueOf(applicationCodeFromSequence)) : applicationCodeFromSequence))
                  .build();
              // choose the list where add the model object
              if (alreadyUsedApplicationCodes.containsKey(applicationCodeFromSequence)) {
                ciAssociatedCode.setStationName(
                    alreadyUsedApplicationCodes.get(applicationCodeFromSequence).getFkStazione()
                        .getIdStazione());
                usedCodes.add(ciAssociatedCode);
              } else {
                unusedCodes.add(ciAssociatedCode);
              }
            }
        );

    return CIAssociatedCodeList.builder()
        .usedCodes(getUsed ? usedCodes : null)
        .unusedCodes(unusedCodes)
        .build();
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
