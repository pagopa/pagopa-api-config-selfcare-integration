package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetailsList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedChannelRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Utility;
import org.apache.commons.lang3.stream.Streams;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PSPsService {

    private final ExtendedChannelRepository extendedChannelRepository;

    private final ModelMapper modelMapper;

    public PSPsService(ExtendedChannelRepository extendedChannelRepository, ModelMapper modelMapper) {
        this.extendedChannelRepository = extendedChannelRepository;
        this.modelMapper = modelMapper;
    }

    public ChannelDetailsList getChannelByFiscalCode(String pspFiscalCode, Pageable pageable) {
        var queryResult = extendedChannelRepository.findAllByPspFiscalCode(pspFiscalCode, pageable);

        List<ChannelDetails> channelsDetailsList = queryResult.stream()
                .map(elem -> modelMapper.map(elem, ChannelDetails.class))
                .collect(Collectors.toList());

        return ChannelDetailsList.builder()
                .channelsDetailsList(channelsDetailsList)
                .pageInfo(Utility.buildPageInfo(queryResult))
                .build();
    }


}


