package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppError;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CIStationRelation;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetail;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionStationSegregationCodes;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionStationSegregationCodesList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedCreditorInstitutionStationRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.specification.PaStazionePaSpecifications;
import it.gov.pagopa.apiconfig.selfcareintegration.specification.StationSpecifications;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Utility;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import it.gov.pagopa.apiconfig.starter.repository.IntermediariPaRepository;
import it.gov.pagopa.apiconfig.starter.repository.StazioniRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BrokersService {

    private final StazioniRepository stazioniRepository;

    private final IntermediariPaRepository intermediariPaRepository;

    private final ExtendedCreditorInstitutionStationRepository paStazionePaRepository;

    private final ModelMapper modelMapper;

    public BrokersService(
            StazioniRepository stazioniRepository,
            IntermediariPaRepository intermediariPaRepository,
            ExtendedCreditorInstitutionStationRepository paStazionePaRepository,
            ModelMapper modelMapper
    ) {
        this.stazioniRepository = stazioniRepository;
        this.intermediariPaRepository = intermediariPaRepository;
        this.paStazionePaRepository = paStazionePaRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Retrieve the paginated list of station by broker tax code. the list can be filtered by station id and/or
     * creditor institution's tax code
     *
     * @param brokerCode broker's tax code
     * @param stationId  station id
     * @param ciTaxCode  creditor institution's tax code
     * @param pageable   page request
     * @return the paginated list of stations
     */
    public StationDetailsList getStationsDetailsFromBroker(
            @NotNull String brokerCode,
            String stationId,
            String ciTaxCode,
            Pageable pageable
    ) {
        IntermediariPa broker = getBrokerIfExists(brokerCode);
        Page<Stazioni> queryResult = stazioniRepository
                .findAll(StationSpecifications.filter(broker.getObjId(), stationId, ciTaxCode), pageable);

        List<StationDetails> stations = queryResult.stream()
                .map(station -> modelMapper.map(station, StationDetails.class))
                .toList();
        return StationDetailsList.builder()
                .pageInfo(Utility.buildPageInfo(queryResult))
                .stationsDetailsList(stations)
                .build();
    }

    public CreditorInstitutionDetails getCreditorInstitutionsAssociatedToBroker(
            @NotNull String brokerId,
            Boolean enabled,
            Pageable pageable
    ) {

        Page<PaStazionePa> page = paStazionePaRepository.findAll(PaStazionePaSpecifications.filter(brokerId, enabled), pageable);

        return CreditorInstitutionDetails.builder()
                .creditorInstitutions(page.stream()
                        .map(entity -> modelMapper.map(entity, CreditorInstitutionDetail.class))
                        .toList())
                .pageInfo(Utility.buildPageInfo(page))
                .build();
    }

    /**
     * Retrieve the list of all segregation codes associated to the stations of the specified broker.
     * The segregation codes are grouped by the creditor institutions associated to the stations.
     *
     * @param brokerTaxCode broker's tax code
     * @return the grouped list of segregation codes
     */
    public CreditorInstitutionStationSegregationCodesList getCreditorInstitutionsSegregationCodeAssociatedToBroker(
            String brokerTaxCode
    ) {
        Map<String, List<String>> groupedList =
                this.paStazionePaRepository.findAllByBrokerTaxCode(brokerTaxCode).parallelStream()
                        .filter(elem -> elem.getSegregazione() != null)
                        .map(elem -> modelMapper.map(elem, CIStationRelation.class))
                        .collect(
                                Collectors.groupingBy(CIStationRelation::getCiTaxCode,
                                        Collectors.mapping(
                                                CIStationRelation::getSegregationCode,
                                                Collectors.toList()
                                        ))
                        );

        return CreditorInstitutionStationSegregationCodesList.builder()
                .ciStationCodes(groupedList
                        .keySet()
                        .parallelStream()
                        .map(key ->
                                CreditorInstitutionStationSegregationCodes.builder()
                                        .ciTaxCode(key)
                                        .segregationCodes(groupedList.get(key))
                                        .build()
                        )
                        .toList())
                .build();
    }

    protected IntermediariPa getBrokerIfExists(String brokerCode) throws AppException {
        Optional<IntermediariPa> result = intermediariPaRepository.findByIdIntermediarioPa(brokerCode);
        if (result.isEmpty()) {
            throw new AppException(AppError.BROKER_NOT_FOUND, brokerCode);
        }
        return result.get();
    }
}
