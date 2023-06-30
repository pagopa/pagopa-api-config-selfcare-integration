package it.gov.pagopa.apiconfig.selfcareintegration.model.station;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.apiconfig.selfcareintegration.model.PageInfo;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.*;

/** Stations */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationDetailsList {

  @JsonProperty("stations")
  @NotNull
  @Schema(description = "List of stations associated to the same entity")
  private List<StationDetails> stationsDetailsList;

  @JsonProperty("page_info")
  @Schema()
  @NotNull
  @Valid
  private PageInfo pageInfo;
}
