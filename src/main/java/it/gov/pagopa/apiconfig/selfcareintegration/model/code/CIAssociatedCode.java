package it.gov.pagopa.apiconfig.selfcareintegration.model.code;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** Code associated with Creditor Institution */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CIAssociatedCode {

  @JsonProperty("code")
  @NotNull
  @Size(min = 2, max = 2, message = "Code must be composed by 2 characters")
  @Schema(description = "The code that bound uniquely a creditor institution to a station")
  private String code;

  @JsonProperty("name")
  @Schema(description = "The name of the station associated to the creditor institution, if exists")
  private String stationName;
}
