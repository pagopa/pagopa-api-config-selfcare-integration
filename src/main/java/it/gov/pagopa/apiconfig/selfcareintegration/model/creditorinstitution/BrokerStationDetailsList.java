package it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.apiconfig.selfcareintegration.model.creditorinstitution.StationDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.validation.constraints.NotNull;
import java.util.List;

/** Stations */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrokerStationDetailsList {

  @JsonProperty("stations")
  @NotNull
  @Schema(description = "List of stations associated to the same broker")
  private List<StationDetails> stationsDetailsList;

}
