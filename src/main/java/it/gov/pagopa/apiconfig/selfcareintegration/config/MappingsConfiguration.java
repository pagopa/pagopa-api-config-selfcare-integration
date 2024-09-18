package it.gov.pagopa.apiconfig.selfcareintegration.config;

import it.gov.pagopa.apiconfig.selfcareintegration.mapper.*;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionDetail;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CreditorInstitutionInfo;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.CIStationRelation;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.ICIStationRelation;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbanDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.iban.IbanEnhanced;
import it.gov.pagopa.apiconfig.starter.entity.Canali;
import it.gov.pagopa.apiconfig.starter.entity.IbanMaster;
import it.gov.pagopa.apiconfig.starter.entity.Pa;
import it.gov.pagopa.apiconfig.starter.entity.PaStazionePa;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for model mapper definitions
 */
@Configuration
public class MappingsConfiguration {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // insert here the new mappers
        mapper.createTypeMap(Canali.class, ChannelDetails.class).setConverter(new ConvertCanaliToChannelDetails());
        mapper.createTypeMap(IbanMaster.class, IbanDetails.class).setConverter(new ConvertIbanMasterToIbanDetail());
        mapper.createTypeMap(PaStazionePa.class, CreditorInstitutionDetail.class)
                .setConverter(new ConvertPaStazionePaToCreditorInstitutionDetail());

        mapper.createTypeMap(Pa.class, CreditorInstitutionInfo.class).setConverter(new ConvertPaToCreditorInstitutionInfo());
        mapper.createTypeMap(IbanMaster.class, IbanEnhanced.class).setConverter(new ConvertIbanMasterToIbanDetailsTemp());
        mapper.createTypeMap(ICIStationRelation.class, CIStationRelation.class)
                .setConverter(new ConvertICIStationRelationToCIStationRelation());

        return mapper;
    }
}
