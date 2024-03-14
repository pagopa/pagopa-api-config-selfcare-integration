package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedChannelRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Utility;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PspService {

    private final ExtendedChannelRepository extendedChannelRepository;

    private final ModelMapper modelMapper;

    public PspService(ExtendedChannelRepository extendedChannelRepository, ModelMapper modelMapper) {
        this.extendedChannelRepository = extendedChannelRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ChannelDetailsList getChannelByFiscalCode(String pspFiscalCode, Pageable pageable) {
        var queryResult = extendedChannelRepository.findAllByPspFiscalCode(pspFiscalCode, pageable);

        List<ChannelDetails> channelsDetailsList = queryResult.stream()
                .map(elem -> modelMapper.map(elem, ChannelDetails.class))
                .toList();

        return ChannelDetailsList.builder()
                .channelsDetailsList(channelsDetailsList)
                .pageInfo(Utility.buildPageInfo(queryResult))
                .build();
    }


}


