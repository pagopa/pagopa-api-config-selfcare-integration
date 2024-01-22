package it.gov.pagopa.apiconfig.selfcareintegration.model.code;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Codes associated with Creditor Institution
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CIAssociatedCodeList {

    @JsonProperty("used")
    @Schema(description = "List of codes already used for existing associations")
    private List<CIAssociatedCode> usedCodes;

    @JsonProperty("unused")
    @NotNull
    @Schema(description = "List of codes not used for existing associations")
    private List<CIAssociatedCode> unusedCodes;
}
