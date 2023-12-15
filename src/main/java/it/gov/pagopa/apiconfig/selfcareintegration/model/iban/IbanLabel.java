package it.gov.pagopa.apiconfig.selfcareintegration.model.iban;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/** Iban label */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IbanLabel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
