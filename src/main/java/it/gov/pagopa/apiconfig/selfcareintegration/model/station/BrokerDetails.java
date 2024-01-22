package it.gov.pagopa.apiconfig.selfcareintegration.model.station;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * BrokerDetails
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrokerDetails extends Broker {

    @JsonProperty("extended_fault_bean")
    @Schema(
            description = "Parameter to find out whether or not the extended fault bean has been enabled")
    @NotNull
    private Boolean faultBeanEsteso;
}
