package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppError;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetail;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.specification.PaStazionePaSpecifications;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Utility;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import it.gov.pagopa.apiconfig.starter.entity.Stazioni;
import it.gov.pagopa.apiconfig.starter.repository.IntermediariPaRepository;
import it.gov.pagopa.apiconfig.starter.repository.PaStazionePaRepository;
import it.gov.pagopa.apiconfig.starter.repository.StazioniRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BrokersService {

    @Autowired
    private StazioniRepository stazioniRepository;

    @Autowired
    private IntermediariPaRepository intermediariPaRepository;

    @Autowired
    private PaStazionePaRepository paStazionePaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public StationDetailsList getStationsDetailsFromBroker(
            @NotNull String brokerId, String stationId, Pageable pageable) throws AppException {
        IntermediariPa broker = getBrokerIfExists(brokerId);
        Page<Stazioni> queryResult;
        if (stationId == null) {
            queryResult = stazioniRepository.findAllByFiltersOrderById(broker.getObjId(), pageable);
        } else {
            queryResult =
                    stazioniRepository.findAllByFiltersOrderById(broker.getObjId(), stationId, pageable);
        }
        List<StationDetails> stations =
                queryResult.stream()
                        .map(station -> modelMapper.map(station, StationDetails.class))
                        .collect(Collectors.toList());
        return StationDetailsList.builder()
                .pageInfo(Utility.buildPageInfo(queryResult))
                .stationsDetailsList(stations)
                .build();
    }

    public CreditorInstitutionDetails getCreditorInstitutionsAssociatedToBroker(@NotNull Integer limit, @NotNull Integer pageNumber, @NotNull String brokerId, Boolean enabled) {

        Page<PaStazionePa> page = paStazionePaRepository.findAll(
                PaStazionePaSpecifications.filter(brokerId, enabled),
                PageRequest.of(pageNumber, limit));

        return CreditorInstitutionDetails.builder()
                .creditorInstitutions(page.stream()
                        .map(entity -> modelMapper.map(entity, CreditorInstitutionDetail.class))
                        .collect(Collectors.toList()))
                .pageInfo(Utility.buildPageInfo(page))
                .build();
    }

    protected IntermediariPa getBrokerIfExists(String brokerId) throws AppException {
        Optional<IntermediariPa> result = intermediariPaRepository.findByIdIntermediarioPa(brokerId);
        if (result.isEmpty()) {
            throw new AppException(AppError.BROKER_NOT_FOUND, brokerId);
        }
        return result.get();
    }
}
