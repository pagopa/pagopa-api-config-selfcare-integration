package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppError;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedChannelRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Utility;
import it.gov.pagopa.apiconfig.starter.entity.Canali;
import it.gov.pagopa.apiconfig.starter.entity.IntermediariPsp;
import it.gov.pagopa.apiconfig.starter.repository.IntermediariPspRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BrokerPSPsService {

    private final ExtendedChannelRepository channelRepository;

    private final IntermediariPspRepository brokerPspRepository;

    private final ModelMapper modelMapper;

    public BrokerPSPsService(ExtendedChannelRepository channelRepository, IntermediariPspRepository brokerPspRepository, ModelMapper modelMapper) {
        this.channelRepository = channelRepository;
        this.brokerPspRepository = brokerPspRepository;
        this.modelMapper = modelMapper;
    }

    public ChannelDetailsList getChannelDetailsFromPSPBroker(
            @NotNull String brokerId, String channelId, Pageable pageable) throws AppException {
        IntermediariPsp broker = getBrokerIfExists(brokerId);
        Page<Canali> queryResult;
        if (channelId == null) {
            queryResult =
                    channelRepository.findByFkIntermediarioPsp_objIdOrderByIdCanale(
                            broker.getObjId(), pageable);
        } else {
            queryResult =
                    channelRepository.findAllByFiltersOrderById(broker.getObjId(), channelId, pageable);
        }
        List<ChannelDetails> channels =
                queryResult
                        .stream()
                        .map(station -> modelMapper.map(station, ChannelDetails.class))
                        .collect(Collectors.toList());
        return ChannelDetailsList.builder()
                .pageInfo(Utility.buildPageInfo(queryResult))
                .channelsDetailsList(channels)
                .build();
    }

    protected IntermediariPsp getBrokerIfExists(String brokerId) throws AppException {
        Optional<IntermediariPsp> result = brokerPspRepository.findByIdIntermediarioPsp(brokerId);
        if (result.isEmpty()) {
            throw new AppException(AppError.BROKER_NOT_FOUND, brokerId);
        }
        return result.get();
    }
}
