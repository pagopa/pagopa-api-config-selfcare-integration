package it.gov.pagopa.apiconfig.selfcareintegration.model.iban;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/** Ibans */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IbansListTemp {

  @JsonProperty("ibans")
  @NotNull
  @Schema(description = "List of IBANs associated to the passed creditor institutions")
  private List<IbanDetailsTemp> ibans;

}
