package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbanDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbansList;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedIbanMasterRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Utility;
import it.gov.pagopa.apiconfig.starter.entity.IbanMaster;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional
public class IbansService {

    @Autowired
    private ExtendedIbanMasterRepository extendedIbanMasterRepository;

    @Autowired
    private ModelMapper modelMapper;

    public IbansList getIbans(@NotEmpty @Size(min = 1, max = 100) List<String> creditorInstitutions, @NotNull Pageable page) {
        Page<IbanMaster> queryResult = extendedIbanMasterRepository.findAllByPa_idDominioIn(creditorInstitutions, page);

        List<IbanDetails> details =
                queryResult.stream()
                        .map(iban -> modelMapper.map(iban, IbanDetails.class))
                        .collect(Collectors.toList());

        return IbansList.builder()
                .pageInfo(Utility.buildPageInfo(queryResult))
                .ibans(details)
                .build();

    }
}
