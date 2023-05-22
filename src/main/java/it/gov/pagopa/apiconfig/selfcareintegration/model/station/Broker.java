package it.gov.pagopa.apiconfig.selfcareintegration.model.station;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** BrokerDetails */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Broker {

  @JsonProperty("broker_code")
  @Schema(example = "223344556677889900", description = "Code used to identify the intermediate EC")
  @NotBlank
  @Size(max = 35)
  private String idIntermediarioPa;

  @JsonProperty("broker_details")
  @Schema(example = "Regione Veneto", description = "Name and generic details of the intermediate EC")
  @NotBlank
  private String codiceIntermediario;

  @JsonProperty("enabled")
  @Schema(description = "Parameter to find out whether or not the intermediate has been enabled")
  @NotNull
  private Boolean enabled;
}
