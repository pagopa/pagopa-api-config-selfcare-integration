package it.gov.pagopa.apiconfig.selfcareintegration.service;

import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppError;
import it.gov.pagopa.apiconfig.selfcareintegration.exception.AppException;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbanDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbansList;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbansListTemp;
import it.gov.pagopa.apiconfig.selfcareintegration.repository.ExtendedIbanMasterRepository;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Utility;
import it.gov.pagopa.apiconfig.starter.entity.IbanMaster;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.repository.PaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional
public class IbansService {

    private final ExtendedIbanMasterRepository extendedIbanMasterRepository;

    @Autowired
    private PaRepository paRepository;

    @Autowired
    private ModelMapper modelMapper;

    public IbansService(ExtendedIbanMasterRepository extendedIbanMasterRepository) {
        this.extendedIbanMasterRepository = extendedIbanMasterRepository;
    }

    public IbansList getIbans(@NotEmpty List<String> creditorInstitutions, @NotNull Pageable page) {
        Page<IbanMaster> queryResult = extendedIbanMasterRepository.findAllByPa_idDominioIn(creditorInstitutions, page);
        return IbansList.builder()
                .pageInfo(Utility.buildPageInfo(queryResult))
                .ibans(queryResult.stream()
                        .map(iban -> modelMapper.map(iban, IbanDetails.class))
                        .toList())
                .build();
    }

    public IbansListTemp getIbansList(@NotNull String organizationFiscalCode, String label) {
        Pa pa = getPaIfExists(organizationFiscalCode);

        List<IbanMaster> ibanMasters;
        if (label == null || label.isEmpty()) {
            ibanMasters = extendedIbanMasterRepository.findByFkPa(pa.getObjId());
        } else {
            ibanMasters = extendedIbanMasterRepository.findByFkPaAndLabel(pa.getObjId(), label);
        }

        List<IbanDetails> ibanDetailsList = ibanMasters.stream()
                .map(elem -> modelMapper.map(elem, IbanDetails.class))
                .collect(Collectors.toList());

        if(ibanDetailsList.isEmpty() && (("ACA").equals(label) || ("0201138TS").equals(label))) {
            IbanMaster lastPublishedIban = getLastPublishedIban(pa);
            if(lastPublishedIban != null) {
                ibanDetailsList.add(modelMapper.map(lastPublishedIban, IbanDetails.class));
            }
        }

        return IbansListTemp.builder()
                .ibans(ibanDetailsList)
                .build();
    }

    private Pa getPaIfExists(String organizationFiscalCode) {
        return paRepository.findByIdDominio(organizationFiscalCode)
                .orElseThrow(() -> new AppException(AppError.CREDITOR_INSTITUTION_NOT_FOUND, organizationFiscalCode));
    }

    private IbanMaster getLastPublishedIban(Pa pa) {
        List<IbanMaster> activeIbans = pa.getIbanMasters().stream()
                .filter(ibanPa -> ibanPa.getValidityDate().before(Timestamp.valueOf(LocalDateTime.now())))
                .collect(Collectors.toList());
        return activeIbans.stream().max(Comparator.comparing(IbanMaster::getInsertedDate)).orElse(null);
    }

}
