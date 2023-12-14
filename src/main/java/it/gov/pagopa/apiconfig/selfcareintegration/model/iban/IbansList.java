package it.gov.pagopa.apiconfig.selfcareintegration.model.iban;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.apiconfig.selfcareintegration.model.PageInfo;
import it.gov.pagopa.apiconfig.selfcareintegration.model.station.StationDetails;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/** Ibans */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IbansList {

  @JsonProperty("ibans")
  @NotNull
  @Schema(description = "List of IBANs associated to the passed creditor institutions")
  private List<IbanDetails> ibans;

  @JsonProperty("page_info")
  @Schema()
  @NotNull
  @Valid
  private PageInfo pageInfo;
}
