package it.gov.pagopa.apiconfig.selfcareintegration.mapper;

import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbanEnhanced;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbanLabel;
import it.gov.pagopa.apiconfig.starter.entity.Iban;
import it.gov.pagopa.apiconfig.starter.entity.IbanAttributeMaster;
import it.gov.pagopa.apiconfig.starter.entity.IbanMaster;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public class ConvertIbanMasterToIbanDetailsTemp implements Converter<IbanMaster, IbanEnhanced> {

    @Override
    public IbanEnhanced convert(MappingContext<IbanMaster, IbanEnhanced> context) {
        IbanMaster src = context.getSource();

        List<IbanLabel> labels = src.getIbanAttributesMasters().stream()
                .map(IbanAttributeMaster::getIbanAttribute)
                .map(label -> IbanLabel.builder()
                        .name(label.getAttributeName())
                        .description(label.getAttributeDescription())
                        .build())
                .toList();

        Iban iban = src.getIban();
        return IbanEnhanced.builder()
                .ciOwnerFiscalCode(src.getPa().getIdDominio())
                .companyName(src.getPa().getRagioneSociale())
                .ibanValue(iban.getIban())
                .publicationDate(OffsetDateTime.ofInstant(src.getInsertedDate().toInstant(), ZoneId.of("UTC")))
                .validityDate(OffsetDateTime.ofInstant(src.getValidityDate().toInstant(), ZoneId.of("UTC")))
                .dueDate(OffsetDateTime.ofInstant(iban.getDueDate().toInstant(), ZoneId.of("UTC")))
                .description(src.getDescription())
                .labels(labels)
                .build();
    }
}
