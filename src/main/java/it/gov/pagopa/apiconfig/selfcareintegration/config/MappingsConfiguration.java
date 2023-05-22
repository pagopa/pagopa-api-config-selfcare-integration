package it.gov.pagopa.apiconfig.selfcareintegration.config;

import it.gov.pagopa.apiconfig.selfcareintegration.mapper.ConvertCanaliToChannelDetails;
import it.gov.pagopa.apiconfig.selfcareintegration.model.channel.ChannelDetails;
import it.gov.pagopa.apiconfig.starter.entity.Canali;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappingsConfiguration {

  @Bean
  ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    // insert here the new mappers
    mapper
        .createTypeMap(Canali.class, ChannelDetails.class)
        .setConverter(new ConvertCanaliToChannelDetails());

    return mapper;
  }
}
