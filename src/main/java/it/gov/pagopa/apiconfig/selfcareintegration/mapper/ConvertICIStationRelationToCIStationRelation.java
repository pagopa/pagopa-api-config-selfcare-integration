package it.gov.pagopa.apiconfig.selfcareintegration.mapper;

import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CIStationRelation;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.ICIStationRelation;
import it.gov.pagopa.apiconfig.selfcareintegration.util.Utility;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

/**
 * Converter class, define how to map the {@link ICIStationRelation} entity into the {@link CIStationRelation} model
 */
public class ConvertICIStationRelationToCIStationRelation implements Converter<ICIStationRelation, CIStationRelation> {

    @Override
    public CIStationRelation convert(MappingContext<ICIStationRelation, CIStationRelation> context) {
        ICIStationRelation source = context.getSource();

        return CIStationRelation.builder()
                .ciTaxCode(source.getIdDominio())
                .segregationCode(Utility.getDoubleDigitCode(source.getSegregazione()))
                .build();
    }
}
