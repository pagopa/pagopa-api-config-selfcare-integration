package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppError;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.CIAssociatedCode;
import it.gov.pagopa.apiconfig.selfcareintegration.model.code.CIAssociatedCodeList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedCreditorInstitutionStationRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Utility;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.repository.PaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@Transactional
public class CreditorInstitutionsService {

    @Value("${sc-int.application_code.max_value}")
    private Integer applicationCodeMaxValue;

    @Value("${sc-int.segregation_code.max_value}")
    private Integer segregationCodeMaxValue;

    private final ExtendedCreditorInstitutionStationRepository ciStationRepository;

    private final PaRepository paRepository;

    private final ModelMapper modelMapper;

    public CreditorInstitutionsService(ExtendedCreditorInstitutionStationRepository ciStationRepository, PaRepository paRepository, ModelMapper modelMapper) {
        this.ciStationRepository = ciStationRepository;
        this.paRepository = paRepository;
        this.modelMapper = modelMapper;
    }

    public StationDetailsList getStationsDetailsFromCreditorInstitution(
            @NotNull String creditorInstitutionCode, Pageable pageable) {
        Pa pa = getPaIfExists(creditorInstitutionCode);
        Page<PaStazionePa> queryResult = ciStationRepository.findByFkPa(pa.getObjId(), pageable);
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
            @NotNull String creditorInstitutionCode, boolean getUsed) {
        Pa pa = getPaIfExists(creditorInstitutionCode);
        List<PaStazionePa> queryResult = ciStationRepository.findByFkPa(pa.getObjId());
        Map<Long, PaStazionePa> alreadyUsedApplicationCodes =
                queryResult.stream()
                        .filter(station -> station.getProgressivo() != null)
                        .collect(Collectors.toMap(PaStazionePa::getProgressivo, station -> station));
        return extractUsedAndUnusedCodes(alreadyUsedApplicationCodes, applicationCodeMaxValue, getUsed);
    }

    public CIAssociatedCodeList getSegregationCodesFromCreditorInstitution(
            @NotNull String creditorInstitutionCode, boolean getUsed, String service) {
        String serviceSubstringToBeSearched = service != null ? service.toLowerCase() : null;
        Pa pa = getPaIfExists(creditorInstitutionCode);
        List<PaStazionePa> queryResult = ciStationRepository.findByFkPa(pa.getObjId());
        Map<Long, PaStazionePa> alreadyUsedApplicationCodes =
                queryResult.stream()
                        .filter(station -> station.getSegregazione() != null)
                        .collect(Collectors.toMap(PaStazionePa::getSegregazione, station -> station));
        // get the set of codes to be obfuscated by service search. If passed service is null, the set
        // is empty and all the element will be returned.
        Set<String> codesToBeObfuscated =
                queryResult.stream()
                        .filter(
                                station -> {
                                    String serviceEndpoint = station.getFkStazione().getServizio();
                                    return serviceSubstringToBeSearched != null
                                            && (serviceEndpoint == null
                                            || !serviceEndpoint.toLowerCase().contains(serviceSubstringToBeSearched));
                                })
                        .map(station -> station.getFkStazione().getIdStazione())
                        .collect(Collectors.toSet());
        // retrieving the data removing the ones to be obfuscated
        CIAssociatedCodeList ciAssociatedCodeList =
                extractUsedAndUnusedCodes(alreadyUsedApplicationCodes, segregationCodeMaxValue, getUsed);
        if (ciAssociatedCodeList.getUsedCodes() != null) {
            ciAssociatedCodeList.setUsedCodes(
                    ciAssociatedCodeList.getUsedCodes().stream()
                            .filter(usedCode -> !codesToBeObfuscated.contains(usedCode.getStationName()))
                            .collect(Collectors.toList()));
        }
        return ciAssociatedCodeList;
    }

    private CIAssociatedCodeList extractUsedAndUnusedCodes(
            Map<Long, PaStazionePa> alreadyUsedCodes, long codeMaxValue, boolean includeUsed) {
        List<CIAssociatedCode> usedCodes = new LinkedList<>();
        List<CIAssociatedCode> unusedCodes = new LinkedList<>();
        // extracting the used and unused code analyzing a sequence of N values and filtering by
        // existing association to station
        LongStream.rangeClosed(0, codeMaxValue)
                .boxed()
                .forEach(
                        codeFromSequence -> {
                            // generate model to be added
                            CIAssociatedCode ciAssociatedCode =
                                    CIAssociatedCode.builder()
                                            .code(
                                                    String.valueOf(
                                                            codeFromSequence < 10
                                                                    ? "0".concat(String.valueOf(codeFromSequence))
                                                                    : codeFromSequence))
                                            .build();
                            // choose the list where must be added the model object
                            if (alreadyUsedCodes.containsKey(codeFromSequence)) {
                                ciAssociatedCode.setStationName(
                                        alreadyUsedCodes.get(codeFromSequence).getFkStazione().getIdStazione());
                                usedCodes.add(ciAssociatedCode);
                            } else {
                                unusedCodes.add(ciAssociatedCode);
                            }
                        });
        // generate final object
        return CIAssociatedCodeList.builder()
                .usedCodes(includeUsed ? usedCodes : null)
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
