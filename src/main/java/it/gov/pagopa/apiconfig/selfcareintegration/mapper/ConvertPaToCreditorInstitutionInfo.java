package it.gov.pagopa.apiconfig.selfcareintegration.mapper;

import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionInfo;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import javax.validation.Valid;

/**
 * Converter class, define how to map the {@link Pa} entity into the {@link CreditorInstitutionInfo} model
 */
public class ConvertPaToCreditorInstitutionInfo implements Converter<Pa, CreditorInstitutionInfo> {

    @Override
    public CreditorInstitutionInfo convert(MappingContext<Pa, CreditorInstitutionInfo> context) {
        @Valid Pa pa = context.getSource();
        return CreditorInstitutionInfo.builder()
                .ciTaxCode(pa.getIdDominio())
                .businessName(pa.getRagioneSociale() != null ? pa.getRagioneSociale() : "")
                .build();
    }
}
