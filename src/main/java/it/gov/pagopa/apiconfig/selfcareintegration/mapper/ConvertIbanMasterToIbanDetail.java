package it.gov.pagopa.apiconfig.selfcareintegration.mapper;

import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbanDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbanLabel;
import it.gov.pagopa.apiconfig.starter.entity.Iban;
import it.gov.pagopa.apiconfig.starter.entity.IbanAttributeMaster;
import it.gov.pagopa.apiconfig.starter.entity.IbanMaster;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertIbanMasterToIbanDetail implements Converter<IbanMaster, IbanDetails> {

    @Override
    public IbanDetails convert(MappingContext<IbanMaster, IbanDetails> context) {
        IbanMaster src = context.getSource();

        List<IbanLabel> labels = src.getIbanAttributesMasters().stream()
                .map(IbanAttributeMaster::getIbanAttribute)
                .map(label -> IbanLabel.builder()
                        .name(label.getAttributeName())
                        .description(label.getAttributeDescription())
                        .build())
                .collect(Collectors.toList());

        Iban iban = src.getIban();
        return IbanDetails.builder()
                .creditorInstitutionFiscalCode(src.getPa().getIdDominio())
                .creditorInstitutionName(src.getPa().getRagioneSociale())
                .iban(iban.getIban())
                .insertedDate(OffsetDateTime.ofInstant(src.getInsertedDate().toInstant(), ZoneId.of("UTC")))
                .validityDate(OffsetDateTime.ofInstant(src.getValidityDate().toInstant(), ZoneId.of("UTC")))
                .dueDate(OffsetDateTime.ofInstant(iban.getDueDate().toInstant(), ZoneId.of("UTC")))
                .description(iban.getDescription())
                .ownerFiscalCode(iban.getFiscalCode())
                .labels(labels)
                .build();
    }
}
